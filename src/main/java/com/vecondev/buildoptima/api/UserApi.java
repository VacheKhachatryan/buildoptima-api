package com.vecondev.buildoptima.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.vecondev.buildoptima.dto.ImageOverview;
import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.dto.filter.FetchResponseDto;
import com.vecondev.buildoptima.dto.user.request.ChangePasswordRequestDto;
import com.vecondev.buildoptima.dto.user.request.EditUserDto;
import com.vecondev.buildoptima.dto.user.response.UserResponseDto;
import com.vecondev.buildoptima.exception.ApiError;
import com.vecondev.buildoptima.security.user.AppUserDetails;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "Endpoints for managing users", externalDocs =
    @ExternalDocumentation(
        description = "Click here to see a detailed explanation of application errors",
        url =
                "https://github.com/vecondev/buildoptima-api/blob/develop/docs/application-errors.md"))
public interface UserApi extends SecuredApi, FetchingApi {

  @Operation(
      summary = "Get user profile",
      description = "Possible error codes: 4011, 4012, 4013, 4014, 4031, 4042, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Fetched a user from DB",
            content =
                @Content(
                    schema = @Schema(implementation = UserResponseDto.class),
                    mediaType = APPLICATION_JSON_VALUE)),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content =
                @Content(
                    schema = @Schema(implementation = ApiError.class),
                    mediaType = APPLICATION_JSON_VALUE))
      })
  ResponseEntity<UserResponseDto> getById(@PathVariable("id") UUID id,
      @Parameter(hidden = true) AppUserDetails user);

  @Operation(
      summary = "Get current user",
      description = "Possible error codes: 4011, 4012, 4013, 4014, 4031, 4042, 5007",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponse(
      responseCode = "200",
      description = "Fetched a user from DB",
      content =
      @Content(
          schema = @Schema(implementation = UserResponseDto.class),
          mediaType = APPLICATION_JSON_VALUE))
  ResponseEntity<UserResponseDto> getCurrentUser();

  @RequestBody(ref = "#/components/requestBodies/FetchUsersRequestExample")
  ResponseEntity<FetchResponseDto> fetch(
            FetchRequestDto fetchRequestDto);

  @Operation(
      summary = "Change password",
      description =
          "Possible error codes: 4001, 40011, 4011, 4012, 4013, 4014, 4031, 4042, 4091,  5007",
      security = @SecurityRequirement(name = "api-security"))
  @RequestBody(
      content =
          @Content(
              schema = @Schema(implementation = ChangePasswordRequestDto.class),
              mediaType = APPLICATION_JSON_VALUE))
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "User password was successfully changed"),
        @ApiResponse(
            responseCode = "400",
            ref = "#/components/responses/MethodArgumentNotValidResponse"),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content =
                @Content(
                    schema = @Schema(implementation = ApiError.class),
                    mediaType = APPLICATION_JSON_VALUE)),
        @ApiResponse(
            responseCode = "409",
            description = "Provided the same password in change password request",
            content =
                @Content(
                    schema = @Schema(implementation = ApiError.class),
                    mediaType = APPLICATION_JSON_VALUE))
      })
  ResponseEntity<Void> changePassword(ChangePasswordRequestDto request);

  @Operation(
      summary = """
          Edit User info. If any field is not to be changed,
          please provide the present user state
          """,
      description =
          "Possible error codes: 4001, 40011, 4011, 4012, 4013, 4014, 4031, 4042, 4091,  5007",
      security = @SecurityRequirement(name = "api-security"))
  @RequestBody(
      content =
          @Content(
              schema = @Schema(implementation = EditUserDto.class),
              mediaType = APPLICATION_JSON_VALUE))
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "User info successfully edited",
            content = @Content(schema = @Schema(implementation = UserResponseDto.class),
            mediaType = APPLICATION_JSON_VALUE)),
        @ApiResponse(
            responseCode = "400",
            ref = "#/components/responses/MethodArgumentNotValidResponse"),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content =
                @Content(
                    schema = @Schema(implementation = ApiError.class),
                    mediaType = APPLICATION_JSON_VALUE))
      })
  ResponseEntity<UserResponseDto> editUser(UUID userId,
      @Parameter(hidden = true) AppUserDetails user,
      EditUserDto editUserDto);

  @Operation(
      summary = "Upload new image or update the previous one of given user",
      description = """
              Possible error codes: 4004, 4011, 4012, 4013, 4014, 4031, 4121,
              4122, 4123, 4124, 5002, 5003, 5005, 5006, 5007, 5009.
              The image has following requirements (extension: jpeg/jpg/png,
              min_width: 600px, max_width: 600px, size: 70KB-30MB).
              To get the uploaded image urls: original image url: 
              'https://buildoptima.s3.amazonaws.com/user/{user_id}/original/{version}' 
              thumbnail image url: 'https://buildoptima.s3.amazonaws.com/user/{user_id}/thumbnail/{version}'""",
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "204",
              description = "The image is successfully uploaded/updated",
              content = @Content(mediaType = APPLICATION_JSON_VALUE)),
          @ApiResponse(
              responseCode = "400",
              description =
                  "Image is not provided",
              content =
              @Content(
                  mediaType = APPLICATION_JSON_VALUE,
                  schema = @Schema(implementation = ApiError.class))),
          @ApiResponse(
              responseCode = "412",
              description =
                  "The provided image  doesn't fit the requirements",
              content =
                  @Content(
                      mediaType = APPLICATION_JSON_VALUE,
                      schema = @Schema(implementation = ApiError.class))),
      })
  ResponseEntity<ImageOverview> uploadImage(
      @Parameter(description = "The user's id whom photo should be uploaded") UUID id,
      @Parameter(description = "The image user want to upload") MultipartFile multipartFile,
      @Parameter(hidden = true) AppUserDetails user);

  @Operation(
      summary = "Download the original image of given user",
      description = """
              Possible error codes: 4011, 4012, 4013, 4014, 4031, 4042, 4045, 5005, 5007.
              The permission to download the image has only the resource owner and the admin
              """,
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The image is successfully downloaded",
            content = {
              @Content(mediaType = MediaType.IMAGE_JPEG_VALUE),
              @Content(mediaType = MediaType.IMAGE_PNG_VALUE),
              @Content(mediaType = APPLICATION_JSON_VALUE)
            }),
        @ApiResponse(
            responseCode = "404",
            description = "The image or user not found",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
      })
  ResponseEntity<byte[]> downloadOriginalImage(
      @Parameter(description = "The user id whom image should be downloaded") UUID ownerId,
      @Parameter(hidden = true) AppUserDetails user);

  @Operation(
      summary = "Download the thumbnail image by given id",
          description = """
              Possible error codes: 4011, 4012, 4013, 4014, 4031, 4042, 4045, 5005, 5007.
              The permission to download the image has only the resource owner and the admin
              """,
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The image is successfully downloaded",
            content = {
              @Content(mediaType = MediaType.IMAGE_JPEG_VALUE),
              @Content(mediaType = MediaType.IMAGE_PNG_VALUE),
              @Content(mediaType = APPLICATION_JSON_VALUE)
            }),
        @ApiResponse(
            responseCode = "404",
            description = "The image or user not found",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
      })
  ResponseEntity<byte[]> downloadThumbnailImage(
      @Parameter(description = "The user id whom image should be downloaded") UUID ownerId,
      @Parameter(hidden = true) AppUserDetails user);

  @Operation(
      summary = "Delete the images (original, thumbnail) of given user",
      description = """
              Possible error codes: 4011, 4012, 4013, 4014, 4042, 4045, 5007.
              The permission to download the image has only the resource owner and the admin.
              """,
      security = @SecurityRequirement(name = "api-security"))
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "The image is successfully deleted"),
        @ApiResponse(
            responseCode = "404",
            description = "The image or user not found",
            content =
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)))
      })
  ResponseEntity<Void> deleteImage(
      @Parameter(description = "The user id whom image should be deleted") UUID ownerId,
      @Parameter(hidden = true) AppUserDetails user);
}
