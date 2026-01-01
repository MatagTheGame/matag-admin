package com.matag.admin.config;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/config")
@AllArgsConstructor
public class ConfigController {
  private final ConfigService configService;

  @PreAuthorize("permitAll()")
  @GetMapping()
  public Map<String, String> config() {
    return configService.getConfig();
  }
}
