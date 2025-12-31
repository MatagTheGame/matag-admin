package application.game.deck

import application.AbstractApplicationTest
import application.TestUtils
import com.fasterxml.jackson.databind.ObjectMapper
import com.matag.admin.game.deck.DeckMetadata
import com.matag.admin.game.deck.DeckMetadataOptions
import com.matag.admin.game.game.GameType
import com.matag.admin.game.join.JoinGameRequest
import com.matag.admin.game.join.JoinGameResponse
import com.matag.adminentities.DeckInfo
import com.matag.cards.Card
import com.matag.cards.properties.Color
import lombok.SneakyThrows
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import java.util.Set

class GameControllerTest : AbstractApplicationTest() {
    @Autowired
    private val objectMapper: ObjectMapper? = null

    @SneakyThrows
    @Test
    fun shouldRetrieveGameInfo() {
        // Given
        loginUser(TestUtils.USER_1_SESSION_TOKEN, TestUtils.USER_1_USERNAME)
        val metadataOptions = DeckMetadataOptions.builder().colors(Set.of<Color?>(Color.WHITE, Color.RED)).build()
        val metadata = DeckMetadata.builder().type("random").options(metadataOptions).build()
        val request = JoinGameRequest.builder()
            .gameType(GameType.UNLIMITED)
            .playerOptions(objectMapper!!.writeValueAsString(metadata))
            .build()

        postForEntity("/game", request, JoinGameResponse::class.java, TestUtils.USER_1_SESSION_TOKEN)

        // When
        val deckInfo =
            getForEntity("/game/active-deck", DeckInfo::class.java, TestUtils.USER_1_SESSION_TOKEN)

        // Then
        Assertions.assertThat<HttpStatusCode?>(deckInfo.getStatus()).isEqualTo(HttpStatus.OK)
        Assertions.assertThat<Card?>(deckInfo.getResponseBody()!!.getCards()).hasSize(60)
    }
}