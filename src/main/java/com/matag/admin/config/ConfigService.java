package com.matag.admin.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

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

  public String getMatagName() {
    return matagName;
  }

  public String getMatagGameUrl() {
    return matagGameUrl;
  }

  public String getMatagAdminUrl() {
    return matagAdminUrl;
  }

  public String getMatagAdminPassword() {
    return matagAdminPassword;
  }

  public String getMatagSupportEmail() {
    return matagSupportEmail;
  }
}
