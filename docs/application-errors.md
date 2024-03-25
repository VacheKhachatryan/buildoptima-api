# Application Errors

## Overview

We use Errors to specify exceptional situations with a unique error code, a http status and an error message.

## Explanation

The reason we use errors with unique codes is that many exceptional situations may have the same http status, 
and the frontend needs them to be able to handle the exceptions properly.

## Example

Below is the sample structure of payload of API error

```json
{
  "status": "CONFLICT",
  "errorCode": 4092,
  "timestamp": "2022-07-21T11:39:10.291Z",
  "message": "There Is a User With Such Email"
}
```

```json
{
  "status": "CONFLICT",
  "errorCode": 4093,
  "timestamp": "2022-07-21T11:39:10.291Z",
  "message": "There Is a User With Such Phone Number"
}
```

#### Errors for 400 BAD REQUEST

| Error Name                                                    | Code  | Message                                                |
|:--------------------------------------------------------------|:------|:-------------------------------------------------------|
| `PROVIDED_WRONG_PASSWORD`                                     | 4001  | `Provided Wrong Password In Change Password Request`   |
| `INVALID_PAGEABLE`                                            | 4002  | `The -skip- Value Should Be Divisible To -take- Value` |
| `INVALID_FILTER_STRUCTURE`                                    | 4003  | `Invalid Filter Structure`                             |
| `IMAGE_IS_REQUIRED`                                           | 4004  | `No Image Was Passed With Request`                     |
| `INVALID_INSTANT`                                             | 4006  | `Can't Parse String Value To Instant`                  |
| `INVALID_ROLE`                                                | 4007  | `Can't Parse String Value To Role Enum`                |
| `INVALID_STATUS`                                              | 4008  | `Can't Parse String Value To Status Enum`              |
| `INVALID_FIELD`                                               | 4009  | `Invalid Field In Fetch Request`                       |
| `CONSTRAINT_VIOLATION`                                        | 40011 | `There Is An Invalid Value In User Input`              |
| `REFRESH_TOKEN_EXPIRED`                                       | 40012 | `Expired Refresh Token`                                |
| `REFRESH_TOKEN_INVALID`                                       | 40013 | `Invalid Refresh Token`                                |
| `INVALID_SORTING_FIELD`                                       | 40014 | `Invalid Sorting Field In Fetch Request`               |

#### Errors for 401 UNAUTHORIZED and 403 FORBIDDEN

| Error Name              | Code | Message                                   |
|:------------------------|:-----|:------------------------------------------|
| `BAD_CREDENTIALS`       | 4011 | `Bad Credentials`                         |
| `ACCESS_TOKEN_MISSING`  | 4012 | `Access Token Missing`                    |
| `INVALID_ACCESS_TOKEN`  | 4013 | `Invalid Access Token`                    |
| `ACCESS_TOKEN_EXPIRED ` | 4014 | `Expired Access Token `                   |
| `NOT_ACTIVE_ACCOUNT  `  | 4015 | `User Email Is Not Verified  `            |
| `ACCESS_DENIED  `       | 4031 | `Permission Denied To Requested Resource` |

#### Errors for 404 NOT FOUND

| Error Name                     | Code  | Message                                 |
|:-------------------------------|:------|:----------------------------------------|
| `CONFIRM_TOKEN_NOT_FOUND`      | 4041  | `Confirmation Token Not Found`          |
| `USER_NOT_FOUND`               | 4042  | `There Is No User With Such Id`         |
| `FAQ_QUESTION_NOT_FOUND`       | 4043  | `There Is No FAQ Question With Such Id` |
| `FAQ_CATEGORY_NOT_FOUND `      | 4044  | `There Is No FAQ Category With Such Id` |
| `IMAGE_NOT_FOUND  `            | 4045  | `There Is No Image For The Given User`  |
| `NEWS_ITEM_NOT_FOUND   `       | 4046  | `News Item Not Found`                   |
| `MIGRATION_HISTORY_NOT_FOUND`  | 4047  | `Migration History Not Found`           |
| `MIGRATION_METADATA_NOT_FOUND` | 4048  | `Migration Metadata Not Found`          |
| `PROPERTY_NOT_FOUND`           | 4049  | `Property Data Not Found`               |

#### Errors for 409 CONFLICT

| Error Name                                                    | Code | Message                                                                |
|:--------------------------------------------------------------|:-----|:-----------------------------------------------------------------------|
| `PROVIDED_SAME_PASSWORD`                                      | 4091 | `Provided The Same Password In Change Password Request`                |
| `USER_ALREADY_EXIST_WITH_EMAIL`                               | 4092 | `There Is a User Registered With Such Email`                           |
| `USER_ALREADY_EXIST_WITH_PHONE`                               | 4093 | `There Is a User Registered With Such Phone Number`                    |
| `FAQ_CATEGORY_ALREADY_EXIST `                                 | 4094 | `There Is a FAQ Category Created With Such Name`                       |
| `FAQ_QUESTION_ALREADY_EXIST  `                                | 4095 | `There Is a FAQ Question Created With Such Question Content`           |
| `CATEGORY_HAS_QUESTIONS   `                                   | 4096 | `The Category Has Questions, To Delete It First Delete It's Questions` |

#### Errors for 412 PRECONDITION FAILED

| Error Name                                                    | Code | Message                                                                                         |
|:--------------------------------------------------------------|:-----|:------------------------------------------------------------------------------------------------|
| `INVALID_IMAGE_FORMAT`                                        | 4121 | `Cannot Upload The Image`                                                                       |
| `INVALID_IMAGE_SIZE`                                          | 4122 | `There Is a User Registered With Such EmailImage Has Smaller Size Than It's Required (600x600)` |
| `INVALID_FILE_SIZE`                                           | 4123 | `File Size Should Be Between 70KB and 30MB`                                                     |
| `INVALID_IMAGE_EXTENSION `                                    | 4124 | `The Extension Of The Image Should Be Either 'jpg/jpeg' or 'png'`                               |

#### Errors for 500 INTERNAL SERVER ERROR

| Error Name                    | Code  | Message                                                   |
|:------------------------------|:------|:----------------------------------------------------------|
| `SEND_EMAIL_FAILED`           | 5001  | `Failed To Send An Email`                                 |
| `FAILED_MULTIPART_CONVERTING` | 5002  | `Error Occurred While Converting Multipart File To File`  |
| `FAILED_IMAGE_RESIZING`       | 5003  | `Error Occurred While Resizing The Image`                 |
| `FAILED_CSV_CONVERTING `      | 5004  | `Error Occurred While Converting Result To '.csv' Format` |
| `FAILED_IMAGE_CONVERTING `    | 5005  | `Error Occurred While Processing Downloaded Image`        |
| `BUCKET_NOT_FOUND `           | 5006  | `Cannot Find Bucket In AWS S3`                            |
| `FAILED_JSON_CONVERTING `     | 5007  | `Error Occurred While Parsing To Json`                    |
| `FAILED_KEY_READ `            | 5008  | `Error Occurred While Retrieving Security Keys`           |
| `FAILED_FILE_DELETION `       | 5009  | `Error Occurred While Deleting The File`                  |
| `FAILED_READ_FROM_JSON `      | 50010 | `Error Occurred While Reading From Json`                  |
| `FAILED_DATA_DOWNLOAD  `      | 50011 | `Error Occurred While Downloading The Data`               |
| `FAILED_INDEX_CREATION `      | 50012 | `Error Occurred While Creating AWS OpenSearch Index`      |
| `FAILED_BULK_DOCUMENT `       | 50013 | `Error Occurred While Adding Data To OpenSearch Index`    |
| `FAILED_SEARCH`               | 50014 | `Error Occurred While Performing Search In OpenSearch`    |