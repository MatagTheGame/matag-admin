package com.matag.admin.config;

import java.util.Map;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ConfigService {
  @Value("${matag.game.url}")
  private String matagGameUrl;

  @Value("${matag.admin.url}")
  private String matagAdminUrl;

  @Value("${matag.admin.password}")
  private String matagAdminPassword;

  public Map<String, String> getConfig() {
    return Map.of(
        "matagAdminUrl", matagAdminUrl,
        "matagGameUrl", matagGameUrl
    );
  }
}
