package com.vecondev.buildoptima.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aws.properties.s3")
public class S3ConfigProperties {

  private String imageBucketName;
  private String dataBucketName;
  private String webBucketName;
}
