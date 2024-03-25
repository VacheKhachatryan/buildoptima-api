package com.vecondev.buildoptima.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sqs")
public class SqsConfigProperties {

  private String mailQueueUrl;
}