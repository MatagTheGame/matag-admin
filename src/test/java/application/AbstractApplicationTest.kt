package application

import application.AbstractApplicationTest.ApplicationTestConfiguration
import com.fasterxml.jackson.databind.ObjectMapper
import com.matag.admin.MatagAdminApplication
import com.matag.admin.config.ConfigService
import com.matag.admin.game.game.GameRepository
import com.matag.admin.game.score.ScoreRepository
import com.matag.admin.game.score.ScoreService
import com.matag.admin.game.session.GameSessionRepository
import com.matag.admin.session.AuthSessionFilter
import com.matag.admin.session.AuthSessionFilter.Companion.ADMIN_NAME
import com.matag.admin.session.AuthSessionFilter.Companion.SESSION_NAME
import com.matag.admin.session.MatagSession
import com.matag.admin.session.MatagSessionRepository
import com.matag.admin.user.MatagUser
import com.matag.admin.user.MatagUserRepository
import com.matag.admin.user.MatagUserType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.client.EntityExchangeResult
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [MatagAdminApplication::class], webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureRestTestClient
@Import(ApplicationTestConfiguration::class)
@ActiveProfiles("test")
abstract class AbstractApplicationTest {
    @Autowired protected lateinit var clock: Clock
    @Autowired protected lateinit var restTestClient: RestTestClient
    @Autowired protected lateinit var objectMapper: ObjectMapper
    @Autowired protected lateinit var matagUserRepository: MatagUserRepository
    @Autowired protected lateinit var matagSessionRepository: MatagSessionRepository
    @Autowired protected lateinit var gameRepository: GameRepository
    @Autowired protected lateinit var gameSessionRepository: GameSessionRepository
    @Autowired protected lateinit var scoreRepository: ScoreRepository
    @Autowired protected lateinit var scoreService: ScoreService
    @Autowired protected lateinit var configService: ConfigService

    @BeforeEach
    fun setup() {
        setCurrentTime(TEST_START_TIME)

        saveUser(TestUtils.guest())
        saveUser(TestUtils.user1())
        saveUser(TestUtils.user2())
        saveUser(TestUtils.inactive())
    }

    @AfterEach
    fun cleanup() {
        gameSessionRepository.deleteAll()
        gameRepository.deleteAll()
        matagSessionRepository.deleteAll()
        scoreRepository.deleteAll()
        matagUserRepository.deleteAll()
    }

    fun setCurrentTime(currentTime: LocalDateTime) =
        (clock as MockClock).setCurrentTime(currentTime.toInstant(ZoneOffset.UTC))

    fun loadUser(username: String) =
        matagUserRepository.findByUsername(username).orElseThrow()

    fun loginUser(userToken: String, username: String) {
        val sessionId = UUID.fromString(userToken).toString()
        if (!matagSessionRepository.existsBySessionId(sessionId)) {
            matagSessionRepository.save(
                MatagSession(
                    sessionId = sessionId,
                    matagUser = loadUser(username),
                    createdAt = LocalDateTime.now(clock),
                    validUntil = LocalDateTime.now(clock).plusSeconds(AuthSessionFilter.SESSION_DURATION_TIME.toLong())
                )
            )
        }
    }

    protected fun <T : Any> getForEntity(uri: String, responseType: Class<T>, token: String? = null) : EntityExchangeResult<T> =
        exchangeForEntity(HttpMethod.GET, uri, "", responseType, token)

    protected fun <T : Any> getForObject(uri: String, responseType: Class<T>, token: String? = null) : T =
        exchangeForEntity(HttpMethod.GET, uri, "", String::class.java, token).let {
            objectMapper.readValue(it.responseBody, responseType)
        }

    protected fun <T : Any> postForEntity(uri: String, request: Any, responseType: Class<T>, token: String? = null): EntityExchangeResult<T> =
        exchangeForEntity(HttpMethod.POST, uri, request, responseType, token)

    protected fun delete(uri: String, token: String): EntityExchangeResult<String> =
        exchangeForEntity(HttpMethod.DELETE, uri, "", String::class.java, token)

    protected fun postForAdmin(uri: String, request: Any): EntityExchangeResult<Any> {
        return restTestClientWithAdminToken()
            .post()
            .uri(uri)
            .body(request)
            .exchange()
            .expectBody<Any>()
            .returnResult()
    }

    private fun saveUser(inactive: MatagUser) {
        val user = matagUserRepository.save(inactive)
        if (user.type == MatagUserType.USER) {
            scoreService.createStartingScore(user)
        }
    }

    private fun <T : Any> exchangeForEntity(method: HttpMethod, uri: String, request: Any, responseType: Class<T>, token: String? = null) =
        restTestClientWithToken(token).mutate()
            .build()
            .method(method)
            .uri(uri)
            .body(request)
            .exchange()
            .expectBody(responseType)
            .returnResult()

    private fun restTestClientWithToken(token: String?) =
        if (token == null) {
            restTestClient
        } else {
            restTestClient.mutate().defaultHeader(SESSION_NAME, token).build()
        }

    private fun restTestClientWithAdminToken() =
        restTestClient.mutate().defaultHeader(ADMIN_NAME, configService.matagAdminPassword).build()

    @Configuration
    open class ApplicationTestConfiguration {
        @Bean
        @Primary
        open fun clock(): Clock {
            return MockClock()
        }

        @get:Primary
        @get:Bean
        open val javaMailSender: JavaMailSender?
            get() = Mockito.mock<JavaMailSender?>(JavaMailSender::class.java)
    }

    companion object {
        val TEST_START_TIME = LocalDateTime.parse("2020-01-01T00:00:00")
    }
}
