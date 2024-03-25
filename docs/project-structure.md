# Project Structure

## Project Folders

| Path                 | Description                  |
|----------------------|------------------------------|
| `.github/            | Github CI/CD actions         |
| `docker/`            | Docker-compose files         |
| `docs/`              | Project documentation folder |
| `src/main/java`      | Sources                      |
| `src/main/resources` | Configurations/Resources     |
| `src/test/java`      | Tests                        |
| `src/test/resources` | Test Resources               |

## Service and Configuration

| Path         | Description                                    |
|--------------| ---------------------------------------------- |
| `.github/`   | GitHub settings and GitHub Actions definitions |
| `.gitignore` | Git ignore file                                |
| `pom.xml`    | Maven pom file for project                     |


## Project Infrastructure

```..  code::
.
|-- com
|   |-- wcm
|   |   |-- buildoptima
|   |   |   | -- api
|   |   |   |   |   MyObjectApi
|   |   |   |   |-- controller
|   |   |   |   |   |   MyObjectController
|   |   |   |   |   |   |-- MyObjectController
|   |   |   | -- config
|   |   |   |   |   MyObjectConfig
|   |   |   |   |-- properties
|   |   |   |   |   |   MyObjectConfigProperties
|   |   |   | -- csv
|   |   |   |   |   CsvRecord
|   |   |   |   | -- myobject
    |   |   |   |   |   MyObjectRecord 
|   |   |   | -- dto
|   |   |   |   |   EntityOverview
|   |   |   |   | -- myobject
|   |   |   |   |   | -- response
|   |   |   |   |   |   |   MyObjectResponseDto
|   |   |   |   |   | -- request
|   |   |   |   |   |   |   MyObjectRequestDto
|   |   |   | -- exception
|   |   |   |   |   MyObjectNotFoundException
|   |   |   | -- filter
|   |   |   |   | -- converter
|   |   |   |   |   |   UUIDConverter
|   |   |   |   | -- model
|   |   |   |   |   |   MyObjectFields
|   |   |   |   | -- specification
|   |   |   |   |   |   GenericSpecification   
|   |   |   | -- manager
|   |   |   |   |   JwtTokenManager
|   |   |   | -- mapper
|   |   |   |   | -- myobject
|   |   |   |   |   |   MyObjectMapper
|   |   |   |   |   | -- decorator
|   |   |   |   |   |   |   MyObjectMapperDecorator
|   |   |   | -- model
|   |   |   |   |   AbstractEntity
|   |   |   |   |   | -- myobject
|   |   |   |   |   |   |   MyObjectStatus
|   |   |   | -- repository
|   |   |   |   |   -- myobject
|   |   |   |   |   |   MyObjectAddressRepsository
|   |   |   | -- security
|   |   |   |   |   RestAuthorizationFilter
|   |   |   |   | -- user
|   |   |   |   |   |   AppUserDetails
|   |   |   | -- service
|   |   |   |   | -- myobject
|   |   |   |   |   |   MyObjectService
|   |   |   |   |   | -- impl
|   |   |   |   |   |   |   MyObjectServiceImpl
|   |   |   | -- util
|   |   |   |   |   FileUtil
|   |   |   | -- validation
|   |   |   |   |   MyObjectValidator
|   |   |   |   | -- constraint
|   |   |   |   |   |   MyConstraint
|   |   |   |   | -- validator
|   |   |   |   |   |   MyConstraintValidator
```


| Package      | Description                                                                         |
|--------------|-------------------------------------------------------------------------------------|
| `api`        | RestControllers, Api documentations                                                 |
| `config`     | All configurations related to project                                               |
| `csv`        | CSV records                                                                         |
| `dto`        | Request and response object transferred over API, ex: DTO                           |
| `exception`  | Exceptions that are thrown across all project, controller advices                   |
| `filter`     | All classes that are being used to filter through different entities of the project |
| `entity`     | External data representation objects, ex: persistent objects                        |
| `manager`    | Helper classes  that helps to deal with e.g security certificates, jwt tokens       |
| `mapper`     | Mapstruct mapper components                                                         |
| `repository` | Data access components                                                              |
| `security`   | Authentication based on request header                                              |
| `service`    | All business logic                                                                  |
| `util`       | Utility classes                                                                     |
| `validation` | Custom validation constraints and validator classes for models                      |



## Temporary objects, ignored in sources

| Path                                                                                                                                           | Description                                  |
|------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------|
| `.idea, .idea, *.ipr, *.iml, *.iws, *.ipr, */target, /target, .run`                                                                            | IntelliJ dependencies folders and files      |
| `nb-configuration.xml, /nbproject/private/, /nbbuild/, /dist/, /nbdist/, /.nb-gradle/, build/, !**/src/main/**/build/, !**/src/test/**/build/` | NetBeans dependencies folders and files      |
| `.project, .classpath, .settings/, *.launch, bin/`                                                                                             | Eclipse dependencies folders and files       |
| `.project, .classpath, .settings/, *.launch, bin/`                                                                                             | Visual Studio dependencies folders and files |
| `.DS_Store`                                                                                                                                    | OSX                                          |
| `*.swp, *.swo`                                                                                                                                 | Vim                                          |
| `*.orig, *.rej`                                                                                                                                | patch                                        |
| `.apt_generated,.springBeans, .sts4-cache`                                                                                                     | STS                                          |