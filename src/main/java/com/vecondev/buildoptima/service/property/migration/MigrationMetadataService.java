package com.vecondev.buildoptima.service.property.migration;

import com.vecondev.buildoptima.dto.property.PropertyReadDto;
import com.vecondev.buildoptima.model.property.Property;
import com.vecondev.buildoptima.model.property.migration.MigrationHistory;
import com.vecondev.buildoptima.model.property.migration.MigrationMetadata;

public interface MigrationMetadataService {

  MigrationMetadata save(MigrationHistory migrationHistory, Property property);

  MigrationMetadata save(
      MigrationHistory migrationHistory, PropertyReadDto property, String failedReason);
}
