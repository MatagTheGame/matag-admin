package com.matag.admin.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConfigService {
  @Value("${matag.name}")
  private String matagName;

  @Value("${matag.game.path}")
  private String matagGamePath;

  @Value("${matag.admin.path}")
  private String matagAdminPath;

  @Value("${matag.admin.externalUrl}")
  private String matagExternalUrl;

  @Value("${matag.admin.password}")
  private String matagAdminPassword;

  @Value("${matag.email.username}")
  private String matagSupportEmail;

  public Map<String, String> getConfig() {
    return Map.of(
      "matagName", matagName,
      "matagAdminUrl", matagAdminPath,
      "matagGameUrl", matagGamePath,
      "matagSupportEmail", matagSupportEmail
    );
  }

  public String getMatagName() {
    return matagName;
  }

  public String getMatagExternalUrl() {
    return matagExternalUrl;
  }

  public String getMatagGamePath() {
    return matagGamePath;
  }

  public String getMatagAdminPath() {
    return matagAdminPath;
  }

  public String getMatagAdminPassword() {
    return matagAdminPassword;
  }

  public String getMatagSupportEmail() {
    return matagSupportEmail;
  }
}
