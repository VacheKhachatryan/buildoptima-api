# Profiles

The profiles are main part of the application.
The profiles control application build and configuration.

## Application Profiles

The profiles defined on [pom.xml](../pom.xml) file under the `<profiles>` tag.

> The default profile is "defeault"

The application build processes executed via maven plugins.
The plugins provided for profiles are:

1. **default**

    - `spring-boot-maven-plugin` - The plugin will build spring boot application
    - `maven-compiler-plugin`    - The plugin will process lombok and mapstruct annotation.
    - `maven-surefire-plugin`    - The plugin generated surefire reports about run tests results. The generated reports will be located on (../target/surefire-reports/) directory.
   
2. **prod**

   - `spring-boot-maven-plugin` - The plugin will build spring boot application
   - `maven-compiler-plugin`    - The plugin will process lombok and mapstruct annotation.
   - `maven-surefire-plugin`    - The plugin generated surefire reports about run tests results. The generated reports will be located on (../target/surefire-reports/) directory.

> The plugins affected on database migration. 
> For more details please read [Application Database setup, configuration and migration](database.md) doc.