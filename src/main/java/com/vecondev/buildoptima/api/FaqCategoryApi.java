package com.vecondev.buildoptima.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.vecondev.buildoptima.dto.Metadata;
import com.vecondev.buildoptima.dto.faq.request.FaqCategoryRequestDto;
import com.vecondev.buildoptima.dto.faq.response.FaqCategoryResponseDto;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.exception.ApiError;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

@Tag(
    name = "FAQ Category",
    description = "Endpoints for managing FAQ categories",
    externalDocs =
        @ExternalDocumentation(
            description = "Click here to see a detailed explanation of application errors",
            url =
                "https://github.com/vecondev/buildoptima-api/blob/develop/docs/application-errors.md"))
public interface FaqCategoryApi extends SecuredApi, FetchingApi {

  @Operation(
      summary = "Create new FAQ category",
      description = "Possible error codes: 40011, 4011, 4012, 4013, 4014, 4031, 4042, 4094, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "New FAQ category should be created.",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FaqCategoryResponseDto.class))),
        @ApiResponse(
            responseCode = "400",
            ref = "#/components/responses/MethodArgumentNotValidResponse"),
        @ApiResponse(
            responseCode = "404",
            description = "User not found with such id.",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(
            responseCode = "409",
            description = "There already is an duplicate value for category name.",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
      })
  ResponseEntity<FaqCategoryResponseDto> create(FaqCategoryRequestDto requestDto);

  @Operation(
      summary = "Get FAQ category by id",
      description = "Possible error codes: 4011, 4012, 4013, 4014, 4031, 4044, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The FAQ Category should be retrieved",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FaqCategoryResponseDto.class))),
        @ApiResponse(
            responseCode = "404",
            description = "There is no FAQ Category found with such Id",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
      })
  ResponseEntity<FaqCategoryResponseDto> getById(UUID id);

  @Operation(
      summary = "Get all FAQ categories",
      description = "Possible error codes: 4011, 4012, 4013, 4014, 4031, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "All FAQ Categories should be retrieved",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FaqCategoryResponseDto.class)))
      })
  ResponseEntity<List<FaqCategoryResponseDto>> getAll();

  @RequestBody(ref = "#/components/requestBodies/FetchFaqCategoriesRequestExample")
  ResponseEntity<FetchResponseDto> fetch(FetchRequestDto fetchRequestDto);

  @Operation(
      summary = "Update the FAQ category",
      description =
          "Possible error codes: 40011, 4011, 4012, 4013, 4014, 4031, 4042, 4044, 4094, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The FAQ Category should be updated",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FaqCategoryResponseDto.class))),
        @ApiResponse(
            responseCode = "400",
            ref = "#/components/responses/MethodArgumentNotValidResponse"),
        @ApiResponse(
            responseCode = "404",
            description = "There is no FAQ Category or User found with such Id",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(
            responseCode = "409",
            description = "There already is an duplicate value for category name.",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
      })
  ResponseEntity<FaqCategoryResponseDto> update(UUID id, FaqCategoryRequestDto requestDto);

  @Operation(
      summary = "Delete the FAQ category",
      description = "Possible error codes: 4011, 4012, 4013, 4014, 4031, 4044, 4096, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The FAQ Category should be deleted",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FaqCategoryResponseDto.class))),
        @ApiResponse(
            responseCode = "404",
            description = "There is no FAQ Category found with such Id",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(
            responseCode = "409",
            description = """
                          There are dependent FAQ questions, so category
                          can only be deleted after deleting that questions.""",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
      })
  ResponseEntity<Void> delete(UUID id);

  @Operation(
      summary = "Exporting all FAQ categories in '.csv' format",
      description = "Possible error codes: 4011, 4012, 4013, 4014, 4031, 5004, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "All faq categories should be exported",
            content = {
              @Content(mediaType = "application/csv"),
              @Content(mediaType = APPLICATION_JSON_VALUE)
            })
      })
  ResponseEntity<Resource> exportInCsv();

  @Operation(
      summary = "Get FAQ Category metadata",
      description = "Possible error codes: 4011, 4012, 4013, 4014, 4031, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The metadata should be got",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Metadata.class)))
      })
  ResponseEntity<Metadata> getMetadata();
}
