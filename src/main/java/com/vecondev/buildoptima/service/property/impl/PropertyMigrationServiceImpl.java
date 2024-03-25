package com.vecondev.buildoptima.service.property.impl;

import static com.vecondev.buildoptima.util.FileUtil.convertS3ObjectToPath;
import static com.vecondev.buildoptima.util.JsonUtil.*;

import com.amazonaws.services.s3.model.S3Object;
import com.vecondev.buildoptima.config.properties.S3ConfigProperties;
import com.vecondev.buildoptima.dto.property.PropertyListDto;
import com.vecondev.buildoptima.dto.property.PropertyReadDto;
import com.vecondev.buildoptima.dto.property.response.PropertyMigrationProgressResponseDto;
import com.vecondev.buildoptima.dto.property.response.PropertyMigrationResponseDto;
import com.vecondev.buildoptima.dto.property.response.PropertyReprocessResponseDto;
import com.vecondev.buildoptima.mapper.property.AddressMapper;
import com.vecondev.buildoptima.mapper.property.PropertyMapper;
import com.vecondev.buildoptima.model.property.Address;
import com.vecondev.buildoptima.model.property.Property;
import com.vecondev.buildoptima.model.property.migration.MigrationHistory;
import com.vecondev.buildoptima.repository.property.PropertyRepository;
import com.vecondev.buildoptima.service.property.PropertyMigrationService;
import com.vecondev.buildoptima.service.property.migration.MigrationHistoryService;
import com.vecondev.buildoptima.service.property.migration.MigrationMetadataService;
import com.vecondev.buildoptima.service.s3.AmazonS3Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.REPEATABLE_READ, noRollbackFor = Exception.class)
public class PropertyMigrationServiceImpl implements PropertyMigrationService {

  private final MigrationHistoryService migrationHistoryService;
  private final MigrationMetadataService migrationMetadataService;
  private final AmazonS3Service amazonS3Service;

  private final PropertyRepository propertyRepository;

  private final PropertyMapper propertyMapper;

  private final S3ConfigProperties s3ConfigProperties;

  @Override
  public List<MigrationHistory> migrateFromS3() {
    List<S3Object> unprocessedFiles =
        amazonS3Service.getObjects(s3ConfigProperties.getDataBucketName()).stream()
            .filter(object -> !migrationHistoryService.existsByKey(object.getKey()))
            .toList();
    int sizeOfUnprocessedFiles = unprocessedFiles.size();
    log.info("Retrieved {} property files from S3 to process", sizeOfUnprocessedFiles);
    List<MigrationHistory> processedFilesBefore = migrationHistoryService.findAll();
    if (sizeOfUnprocessedFiles != 0) {
      processFiles(unprocessedFiles, false);
      log.info("{} property files from S3 were processed", sizeOfUnprocessedFiles);
    } else {
      log.info("No new property files were found in S3 bucket");
    }
    return processedFilesBefore;
  }

  @Override
  public void migrateFromS3(S3Object s3Object) {
    processFiles(List.of(s3Object), true);
  }

  @Override
  public PropertyMigrationResponseDto getMigrationResults(
      List<MigrationHistory> processedFilesBefore) {
    List<MigrationHistory> allProcessedFiles = migrationHistoryService.findAll();

    return new PropertyMigrationResponseDto(
        allProcessedFiles.size(),
        getFailedFilesWithReasons(allProcessedFiles),
        allProcessedFiles.size() - processedFilesBefore.size(),
        (allProcessedFiles.stream()
                .filter(history -> history.getFailedAt() == null)
                .toList()
                .size())
            - (processedFilesBefore.stream()
                .filter(history -> history.getFailedAt() == null)
                .toList()
                .size()),
        propertyRepository.findAll().size());
  }

  /**
   * Reprocess all the files that have been failed before.
   *
   * @return the all files that have been failed to process before
   */
  @Override
  public List<MigrationHistory> reprocessFailedFiles() {
    List<MigrationHistory> failedToProcessFiles =
        migrationHistoryService.findAllByFailedAtNotNull();
    List<S3Object> filesToReprocess =
        failedToProcessFiles.stream()
            .filter(
                file ->
                    amazonS3Service.doesObjectExist(
                        s3ConfigProperties.getDataBucketName(), file.getFilePath()))
            .map(
                file ->
                    amazonS3Service.getObject(
                        s3ConfigProperties.getDataBucketName(), file.getFilePath()))
            .toList();
    failedToProcessFiles.stream()
        .filter(
            file ->
                !amazonS3Service.doesObjectExist(
                    s3ConfigProperties.getDataBucketName(), file.getFilePath()))
        .forEach(file -> migrationHistoryService.deleteByFilePath(file.getFilePath()));
    int filesToReprocessCount = filesToReprocess.size();
    if (filesToReprocessCount != 0) {
      processFiles(filesToReprocess, false);
      log.info("{} property files from S3 were re-processed", filesToReprocessCount);
    } else {
      log.info("No property files were found in S3 bucket to re-process");
    }

    return failedToProcessFiles;
  }

  /**
   * Get the results of last reprocess.
   *
   * @param failedFilesBefore the all files that have been failed to process before
   */
  @Override
  public PropertyReprocessResponseDto getReprocessResults(
      List<MigrationHistory> failedFilesBefore) {
    Map<String, String> allFailedFilesToProcess =
        getFailedFilesWithReasons(migrationHistoryService.findAll());

    return new PropertyReprocessResponseDto(
        failedFilesBefore.size() - allFailedFilesToProcess.size(), allFailedFilesToProcess);
  }

  @Override
  public PropertyMigrationProgressResponseDto getMigrationProgress() {
    List<MigrationHistory> allProcessedFiles = migrationHistoryService.findAll();
    Map<String, String> allFailedFilesToProcess = getFailedFilesWithReasons(allProcessedFiles);
    List<Property> allProperties = propertyRepository.findAll();

    return new PropertyMigrationProgressResponseDto(
        allProcessedFiles.size(), allFailedFilesToProcess, allProperties.size());
  }

  private void  processFiles(List<S3Object> unprocessedFiles, boolean isDelta) {
    int sizeOfUnprocessedFiles = unprocessedFiles.size();
    CountDownLatch countDownLatch = new CountDownLatch(sizeOfUnprocessedFiles);
    ExecutorService executorService = Executors.newFixedThreadPool(sizeOfUnprocessedFiles);
    unprocessedFiles.forEach(
            s3Object -> {
              Runnable runnable = () -> {
                try {
                  saveProperty(
                      readFromJson(convertS3ObjectToPath(s3Object).toFile()),
                      migrationHistoryService.saveSucceededHistory(s3Object.getKey(), isDelta));
                  log.info("""
                          {} file from S3 was successfully processed
                          and removed from local storage""",
                      s3Object.getKey());
                } catch (Exception e) {
                  migrationHistoryService
                      .saveFailedHistory(s3Object.getKey(), e.getMessage(), isDelta);
                  log.info("""
                          Failed processing {} file from S3.
                          See failed reason in migration metadata""",
                      s3Object.getKey());
                }
                countDownLatch.countDown();
              };
              executorService.submit(runnable);
            });
    try {
      countDownLatch.await();
    } catch (InterruptedException ex) {
      ex.printStackTrace();
      Thread.currentThread().interrupt();
    }
  }


  /**
   * Saves the whole property data in database and the property addresses in OpenSearch.
   */
  private void saveProperty(PropertyListDto propertyListDto, MigrationHistory migrationHistory) {
    List<PropertyReadDto> properties = propertyListDto.getProperties();
    properties.forEach(
        propertyDto -> {
          try {
            Optional<Property> fromDb = propertyRepository.findById(propertyDto.getAin());
            Property savedProperty;
            if (fromDb.isEmpty()) {
              savedProperty =
                  propertyRepository.save(propertyMapper.mapToEntity(propertyDto));
            } else {
              savedProperty = update(propertyDto, fromDb.get());
            }
            migrationMetadataService.save(migrationHistory, savedProperty);
          } catch (Exception e) {
            migrationMetadataService.save(migrationHistory, propertyDto, e.getMessage());
          }
        });
  }

  private Map<String, String> getFailedFilesWithReasons(List<MigrationHistory> migrationHistories) {
    return migrationHistories.stream()
        .filter(history -> history.getFailedAt() != null)
        .collect(
            Collectors.toMap(MigrationHistory::getFilePath,
                migration ->
                    (migration.getFailedReason() == null) ? " - " : migration.getFailedReason()));
  }

  private Property update(PropertyReadDto propertyDto, Property toUpdate) {
    Property property = propertyMapper.mapToEntity(propertyDto);
    List<Address> addresses = toUpdate.getAddresses();
    toUpdate.removeAddresses(addresses);
    toUpdate.setMunicipality(property.getMunicipality());
    toUpdate.addAddresses(property.getAddresses());
    toUpdate.setLocations(property.getLocations());
    toUpdate.setDetails(property.getDetails());
    toUpdate.setHazards(property.getHazards());
    toUpdate.setZoningDetails(property.getZoningDetails());
    return propertyRepository.save(toUpdate);
  }
}
