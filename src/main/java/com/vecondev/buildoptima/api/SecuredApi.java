package com.vecondev.buildoptima.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.vecondev.buildoptima.exception.ApiError;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized request",
          content =
              @Content(
                  schema = @Schema(implementation = ApiError.class),
                  mediaType = APPLICATION_JSON_VALUE)),
      @ApiResponse(
          responseCode = "403",
          description = """
                        Authenticated user hasn't permission to deal with
                        these resources (Should be either MODERATOR or ADMIN)""",
          content =
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ApiError.class)))
    })
public interface SecuredApi { }
