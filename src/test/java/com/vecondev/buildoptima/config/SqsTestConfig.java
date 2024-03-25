package com.vecondev.buildoptima.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "config.aws.sqs")
public class SqsTestConfig {

  private String url;
  private String region;
  private String accessKey;
  private String secretKey;
  private String propertyQueueName;

  @Bean
  @Profile("test")
  AmazonSQSAsync amazonSqsAsyncTest() {
    AmazonSQSAsync amazonSqsAsync =
        AmazonSQSAsyncClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(url, region))
            .withCredentials(
                new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
            .build();
    amazonSqsAsync.createQueue(propertyQueueName);

    return amazonSqsAsync;
  }
}
