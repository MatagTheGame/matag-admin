package application;

import com.matag.admin.MatagAdminApplication;
import com.matag.admin.config.ConfigService;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.score.ScoreRepository;
import com.matag.admin.game.score.ScoreService;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.admin.session.MatagSession;
import com.matag.admin.session.MatagSessionRepository;
import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static application.TestUtils.*;
import static com.matag.admin.session.AuthSessionFilter.*;
import static com.matag.admin.user.MatagUserType.USER;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MatagAdminApplication.class, webEnvironment = RANDOM_PORT)
@AutoConfigureRestTestClient
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
    private ScoreRepository scoreRepository;
    @Autowired
    private ScoreService scoreService;

    @Autowired
    protected Clock clock;

    @Autowired
    protected RestTestClient restTestClient;

    @Autowired
    private ConfigService configService;

    @BeforeEach
    public void setup() {
        setCurrentTime(TEST_START_TIME);

        saveUser(guest());
        saveUser(user1());
        saveUser(user2());
        saveUser(inactive());
    }

    private void saveUser(MatagUser inactive) {
        var user = matagUserRepository.save(inactive);
        if (user.getType() == USER) {
            scoreService.createStartingScore(user);
        }
    }

    @AfterEach
    public void cleanup() {
        gameSessionRepository.deleteAll();
        gameRepository.deleteAll();
        matagSessionRepository.deleteAll();
        scoreRepository.deleteAll();
        matagUserRepository.deleteAll();
    }

    public void setCurrentTime(LocalDateTime currentTime) {
        ((MockClock) clock).setCurrentTime(currentTime.toInstant(ZoneOffset.UTC));
    }

    public MatagUser loadUser(String username) {
        return matagUserRepository.findByUsername(username).orElseThrow(RuntimeException::new);
    }

    public void loginUser(String userToken, String username) {
        var sessionId = UUID.fromString(userToken).toString();
        if (!matagSessionRepository.existsBySessionId(sessionId)) {
            matagSessionRepository.save(MatagSession.builder()
                    .sessionId(sessionId)
                    .matagUser(loadUser(username))
                    .createdAt(LocalDateTime.now(clock))
                    .validUntil(LocalDateTime.now(clock).plusSeconds(SESSION_DURATION_TIME))
                    .build());
        }
    }

    protected <T> EntityExchangeResult<T> postForEntity(String uri, Object request, Class<T> responseType, String token) {
        return restTestClientWithToken(token)
                .post()
                .uri(uri)
                .body(request)
                .exchange()
                .expectBody(responseType)
                .returnResult();
    }

    protected <T> EntityExchangeResult<T> postForEntity(String uri, Object request, Class<T> responseType) {
        return restTestClient
                .post()
                .uri(uri)
                .body(request)
                .exchange()
                .expectBody(responseType)
                .returnResult();
    }

    protected <T> EntityExchangeResult<T> getForEntity(String uri, Class<T> responseType) {
        return restTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectBody(responseType)
                .returnResult();
    }

    protected <T> EntityExchangeResult<T> getForEntity(String uri, Class<T> responseType, String token) {
        return restTestClientWithToken(token)
                .get()
                .uri(uri)
                .exchange()
                .expectBody(responseType)
                .returnResult();
    }

    protected EntityExchangeResult<String> delete(String uri, String token) {
        return restTestClientWithToken(token)
                .delete()
                .uri(uri)
                .exchange()
                .expectBody(String.class)
                .returnResult();
    }

    protected EntityExchangeResult<Object> postForAdmin(String uri, Object request) {
        return restTestClientWithAdminToken()
                .post()
                .uri(uri)
                .body(request)
                .exchange()
                .expectBody(Object.class)
                .returnResult();
    }

    private RestTestClient restTestClientWithToken(String token) {
        return restTestClient
                .mutate().defaultHeader(SESSION_NAME, token)
                .build();
    }

    private RestTestClient restTestClientWithAdminToken() {
        return restTestClient
                .mutate().defaultHeader(ADMIN_NAME, configService.getMatagAdminPassword())
                .build();
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
