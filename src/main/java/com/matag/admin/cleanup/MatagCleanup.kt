package com.matag.admin.cleanup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class MatagCleanup {
  private static final Logger LOGGER = LoggerFactory.getLogger(MatagCleanup.class);

  private final MatagSessionsCleanup matagSessionsCleanup;
  private final MatagGameCleanup matagGameCleanup;

  @Scheduled(fixedRate = 6 * 60 * 60 * 1000, initialDelay = 10 * 60 * 1000)
  public void cleanup() {
    LOGGER.info("cleanup triggered.");

    matagSessionsCleanup.cleanup();
    matagGameCleanup.cleanup();
  }
}
