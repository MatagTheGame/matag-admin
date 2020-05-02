package com.matag.admin.cleanup;

import com.matag.admin.session.MatagSessionRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class MatagSessionsCleanup {
  private static final Logger LOGGER = LoggerFactory.getLogger(MatagSessionsCleanup.class);

  private final MatagSessionRepository matagSessionRepository;
  private final Clock clock;

  @Transactional
  public void cleanup() {
    int deletedRows = matagSessionRepository.deleteValidUntilBefore(LocalDateTime.now(clock));
    LOGGER.info("MatagSessionsCleanup deleted " + deletedRows + " rows.");
  }
}
