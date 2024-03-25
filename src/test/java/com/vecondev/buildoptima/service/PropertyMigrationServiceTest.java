package com.vecondev.buildoptima.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.model.S3Object;
import com.vecondev.buildoptima.config.properties.S3ConfigProperties;
import com.vecondev.buildoptima.dto.property.response.PropertyMigrationProgressResponseDto;
import com.vecondev.buildoptima.dto.property.response.PropertyMigrationResponseDto;
import com.vecondev.buildoptima.dto.property.response.PropertyReprocessResponseDto;
import com.vecondev.buildoptima.model.property.migration.MigrationHistory;
import com.vecondev.buildoptima.parameters.property.PropertyMigrationServiceTestParameters;
import com.vecondev.buildoptima.repository.property.PropertyRepository;
import com.vecondev.buildoptima.service.property.impl.PropertyMigrationServiceImpl;
import com.vecondev.buildoptima.service.property.migration.MigrationHistoryService;
import com.vecondev.buildoptima.service.property.migration.MigrationMetadataService;
import com.vecondev.buildoptima.service.s3.AmazonS3Service;
import com.vecondev.buildoptima.util.FileUtil;
import com.vecondev.buildoptima.util.JsonUtil;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class PropertyMigrationServiceTest {

  private final PropertyMigrationServiceTestParameters testParameters =
      new PropertyMigrationServiceTestParameters();
  @InjectMocks private PropertyMigrationServiceImpl propertyService;
  @Mock private AmazonS3Service amazonS3Service;
  @Mock private MigrationHistoryService migrationHistoryService;
  @Mock private MigrationMetadataService migrationMetadataService;
  @Mock private S3ConfigProperties s3ConfigProperties;
  @Mock private PropertyRepository propertyRepository;

  @Test
  void successfulPropertiesMigration() {
    Path path = testParameters.convertS3ObjectToPath();

    when(amazonS3Service.getObjects(anyString())).thenReturn(testParameters.getObjectsFromBucket());
    when(migrationHistoryService.existsByKey(any())).thenReturn(true);

    try (MockedStatic<JsonUtil> jsonUtil = Mockito.mockStatic(JsonUtil.class);
        MockedStatic<FileUtil> fileUtil = Mockito.mockStatic(FileUtil.class)) {
      jsonUtil
          .when(() -> JsonUtil.readFromJson(any(File.class)))
          .thenReturn(testParameters.readFromJson());
      fileUtil.when(() -> FileUtil.convertS3ObjectToPath(any(S3Object.class))).thenReturn(path);
      propertyService.migrateFromS3();
      verify(migrationHistoryService).findAll();
    }
  }

  @Test
  void successfulRetrievalOfMigrationResults() {
    List<MigrationHistory> migrationHistoriesBefore = testParameters.getMigrationHistoryList();
    List<MigrationHistory> migrationHistories = new ArrayList<>(migrationHistoriesBefore);
    migrationHistories.add(testParameters.getMigrationHistory());
    List<MigrationHistory> failedMigrationHistories =
        migrationHistories.stream().filter(history -> history.getFailedAt() != null).toList();

    when(migrationHistoryService.findAll()).thenReturn(migrationHistories);

    PropertyMigrationResponseDto response =
        propertyService.getMigrationResults(migrationHistoriesBefore);
    assertEquals(migrationHistories.size(), response.getAllProcessedFiles());
    assertEquals(failedMigrationHistories.size(), response.getAllFailedFilesToProcess().size());
    verify(propertyRepository).findAll();
  }

  @Test
  void successfulReprocessOfFailedFiles() {
    List<MigrationHistory> migrationHistories = testParameters.getMigrationHistoryList();
    List<MigrationHistory> failedMigrationHistories =
        migrationHistories.stream().filter(history -> history.getFailedAt() != null).toList();

    when(migrationHistoryService.findAllByFailedAtNotNull()).thenReturn(failedMigrationHistories);
    when(amazonS3Service.doesObjectExist(any(), any())).thenReturn(true);
    when(amazonS3Service.getObject(any(), any())).thenReturn(new S3Object());

    List<MigrationHistory> failedToProcessFiles = propertyService.reprocessFailedFiles();
    assertEquals(failedMigrationHistories.size(), failedToProcessFiles.size());
    verify(s3ConfigProperties, times(failedMigrationHistories.size() + migrationHistories.size()))
        .getDataBucketName();
  }

  @Test
  void successfulRetrievalOfReprocessResults() {
    List<MigrationHistory> migrationHistories = testParameters.getMigrationHistoryList();
    List<MigrationHistory> failedMigrationHistoriesBefore =
        migrationHistories.stream().filter(history -> history.getFailedAt() != null).toList();

    when(migrationHistoryService.findAll()).thenReturn(migrationHistories);

    PropertyReprocessResponseDto response =
        propertyService.getReprocessResults(failedMigrationHistoriesBefore);
    assertEquals(
        failedMigrationHistoriesBefore.size(), response.getAllFailedFilesToProcess().size());
  }

  @Test
  void successfulRetrievalOfMigrationProgress() {
    List<MigrationHistory> migrationHistories = testParameters.getMigrationHistoryList();
    List<MigrationHistory> failedMigrationHistoriesBefore =
        migrationHistories.stream().filter(history -> history.getFailedAt() != null).toList();

    when(migrationHistoryService.findAll()).thenReturn(migrationHistories);

    PropertyMigrationProgressResponseDto response = propertyService.getMigrationProgress();
    assertEquals(migrationHistories.size(), response.getAllProcessedFiles());
    assertEquals(
        failedMigrationHistoriesBefore.size(), response.getAllFailedFilesToProcess().size());
    verify(propertyRepository).findAll();
  }
}
