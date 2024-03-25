FROM maven:3.8.5-eclipse-temurin-17
EXPOSE 80
ARG AWS_ACCESS_KEY_ID
ENV AWS_ACCESS_KEY_ID $AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY
ENV AWS_SECRET_ACCESS_KEY $AWS_SECRET_ACCESS_KEY
ARG AWS_DEFAULT_REGION
ENV AWS_DEFAULT_REGION $AWS_DEFAULT_REGION
WORKDIR /usr/src/app
COPY . /usr/src/app
RUN mvn clean install -DskipTests
CMD ["java", "-jar", "target/buildoptima-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]