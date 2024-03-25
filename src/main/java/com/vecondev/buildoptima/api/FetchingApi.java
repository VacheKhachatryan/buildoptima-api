package com.vecondev.buildoptima.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.exception.ApiError;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;

public interface FetchingApi {

  @Operation(
      summary = "Fetch filtered, sorted and paged ",
      description = "Possible error codes: 4002, 4003, 40014, 4011, 4012, 4013, 4014, 4031, 5007",
      security = @SecurityRequirement(name = "api-security"),
      externalDocs =
          @ExternalDocumentation(
              description =
                  "Click here to see a detailed explanation of this endpoint requirements",
              url =
                  "https://github.com/vecondev/buildoptima-api/blob/develop/docs/filtering-sorting.md"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Fetched results from DB",
            content =
                @Content(
                    schema = @Schema(implementation = FetchResponseDto.class),
                    mediaType = APPLICATION_JSON_VALUE)),
        @ApiResponse(
            responseCode = "400",
            description = "There is an invalid value in request body",
            content =
                @Content(
                    schema = @Schema(implementation = ApiError.class),
                    mediaType = APPLICATION_JSON_VALUE))
      })
  ResponseEntity<FetchResponseDto> fetch(FetchRequestDto fetchRequestDto);
}
