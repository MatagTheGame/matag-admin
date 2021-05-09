package com.matag.admin.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class ConfigService {
  @Value("${matag.name}")
  private String matagName;

  @Value("${matag.game.url}")
  private String matagGameUrl;

  @Value("${matag.admin.url}")
  private String matagAdminUrl;

  @Value("${matag.admin.password}")
  private String matagAdminPassword;

  @Value("${matag.email.username}")
  private String matagSupportEmail;

  public Map<String, String> getConfig() {
    return Map.of(
      "matagName", matagName,
      "matagAdminUrl", matagAdminUrl,
      "matagGameUrl", matagGameUrl,
      "matagSupportEmail", matagSupportEmail
    );
  }
}
