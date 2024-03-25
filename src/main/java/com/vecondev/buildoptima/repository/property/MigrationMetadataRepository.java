package com.vecondev.buildoptima.repository.property;

import com.vecondev.buildoptima.model.property.migration.MigrationMetadata;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MigrationMetadataRepository extends JpaRepository<MigrationMetadata, UUID> {

  boolean existsByAin(String ain);

  Optional<MigrationMetadata> findByAin(String ain);
}
