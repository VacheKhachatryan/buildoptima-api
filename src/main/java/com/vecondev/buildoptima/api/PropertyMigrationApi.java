package com.vecondev.buildoptima.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.vecondev.buildoptima.dto.property.response.PropertyMigrationProgressResponseDto;
import com.vecondev.buildoptima.dto.property.response.PropertyMigrationResponseDto;
import com.vecondev.buildoptima.dto.property.response.PropertyReprocessResponseDto;
import com.vecondev.buildoptima.exception.ApiError;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(
    name = "Property migration",
    description = "Endpoints for managing property migrations",
    externalDocs =
        @ExternalDocumentation(
            description = "Click here to see a detailed explanation of application errors",
            url =
                "https://github.com/vecondev/buildoptima-api/blob/develop/docs/application-errors.md"))
public interface PropertyMigrationApi extends SecuredApi {

  @Operation(
      summary = "Migrate all unprocessed files",
      description = "Possible error codes: 4011, 4012, 4013, 4014, 4031, 4047, 4048, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "All files were processed",
            content =
                @Content(
                    schema = @Schema(implementation = PropertyMigrationResponseDto.class),
                    mediaType = APPLICATION_JSON_VALUE)),
        @ApiResponse(
            responseCode = "404",
            description =
                "Migration metadata or Migration history not found with such id/property_ain",
            content =
                @Content(
                    schema = @Schema(implementation = ApiError.class),
                    mediaType = APPLICATION_JSON_VALUE))
      })
  ResponseEntity<PropertyMigrationResponseDto> migrateUnprocessedFiles();

  @Operation(
      summary = "Re-process all failed files",
      description = "Possible error codes: 4011, 4012, 4013, 4014, 4031, 4047, 4048, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "All failed files were re-processed",
            content =
                @Content(
                    schema = @Schema(implementation = PropertyReprocessResponseDto.class),
                    mediaType = APPLICATION_JSON_VALUE)),
        @ApiResponse(
            responseCode = "404",
            description =
                "Migration metadata or Migration history not found with such id/property_ain",
            content =
                @Content(
                    schema = @Schema(implementation = ApiError.class),
                    mediaType = APPLICATION_JSON_VALUE))
      })
  ResponseEntity<PropertyReprocessResponseDto> reprocessFailedToProcessFiles();

  @Operation(
      summary = "Track the migration progress",
      description = "Possible error codes: 4011, 4012, 4013, 4014, 4031, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The information about file migrations was got",
            content =
                @Content(
                    schema = @Schema(implementation = PropertyMigrationProgressResponseDto.class),
                    mediaType = APPLICATION_JSON_VALUE))
      })
  ResponseEntity<PropertyMigrationProgressResponseDto> trackMigrationProgress();
}
