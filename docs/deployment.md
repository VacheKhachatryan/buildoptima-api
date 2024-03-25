# Deployment 

The application deployment orchestrated by `AWS EC2`, using  `GitHub Actions`.

## Get Started
In order to start the application deployment the following prerequisites required:

1. Amazon Elastic Compute Cloud [AWS EC2](https://console.aws.amazon.com/ec2)
2. Amazon Elastic Container Service [AWS ECR](https://console.aws.amazon.com/ecr/repositories)   
3. Amazon Command Line Interface [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide)

## How to Deploy
For application deployment on EC2 the following steps required:

1. Set secrets and environments variables in GitHub settings
2. Build executable `jar` file by spring-boot maven plugin
3. Build docker image with embedded `jar` file
4. Push docker image to ECR and tag with latest also removing all previous tags
5. Copy [docker-compose.yml](./docker/docker-compose.yml) file to EC2 instance
6. Run the docker file inside the EC2 server

### Secrets and Environments Variables

Secrets that needs to be set in GitHub repository:
 - AWS_ACCESS_KEY_ID
 - AWS_SECRET_ACCESS_KEY
 - EC2_PRIVATE_KEY 
 - EC2_USER 
 - EC2_IP_ADDRESS

Environments variables set in GitHub Actions:
 - AWS_ECR_REGISTRY: the ECR registry where should be created project images (e.g. 997658595201.dkr.ecr.us-east-1.amazonaws.com )
 - AWS_ECR_REPOSITORY: the ECR repository name (e.g `buildoptima`)`

### Build JAR
To build an executable jar run the following maven command:

```bash
mvn clean package -DskipTests
```

This command will skip the tests and will generate jar file in `target` directory (e.g `buildoptima-0.0.1-SNAPSHOT.jar`).

### Build Docker
In the root directory create a new `Dockerfile`:

```dockerfile
FROM maven:3.8.5-eclipse-temurin-17
EXPOSE 443
WORKDIR /usr/src/app
COPY . /usr/src/app
RUN mvn clean install -DskipTests
CMD ["java", "-jar", "target/buildoptima-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
```

Build docker image with arguments:
```bash
docker build -t $AWS_ECR_REGISTRY/$AWS_ECR_REPOSITORY .
```

Push docker image to ECR:
```bash
docker push $AWS_ECR_REGISTRY/$AWS_ECR_REPOSITORY
```

#### Deployment

1. Create working directory in EC2 server `/home/ubuntu/buildoptima/data`
2. Copy `docker-compose.yml` file in EC2 server
3. Install `docker` if not exists in EC2
4. Install `docker-compose` if not exists in EC2
5. Install and configure AWS CLI if not exists in EC2 with your AWS access and secret keys that have access to project ECR repository
6. Stop all containers that are running from the previous image if it exists in EC2
7. Delete the previous image
8. Run `docker-compose.yml` file



