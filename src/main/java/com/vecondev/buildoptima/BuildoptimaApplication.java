package com.vecondev.buildoptima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableSqs
@EnableScheduling
@EnableJpaRepositories
@EnableTransactionManagement
@EnableConfigurationProperties
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class BuildoptimaApplication {

  public static void main(String[] args) {
    SpringApplication.run(BuildoptimaApplication.class, args);
  }
}
