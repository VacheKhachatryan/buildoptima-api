package com.vecondev.buildoptima.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.vecondev.buildoptima.dto.Metadata;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.dto.news.request.NewsCreateRequestDto;
import com.vecondev.buildoptima.dto.news.request.NewsUpdateRequestDto;
import com.vecondev.buildoptima.dto.news.response.NewsResponseDto;
import com.vecondev.buildoptima.exception.ApiError;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

@Tag(name = "News", description = "Endpoints for managing news", externalDocs =
    @ExternalDocumentation(
        description = "Click here to see a detailed explanation of application errors",
        url =
                "https://github.com/vecondev/buildoptima-api/blob/develop/docs/application-errors.md"))
public interface NewsApi extends SecuredApi, FetchingApi {

  @Operation(
      summary = "Add news item",
      description = """
                    Possible error codes: 40011, 4011, 4012, 4013, 4014,
                    4031, 4121, 4122, 4123, 4124, 5002, 5003, 5005, 5006, 5007, 5009""",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "News has been added",
            content = @Content(schema = @Schema(implementation = UUID.class))),
        @ApiResponse(
            responseCode = "400",
            ref = "#/components/responses/MethodArgumentNotValidResponse"),
        @ApiResponse(
            responseCode = "412",
            description =
                "Image is not provided or the provided image  doesn't fit the requirements",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
      })
  ResponseEntity<NewsResponseDto> create(
      NewsCreateRequestDto createNewsRequestDto);

  @Operation(
      summary = "Get news item by id",
      description = """
              Possible error codes: 4011, 4012, 4013, 4014, 4031, 4046,  5007.
              To get the uploaded image urls: original image url: 'https://buildoptima.s3.amazonaws.com/news/{news_id}/original/{version}' thumbnail image url: 'https://buildoptima.s3.amazonaws.com/news/{news_id}/thumbnail/{version}'
              """,
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "Fetched news item from DB",
              content =
              @Content(
                  schema = @Schema(implementation = NewsResponseDto.class),
                  mediaType = APPLICATION_JSON_VALUE)),
          @ApiResponse(
              responseCode = "404",
              description = "No News item found by id",
              content =
              @Content(
                  schema = @Schema(implementation = NewsResponseDto.class),
                  mediaType = APPLICATION_JSON_VALUE))
      })
  ResponseEntity<NewsResponseDto> getById(
      @Parameter(description = "The news item id which should be fetched") UUID id);

  @RequestBody(ref = "#/components/requestBodies/FetchNewsRequestExample")
  ResponseEntity<FetchResponseDto> fetch(
            FetchRequestDto fetchRequestDto);

  @Operation(summary = "Update news item",
          description = "Possible error codes: 40011, 4011, 4012, 4013, 4014, 4031, 4046, 5007.",
          security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Updated the news item",
            content =
                @Content(
                    schema = @Schema(implementation = NewsResponseDto.class),
                    mediaType = APPLICATION_JSON_VALUE)),
        @ApiResponse(
            responseCode = "400",
            ref = "#/components/responses/MethodArgumentNotValidResponse"),
        @ApiResponse(
            responseCode = "404",
            description = "News item not found by id",
            content =
                @Content(
                    schema = @Schema(implementation = ApiError.class),
                    mediaType = APPLICATION_JSON_VALUE))
      })
  ResponseEntity<NewsResponseDto> update(
      @Parameter(description = "The news item id which should be updated") UUID id,
      NewsUpdateRequestDto newsRequestDto);

  @Operation(
      summary = "Export news csv",
          description = """
                        Possible error codes: 4002, 4003, 40014, 4011, 4012,
                        4013, 4014, 4031, 5004, 5007.""",
          security = @SecurityRequirement(name = "api-security"),
      externalDocs =
          @ExternalDocumentation(
              description =
                  "Click here to see a detailed explanation of this endpoint requirements",
              url =
                  "https://github.com/vecondev/buildoptima-api/blob/develop/docs/filtering-sorting.md"))
  @RequestBody(ref = "#/components/requestBodies/FetchNewsRequestExample")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully exported news csv"),
          @ApiResponse(
              responseCode = "400",
              description = "There is an invalid value in fetch request",
              content =
              @Content(
                  schema = @Schema(implementation = ApiError.class),
                  mediaType = APPLICATION_JSON_VALUE))
      })
  ResponseEntity<Resource> exportInCsv(
      FetchRequestDto fetchRequestDto);

  @Operation(summary = "Delete news item",
          description = "Possible error codes: 4011, 4012, 4013, 4014, 4031, 4046, 5007.",
          security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "News item has been deleted"),
          @ApiResponse(
              responseCode = "404",
              description = "There is no News item found by given id",
              content =
              @Content(
                  schema = @Schema(implementation = ApiError.class),
                  mediaType = APPLICATION_JSON_VALUE))})
  ResponseEntity<Void> delete(
      @Parameter(description = "The news item id which should be deleted") UUID id);

  @Operation(summary = "Get news metadata",
          description = "Possible error codes: 4011, 4012, 4013, 4014, 4031, 5007.",
          security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {@ApiResponse(responseCode = "200", description = "Successfully got news metadata",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE))})
  ResponseEntity<Metadata> getMetadata();

  @Operation(summary = "Archive news item",
          description = "Possible error codes: 4011, 4012, 4013, 4014, 4031, 4046, 5007.",
          security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully archived news item",
              content =
                  @Content(
                      schema = @Schema(implementation = NewsResponseDto.class),
                      mediaType = APPLICATION_JSON_VALUE)),
          @ApiResponse(
              responseCode = "404",
              description = "No News item found by given id",
              content =
              @Content(
                  schema = @Schema(implementation = ApiError.class),
                  mediaType = APPLICATION_JSON_VALUE))
      })
  ResponseEntity<NewsResponseDto> archive(
      @Parameter(description = "The news item id which should be archived") UUID id);
}
