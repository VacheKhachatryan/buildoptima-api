# Configuration 

The configuration is main part of the application.
Configuration allow to set up main data to have independence on any environment.

## Application Configs

The [application.yml](../src/main/resources/application.yml) is general configuration. 
The application.yml provide settings for

1. Spring
    - application    - The application name
    - data           - The spring data web default pageable configuration
    - profiles       - The active profile
    - datasource     - The database connection settings
    - jpa            - Hibernate setting 
    - flyway         - The database migration setting
2. Server
   - port            - The server running port
   
3. Springdoc    
   - api-docs        - The openApi configuration settings
   - swagger-ui      - The Swagger UI main settings
   
> Please follow documentation structure when you will need to make changes on them.
> Please use ${ConfigPropertyName:ConfigPropertyDefaultValue} SPEL specification to allow make external configuration for property.

## Messages Bundle

> The all messages provided in English language (Locale.US) for the application.
The application's client messages are flexible and configurable. For support new languages we can add new messages_hy.properties.
The client messages are located [messages.properties](../src/main/resources/messages.properties) file.


## Flyway Configuration

> The flyway is database migration tool, that will allow application to have versioned database structure.

The main part of  Buildoptima application's database setup via flyway migration tool.
The flyway related configuration's queries are located on  [directory](../src/main/resources/db/migration/).

For more details please read [Application Database configuration and migration](../docs/database.md) doc.
