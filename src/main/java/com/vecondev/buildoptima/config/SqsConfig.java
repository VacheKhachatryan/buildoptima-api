package com.vecondev.buildoptima.config;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class SqsConfig {

  @Bean
  @Primary
  public AmazonSQSAsync amazonSqsAsync() {
    return AmazonSQSAsyncClientBuilder.defaultClient();
  }
}
