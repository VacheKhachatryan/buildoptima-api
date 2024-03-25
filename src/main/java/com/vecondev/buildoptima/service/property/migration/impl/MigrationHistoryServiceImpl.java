package com.vecondev.buildoptima.service.property.migration.impl;

import static com.vecondev.buildoptima.exception.Error.MIGRATION_HISTORY_NOT_FOUND;

import com.vecondev.buildoptima.exception.ResourceNotFoundException;
import com.vecondev.buildoptima.model.property.migration.MigrationHistory;
import com.vecondev.buildoptima.repository.property.MigrationHistoryRepository;
import com.vecondev.buildoptima.service.property.migration.MigrationHistoryService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(noRollbackFor = Exception.class)
public class MigrationHistoryServiceImpl implements MigrationHistoryService {

  private final MigrationHistoryRepository historyRepository;

  @Override
  public MigrationHistory saveSucceededHistory(String filePath, boolean isDelta) {
    return save(filePath, null, null, isDelta);
  }

  @Override
  public MigrationHistory saveFailedHistory(String filePath, String failedReason, boolean isDelta) {
    return save(filePath, failedReason, Instant.now(), isDelta);
  }

  @Override
  public boolean existsByKey(String filePath) {
    return historyRepository.existsByFilePath(filePath);
  }

  @Override
  public List<MigrationHistory> findAll() {
    return historyRepository.findAll();
  }

  @Override
  public List<MigrationHistory> findAllByFailedAtNotNull() {
    return historyRepository.findAllByFailedAtNotNull();
  }

  @Override
  public void deleteByFilePath(String filePath) {
    historyRepository.deleteByFilePath(filePath);
  }

  private MigrationHistory save(
      String filePath, String failedReason, Instant failedAt, boolean isDelta) {
    MigrationHistory migrationHistory;
    if (!historyRepository.existsByFilePath(filePath)) {
      migrationHistory =
          historyRepository.saveAndFlush(
              MigrationHistory.builder()
                  .filePath(filePath)
                  .delta(isDelta)
                  .failedAt(failedAt)
                  .failedReason(failedReason)
                  .build());
      log.info("Successfully saved processing history of file {}", filePath);
    } else {
      migrationHistory =
          historyRepository.saveAndFlush(
              historyRepository
                  .findByFilePath(filePath)
                  .orElseThrow(() -> new ResourceNotFoundException(MIGRATION_HISTORY_NOT_FOUND))
                  .toBuilder()
                  .failedAt(failedAt)
                  .delta(isDelta)
                  .failedReason(failedReason)
                  .build());
      log.info("Successfully updated processing history of file {}", filePath);
    }
    return migrationHistory;
  }
}
