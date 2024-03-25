package com.vecondev.buildoptima.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.vecondev.buildoptima.dto.EntityOverview;
import com.vecondev.buildoptima.dto.Metadata;
import com.vecondev.buildoptima.dto.faq.request.FaqQuestionRequestDto;
import com.vecondev.buildoptima.dto.faq.response.FaqCategoryResponseDto;
import com.vecondev.buildoptima.dto.faq.response.FaqQuestionResponseDto;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.exception.ApiError;
import com.vecondev.buildoptima.filter.model.DictionaryField;
import com.vecondev.buildoptima.model.Status;
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
import org.springframework.web.bind.annotation.PathVariable;

@Tag(
    name = "FAQ Question",
    description = "Endpoints for managing FAQ questions",
    externalDocs =
        @ExternalDocumentation(
            description = "Click here to see a detailed explanation of application errors",
            url =
                "https://github.com/vecondev/buildoptima-api/blob/develop/docs/application-errors.md"))
public interface FaqQuestionApi extends SecuredApi, FetchingApi {

  @Operation(
      summary = "Create new FAQ Question",
      description =
          "Possible error codes: 40011, 4011, 4012, 4013, 4014, 4031, 4042, 4044, 4095, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "New FAQ Question should be created.",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FaqQuestionResponseDto.class))),
        @ApiResponse(
            responseCode = "400",
            ref = "#/components/responses/MethodArgumentNotValidResponse"),
        @ApiResponse(
            responseCode = "404",
            description = "There is no category OR user found with such id.",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(
            responseCode = "409",
            description = "There already is an duplicate value for the question.",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
      })
  ResponseEntity<FaqQuestionResponseDto> create(FaqQuestionRequestDto requestDto);

  @Operation(
      summary = "Get FAQ Question by id",
      description = "Possible error codes: 4011, 4012, 4013, 4014, 4031, 4043, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The FAQ Question should be got",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FaqCategoryResponseDto.class))),
        @ApiResponse(
            responseCode = "404",
            description = "There is no FAQ Question found with such Id",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
      })
  ResponseEntity<FaqQuestionResponseDto> getById(@PathVariable UUID id);

  @Operation(
      summary = "Get all FAQ Questions",
      description = "Possible error codes: 4011, 4012, 4013, 4014, 4031, 4043, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The FAQ Questions should be retrieved",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FaqQuestionResponseDto.class)))
      })
  ResponseEntity<List<FaqQuestionResponseDto>> getAll();

  @RequestBody(ref = "#/components/requestBodies/FetchFaqQuestionsRequestExample")
  ResponseEntity<FetchResponseDto> fetch(FetchRequestDto fetchRequestDto);

  @Operation(
      summary = "Update the FAQ Question",
      description =
          "Possible error codes: 40011, 4011, 4012, 4013, 4014, 4031, 4042, 4043, 4044, 4095, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "New FAQ Question should be created.",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FaqQuestionResponseDto.class))),
        @ApiResponse(
            responseCode = "400",
            ref = "#/components/responses/MethodArgumentNotValidResponse"),
        @ApiResponse(
            responseCode = "404",
            description = "One of the resources of category, question and user not found",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(
            responseCode = "409",
            description = "There already is an duplicate value for the question.",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
      })
  ResponseEntity<FaqQuestionResponseDto> update(UUID id, FaqQuestionRequestDto requestDto);

  @Operation(
      summary = "Delete the FAQ Question",
      description = "Possible error codes: 4011, 4012, 4013, 4014, 4031, 4043, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The FAQ Question should be deleted",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FaqCategoryResponseDto.class))),
        @ApiResponse(
            responseCode = "404",
            description = "There is no FAQ Question found with such Id",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
      })
  ResponseEntity<Void> delete(@PathVariable UUID id);

  @Operation(
      summary = "Exporting all FAQ questions in '.csv' format",
      description = "Possible error codes: 4011, 4012, 4013, 4014, 4031, 5004, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "All faq questions should be exported",
            content = {
              @Content(mediaType = "application/csv"),
              @Content(mediaType = APPLICATION_JSON_VALUE)
            })
      })
  ResponseEntity<Resource> exportInCsv();

  @Operation(
      summary = "Get FAQ Question metadata",
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

  @Operation(
      summary = """
                Find all users who updated questions with given status,
                or all categories that have questions with such status""",
      description = "Possible error codes: 4009, 4011, 4012, 4013, 4014, 4031, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The result should be successfully got",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = EntityOverview.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid dictionary field to lookup for",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = EntityOverview.class)))
      })
  ResponseEntity<List<EntityOverview>> lookup(Status status, DictionaryField dictionary);
}
