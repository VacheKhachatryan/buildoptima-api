package com.vecondev.buildoptima.repository.property;

import com.vecondev.buildoptima.model.property.migration.MigrationHistory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MigrationHistoryRepository extends JpaRepository<MigrationHistory, UUID> {
  boolean existsByFilePath(String key);

  List<MigrationHistory> findAllByFailedAtNotNull();

  void deleteByFilePath(String filePath);

  List<MigrationHistory> findAllByFailedAtIsNull();

  Optional<MigrationHistory> findByFilePath(String filePath);
}
