package com.vecondev.buildoptima.api.controller;

import com.vecondev.buildoptima.api.PropertyMigrationApi;
import com.vecondev.buildoptima.dto.property.response.PropertyMigrationProgressResponseDto;
import com.vecondev.buildoptima.dto.property.response.PropertyMigrationResponseDto;
import com.vecondev.buildoptima.dto.property.response.PropertyReprocessResponseDto;
import com.vecondev.buildoptima.model.property.migration.MigrationHistory;
import com.vecondev.buildoptima.service.property.PropertyMigrationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/properties")
@PreAuthorize("hasAuthority('resource_write')")
public class PropertyMigrationController implements PropertyMigrationApi {

  private final PropertyMigrationService propertyMigrationService;

  @Override
  @PostMapping("/migrate")
  public ResponseEntity<PropertyMigrationResponseDto> migrateUnprocessedFiles() {
    List<MigrationHistory> processedFilesBefore = propertyMigrationService.migrateFromS3();

    return ResponseEntity.ok(propertyMigrationService.getMigrationResults(processedFilesBefore));
  }

  @Override
  @PostMapping("/re-process")
  public ResponseEntity<PropertyReprocessResponseDto> reprocessFailedToProcessFiles() {
    List<MigrationHistory> failedFilesBefore = propertyMigrationService.reprocessFailedFiles();

    return ResponseEntity.ok(propertyMigrationService.getReprocessResults(failedFilesBefore));
  }

  @Override
  @GetMapping("/progress")
  public ResponseEntity<PropertyMigrationProgressResponseDto> trackMigrationProgress() {
    return ResponseEntity.ok(propertyMigrationService.getMigrationProgress());
  }
}
