# Code Style Agreement

As code style agreement we are using Google's coding standards for source code in the Java™ Programming Language.
Like other programming style guides, the issues covered span not only aesthetic issues of formatting, but other types of conventions or coding standards as well.
For more details about check style plugin please visit the following [link](https://maven.apache.org/plugins/maven-checkstyle-plugin/usage.html).

## Setup Code Style
The apache maven `checkstyle` plugin is used for code styling:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.1.2</version>
    <configuration>
        <configLocation>google_checks.xml</configLocation>
        <encoding>UTF-8</encoding>
        <consoleOutput>true</consoleOutput>
        <failsOnError>true</failsOnError>
        <linkXRef>false</linkXRef>
    </configuration>
    <executions>
        <execution>
            <id>validate</id>
            <phase>validate</phase>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

It's also possible to add custom styles by providing configuration file defined styling convention:
```xml
<module name="Checker">
    <module name="EmptyLineSeparator">
        <property name="allowNoEmptyLineBetweenFields" value="true"/>
        <property name="allowMultipleEmptyLines" value="false"/>
    </module>
    <module name="Indentation">
        <property name="basicOffset" value="2"/>
    </module>
</module>
```

## Styling Convention
For detailed info about styling convention, visit the following [link](https://google.github.io/styleguide/javaguide.html)

## Configuring Styles in IntelliJ IDEA
Use [intellij-java-google-style.xml](intellij_java_google_style.xml) file to configure your IntelliJ IDEA.
Find detailed steps on how to configure [here](https://medium.com/swlh/configuring-google-style-guide-for-java-for-intellij-c727af4ef248)

## Naming Convention

All spring managed beans should be represented with the following naming convention

``` Spring Managed Component for selected layer  = ObjectName + ComponentName```

Where `ComponentName` is `Controller`, `Repository`, `Service`

In case if we have multiple representation of some components naming convention is the same.
We will differentiate beans by provided name. `@Repository("userDBRepository")` and `@Repository("userRestRepository")`
