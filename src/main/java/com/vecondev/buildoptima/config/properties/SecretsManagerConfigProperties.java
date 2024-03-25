package com.vecondev.buildoptima.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "secrets-manager")
public class SecretsManagerConfigProperties {

  private String privateKeySecret;
}
