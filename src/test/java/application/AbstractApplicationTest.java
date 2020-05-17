package application;

import com.matag.admin.MatagAdminApplication;
import com.matag.admin.config.ConfigService;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.admin.session.MatagSession;
import com.matag.admin.session.MatagSessionRepository;
import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.UUID;

import static application.TestUtils.*;
import static com.matag.admin.session.AuthSessionFilter.SESSION_DURATION_TIME;
import static com.matag.admin.session.AuthSessionFilter.SESSION_NAME;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MatagAdminApplication.class, webEnvironment = RANDOM_PORT)
@Import(AbstractApplicationTest.ApplicationTestConfiguration.class)
@ActiveProfiles("test")
public abstract class AbstractApplicationTest {
  public static final LocalDateTime TEST_START_TIME = LocalDateTime.parse("2020-01-01T00:00:00");

  @Autowired
  private MatagUserRepository matagUserRepository;

  @Autowired
  private MatagSessionRepository matagSessionRepository;

  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private GameSessionRepository gameSessionRepository;

  @Autowired
  protected Clock clock;

  @Autowired
  protected TestRestTemplate restTemplate;

  @Autowired
  private ConfigService configService;

  @Before
  public void setup() {
    setCurrentTime(TEST_START_TIME);

    matagUserRepository.save(user1());
    matagUserRepository.save(user2());
    matagUserRepository.save(inactive());
  }

  @After
  public void cleanup() {
    restTemplate.getRestTemplate().getInterceptors().clear();

    gameSessionRepository.deleteAll();
    gameRepository.deleteAll();
    matagSessionRepository.deleteAll();
    matagUserRepository.deleteAll();
  }

  public void setCurrentTime(LocalDateTime currentTime) {
    ((MockClock) clock).setCurrentTime(currentTime.toInstant(ZoneOffset.UTC));
  }

  public MatagUser loadUser(String username) {
    return matagUserRepository.findByUsername(username).orElseThrow(RuntimeException::new);
  }

  public void loginUser(String userToken, String username) {
    String sessionId = UUID.fromString(userToken).toString();
    if (!matagSessionRepository.existsBySessionId(sessionId)) {

      matagSessionRepository.save(MatagSession.builder()
        .sessionId(sessionId)
        .matagUser(loadUser(username))
        .createdAt(LocalDateTime.now(clock))
        .validUntil(LocalDateTime.now(clock).plusSeconds(SESSION_DURATION_TIME))
        .build());
      System.out.println("save session");
    }
  }

  public void userIsLoggedIn(String userToken, String username) {
    loginUser(userToken, username);
    restTemplate.getRestTemplate().setInterceptors(
      Collections.singletonList((request, body, execution) -> {
        request.getHeaders().add(SESSION_NAME, userToken);
        return execution.execute(request, body);
      }));
  }

  public void adminAuthentication() {
    restTemplate.getRestTemplate().setInterceptors(
      Collections.singletonList((request, body, execution) -> {
        request.getHeaders().add("admin", configService.getMatagAdminPassword());
        return execution.execute(request, body);
      }));
  }

  @Configuration
  public static class ApplicationTestConfiguration {
    @Bean
    @Primary
    public Clock clock() {
      return new MockClock();
    }

    @Bean
    @Primary
    public JavaMailSender getJavaMailSender() {
      return Mockito.mock(JavaMailSender.class);
    }
  }
}
