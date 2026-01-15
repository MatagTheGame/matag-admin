package com.matag.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MatagAdminWebConfiguration implements WebMvcConfigurer {
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/").setViewName("redirect:/ui");
    registry.addViewController("/ui").setViewName("forward:/admin.html");
    registry.addViewController("/ui/**").setViewName("forward:/admin.html");
  }
}
