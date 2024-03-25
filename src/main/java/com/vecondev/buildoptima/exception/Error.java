package com.vecondev.buildoptima.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Error contains: CODE a unique code which value's first 3 numbers represent the response status
 * code. HTTP STATUS the response status code MESSAGE the error message
 */
@Getter
@RequiredArgsConstructor
public enum Error {
  PROVIDED_WRONG_PASSWORD(4001, BAD_REQUEST, "Provided Wrong Password In Change Password Request"),
  INVALID_PAGEABLE(4002, BAD_REQUEST, "The `skip` Value Should Be Divisible To `take`Value"),
  INVALID_FILTER_STRUCTURE(4003, BAD_REQUEST, "Invalid Filter Structure"),
  IMAGE_IS_REQUIRED(4004, BAD_REQUEST, "No Image Was Passed With Request"),
  INVALID_INSTANT(4006, BAD_REQUEST, "Can't Parse String Value To Instant"),
  INVALID_ROLE(4007, BAD_REQUEST, "Can't Parse String Value To Role Enum"),
  INVALID_STATUS(4008, BAD_REQUEST, "Can't Parse String Value To Status Enum"),
  INVALID_FIELD(4009, BAD_REQUEST, "Invalid Field In Fetch Request"),
  CONSTRAINT_VIOLATION(40011, BAD_REQUEST, "There Is An Invalid Value In User Input"),
  REFRESH_TOKEN_EXPIRED(40012, BAD_REQUEST, "Expired Refresh Token"),
  REFRESH_TOKEN_INVALID(40013, BAD_REQUEST, "Invalid Refresh Token"),
  INVALID_SORTING_FIELD(40014, BAD_REQUEST, "Invalid Sorting Field In Fetch Request"),

  BAD_CREDENTIALS(4011, UNAUTHORIZED, "Bad Credentials"),
  NOT_ACTIVE_ACCOUNT(4015, UNAUTHORIZED, "User Email Is Not Verified"),

  ACCESS_DENIED(4031, FORBIDDEN, "Permission Denied To Requested Resource"),

  CONFIRM_TOKEN_NOT_FOUND(4041, NOT_FOUND, "Confirmation Token Not Found"),
  USER_NOT_FOUND(4042, NOT_FOUND, "There Is No User With Such Id"),
  FAQ_QUESTION_NOT_FOUND(4043, NOT_FOUND, "There Is No FAQ Question With Such Id"),
  FAQ_CATEGORY_NOT_FOUND(4044, NOT_FOUND, "There Is No FAQ Category With Such Id"),
  IMAGE_NOT_FOUND(4045, NOT_FOUND, "There Is No Image For The Given User"),
  NEWS_ITEM_NOT_FOUND(4046, NOT_FOUND, "News Item Not Found"),
  MIGRATION_HISTORY_NOT_FOUND(
      4047, NOT_FOUND, "There Is No Migration History With Such Id/File_Path."),
  MIGRATION_METADATA_NOT_FOUND(
      4048, NOT_FOUND, "There Is No Migration Metadata With Such Id/Property_AIN."),
  PROPERTY_NOT_FOUND(4049, NOT_FOUND, "There Is No Property Data With Such Ain"),

  PROVIDED_SAME_PASSWORD(4091, CONFLICT, "Provided The Same Password In Change Password Request"),
  USER_ALREADY_EXIST_WITH_EMAIL(4092, CONFLICT, "There Is a User Registered With Such Email"),
  USER_ALREADY_EXIST_WITH_PHONE(
      4093, CONFLICT, "There Is a User Registered With Such Phone Number"),
  FAQ_CATEGORY_ALREADY_EXIST(4094, CONFLICT, "There Is a FAQ Category Created With Such Name"),
  FAQ_QUESTION_ALREADY_EXIST(
      4095, CONFLICT, "There Is a FAQ Question Created With Such Question Content"),
  CATEGORY_HAS_QUESTIONS(
      4096, CONFLICT, "The Category Has Questions, To Delete It First Delete It's Questions"),

  INVALID_IMAGE_FORMAT(4121, PRECONDITION_FAILED, "Cannot Upload The Image"),
  INVALID_IMAGE_SIZE(
      4122, PRECONDITION_FAILED, "Image Has Smaller Size Than It's Required (600x600)"),
  INVALID_FILE_SIZE(4123, PRECONDITION_FAILED, "File Size Should Be Between 70KB and 30MB"),
  INVALID_IMAGE_EXTENSION(
      4124, PRECONDITION_FAILED, "The Extension Of The Image Should Be Either 'jpg/jpeg' or 'png'"),

  SEND_EMAIL_FAILED(5001, INTERNAL_SERVER_ERROR, "Failed To Send An Email"),
  FAILED_MULTIPART_CONVERTING(
      5002, INTERNAL_SERVER_ERROR, "Error Occurred While Converting Multipart File To File"),
  FAILED_IMAGE_RESIZING(5003, INTERNAL_SERVER_ERROR, "Error Occurred While Resizing The Image"),
  FAILED_CSV_CONVERTING(
      5004, INTERNAL_SERVER_ERROR, "Error Occurred While Converting Result To '.csv' Format"),
  FAILED_IMAGE_CONVERTING(
      5005, INTERNAL_SERVER_ERROR, "Error Occurred While Processing Downloaded Image"),
  BUCKET_NOT_FOUND(5006, INTERNAL_SERVER_ERROR, "Cannot Find Bucket In AWS S3"),
  FAILED_JSON_CONVERTING(5007, INTERNAL_SERVER_ERROR, "Error Occurred While Parsing To Json"),
  FAILED_READ_FROM_JSON(50010, INTERNAL_SERVER_ERROR, "Error Occurred While Reading From Json"),
  FAILED_KEY_READ(5008, INTERNAL_SERVER_ERROR, "Error Occurred While Retrieving Security Keys"),
  FAILED_FILE_DELETION(5009, INTERNAL_SERVER_ERROR, "Error Occurred While Deleting The File"),
  FAILED_DATA_DOWNLOAD(50011, INTERNAL_SERVER_ERROR, "Error Occurred While Downloading The Data"),
  FAILED_INDEX_CREATION(
      50012, INTERNAL_SERVER_ERROR, "Error Occurred While Creating AWS OpenSearch Index"),
  FAILED_BULK_DOCUMENT(
      50013, INTERNAL_SERVER_ERROR, "Error Occurred While Adding Data To OpenSearch Index"),
  FAILED_SEARCH(
      50014, INTERNAL_SERVER_ERROR, "Error Occurred While Performing Search In OpenSearch");

  private final Integer code;
  private final HttpStatus httpStatus;
  private final String message;
}
