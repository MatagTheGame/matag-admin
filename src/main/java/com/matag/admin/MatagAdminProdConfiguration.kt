package com.matag.admin;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Profile("!test")
@Configuration
@EnableScheduling
public class MatagAdminProdConfiguration {
  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }
}
