package com.vecondev.buildoptima.config.properties;

import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Component
@Validated
@ConfigurationProperties(prefix = "page")
public class FilterConfigProperties {

  @NotNull private int skip;
  @NotNull private int take;
}
