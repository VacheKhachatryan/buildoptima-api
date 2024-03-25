package com.vecondev.buildoptima.parameters.property;

import com.amazonaws.services.s3.model.S3Object;
import com.vecondev.buildoptima.dto.property.PropertyListDto;
import com.vecondev.buildoptima.dto.property.PropertyReadDto;
import com.vecondev.buildoptima.model.property.migration.MigrationHistory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PropertyMigrationServiceTestParameters {

  public List<S3Object> getObjectsFromBucket() {
    return new ArrayList<>(List.of(new S3Object(), new S3Object(), new S3Object()));
  }

  public MigrationHistory getMigrationHistory() {
    return new MigrationHistory(UUID.randomUUID(), "100.json.gz", Instant.now(), true, null, null);
  }

  public List<MigrationHistory> getMigrationHistoryList() {
    return List.of(
        new MigrationHistory(UUID.randomUUID(), "101.json.gz", Instant.now(), true, null, null),
        new MigrationHistory(UUID.randomUUID(), "102.json.gz", Instant.now(), true, null, null),
        new MigrationHistory(
            UUID.randomUUID(),
            "103.json",
            Instant.now(),
            true,
            Instant.now(),
            "Not in GZIP format"),
        new MigrationHistory(
            UUID.randomUUID(),
            "104.json",
            Instant.now(),
            true,
            Instant.now(),
            "Not in GZIP format"));
  }

  public Path convertS3ObjectToPath() {
    return Paths.get("anyPath");
  }

  public PropertyListDto readFromJson() {
    List<PropertyReadDto> propertyReadDtoList =
        List.of(new PropertyReadDto(), new PropertyReadDto(), new PropertyReadDto());
    return new PropertyListDto(propertyReadDtoList);
  }
}
