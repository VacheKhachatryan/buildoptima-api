# Filtering & Sorting for Architect Portal

## Overview

A general dynamic filtering approach is implemented for all Architect Portal entities like news,
songwriters, playlist etc. With this approach it will be possible to generate dynamic filtering with
different condition combinations (OR/AND)
.

## Example

Below is the sample structure of payload to be sent in search request:

```json
{
  "skip": 0,
  "take": 10,
  "sort": [
    {
      "field": "firstName",
      "order": "asc"
    }
  ],
  "filter": {
    "and": [
      {
        "operation": "eq",
        "name": "firstName",
        "value": "John"
      },
      {
        "or": [
          {
            "operation": "like",
            "name": "lastName",
            "value": "terson"
          },
          {
            "operation": "gt",
            "name": "creationDate",
            "value": "2018-11-30T18:35:24.00Z"
          }
        ]
      }
    ]
  }
}
```

## Explanation

The JSON payload can be empty (e.g {}) which will return all the result without sorting and
filtering. The `skip` and `take` fields must have numerical values (e.g., skip=0, take=10), and if
values aren't provided then will be 0 and 10 respectfully. The `sort`->`field` and `filter`->`name`
fields must consist only from the fields which can be provided per entity.

### Skip/Take

- `skip` - Specifying how many items result needs to skip (default 0), means offset from start
- `take` - Specifying how many items result needs to return starting give offset

### Sort

The `sort` object is a list of object with the following fields:

- `field` - The filed name result will be sorted
- `order` - The order of sorting, possible values are `asc` and `desc`

### Filter

Filer object can contain two type of objects: `or`/`and` (predicate) and combination of these
object. The predicate object has the following structure

- `operation` - The operator (e.g. `eq`,`gt`)
- `name` - The field name (e.g. `firstName`)
- `value` - Single value of filtering
- `values` - Multiple values of filtering (used with `in` operation)

### Filter Operations:

- `eq`       (result in case of exact matching)
- `like`     (result in case if contains any value like)
- `in`       (result if any value is in the scope of provided values)
- `gt`       (result if any value is greater than provided value)
- `ge`       (result if any value is greater than or equal to provided value)
- `lt`       (result if any value is less than provided value)
- `le`       (result if any value is less than or equal to provided value)

## Field Definitions for Architect Portal Entities

### User Entity

#### Filtering Fields

| Field       | Type     | Operations             | Notes                                                                      |
|:------------|:---------|:-----------------------|:---------------------------------------------------------------------------|
| `firstName` | String   | `eq`, `like`, `in`     | Must be provided with `value` field                                        |
| `lastName`  | String   | `eq`, `like`, `in`     | Must be provided with `value` field                                        |
| `email`     | String   | `eq`, `like`, `in`     | Must be provided with `value` field                                        |
| `phone`     | String   | `eq`, `like`, `in`     | Must be provided with `value` field                                        |
| `role`      | String   | `eq`                   | Must be provided with `value` field, possible values `CLIENT`, `ADMIN`     |
| `createdAt` | DateTime | `gt`, `ge`, `lt`, `le` | Must be provided in ISO format `yyyy-MM-dd'T'HH:mm'Z'`, with `value` field |
| `updatedAt` | DateTime | `gt`, `ge`, `lt`, `le` | Must be provided in ISO format `yyyy-MM-dd'T'HH:mm'Z'`, with `value` field |

#### Sorting Fields

- `firstName`
- `lastName`
- `email`
- `phone`
- `createdAt`
- `updatedAt`


### News Entity

#### Filtering Fields

| Field       | Type     | Operations             | Notes                                                                      |
|:------------|:---------|:-----------------------|:---------------------------------------------------------------------------|
| `title`     | String   | `eq`, `like`           | Must be provided with `value` field                                        |
| `category`  | String   | `eq`                   | Must be provided with `value` field                                        |
| `createdAt` | DateTime | `gt`, `ge`, `lt`, `le` | Must be provided in ISO format `yyyy-MM-dd'T'HH:mm'Z'`, with `value` field |
| `updatedAt` | DateTime | `gt`, `ge`, `lt`, `le` | Must be provided in ISO format `yyyy-MM-dd'T'HH:mm'Z'`, with `value` field |
| `createdBy` | String   | `eq`                   | Must be provided with `value` field (UUID userId)                          |
| `updatedBy` | String   | `eq`                   | Must be provided with `value` field (UUID userId)                          |


#### Sorting Fields

- `title`
- `category`
- `summary`
- `description`
- `createdAt`
- `updatedAt`
- `createdBy`
- `updatedBy`

### FAQ Category Entity

#### Filtering Fields

| Field        | Type      | Operations                         | Notes                                                                                                           |
|:-------------|:----------|:-----------------------------------|:----------------------------------------------------------------------------------------------------------------|
| `name`       | String    | `eq`, `like`, `in`                 | Must be provided with `value` field in case of `eq`,`like`, and with `values` for `in`                          |
| `createdAt`  | DateTime  | `eq`, `in`, `gt`, `ge`, `lt`, `le` | Must be provided with `value` field in case of `eq`,`like`, `gt`, `ge`, `lt`, `le`, and with `values` for `in`  |
| `updatedAt`  | DateTime  | `eq`, `in`, `gt`, `ge`, `lt`, `le` | Must be provided with `value` field in case of `eq`,`like`, `gt`, `ge`, `lt`, `le`, and with `values` for `in`  |
| `createdBy`  | UUID      | `eq` `in`                          | Must be provided with `value` field in case of `eq`, and with `values` for `in`                                 |
| `updatedBy`  | UUID      | `eq` `in`                          | Must be provided with `value` field in case of `eq`, and with `values` for `in`                                 |
#### Sorting Fields

- `name`
- `createdAt`
- `updatedAt`


### FAQ Question Entity

#### Filtering Fields

| Field       | Type     | Operations                         | Notes                                                                                                                                                 |
|:------------|:---------|:-----------------------------------|:------------------------------------------------------------------------------------------------------------------------------------------------------|
| `question`  | String   | `eq`, `like`, `in`                 | Must be provided with `value` field in case of `eq`,`like`, and with `values` for `in`                                                                |
| `answer`    | String   | `eq`, `like`, `in`                 | Must be provided with `value` field in case of `eq`,`like`, and with `values` for `in`                                                                |
| `status`    | String   | `eq`                               | Must be provided with `value` field, possible values `ACTIVE`, `ARCHIVED`                                                                             |
| `createdAt` | DateTime | `eq`, `in`, `gt`, `ge`, `lt`, `le` | Must be provided in ISO format `yyyy-MM-dd'T'HH:mm'Z'`, with `value` field in case of `eq`,`like`, `gt`, `ge`, `lt`, `le`, and with `values` for `in` |
| `updatedAt` | DateTime | `eq`, `in`, `gt`, `ge`, `lt`, `le` | Must be provided in ISO format `yyyy-MM-dd'T'HH:mm'Z'`, with `value` field in case of `eq`,`like`, `gt`, `ge`, `lt`, `le`, and with `values` for `in` |
| `createdBy` | UUID     | `eq` `in`                          | Must be provided with `value` field in case of `eq`, and with `values` for `in`                                                                       |
| `updatedBy` | UUID     | `eq` `in`                          | Must be provided with `value` field in case of `eq`, and with `values` for `in`                                                                       |
| `category`  | UUID     | `eq` `in`                          | Must be provided with `value` field in case of `eq`, and with `values` for `in`                                                                       |

#### Sorting Fields

- `question`
- `answer`
- `status`
- `createdAt`
- `updatedAt`

