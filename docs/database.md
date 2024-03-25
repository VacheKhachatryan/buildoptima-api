# Database Setup, Config and Migration 

The application uses:
- PostgreSQL database 14 on both locale and production environments

## Database Config

The following datasource and jpa is used for database configuration.
```yml
spring:
   # Datasource config
   datasource:
      username: postgres
      password: root
      url: jdbc:postgresql://localhost:5432/buildoptima?currentSchema=buildoptima

   # JPA config
   jpa:
      hibernate:
         ddl-auto: validate
      properties:
         hibernate:
            show_sql: true
            dialect: org.hibernate.dialect.PostgreSQLDialect
```

Although environment differences we have one configuration for both of them. Using SPEL (Spring Expression Language) allow us to use external configuration(for production and stage environments)  with default values (for locale environment).

## Database Setup on Local Environment.

For setup and running PostgreSQL database on your local machine, please follow steps.

1. Install PostgreSQL 14.2 and pgAdmin
2. Set password for database default user(`postgres`) as `root`
3. Create database `buildoptima`
4. Go to `buildoptima` database
5. Create schema `buildoptima`

### Database Migration

For database migration we are using Flyway database migration tool.
The flyway use versioned sql scripts. The sql scripts are located on [folder](../src/main/resources/db/migration).
All sql script filenames has common pattern:

- V[version.major version.minor version]__[file description].sql

> For make changes on database create a sql file with name using greater version (can be major or minor version).