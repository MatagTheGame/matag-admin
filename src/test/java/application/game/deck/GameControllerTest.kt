package application.game.deck

import application.AbstractApplicationTest
import application.TestUtils.USER_1_SESSION_TOKEN
import application.TestUtils.USER_1_USERNAME
import com.matag.admin.game.deck.ALL_SETS
import com.matag.admin.game.deck.DeckInfo
import com.matag.admin.game.deck.DeckMetadata
import com.matag.admin.game.deck.DeckMetadataOptions
import com.matag.admin.game.game.GameType
import com.matag.admin.game.join.JoinGameRequest
import com.matag.admin.game.join.JoinGameResponse
import com.matag.cards.properties.Color
import com.matag.cards.properties.Type
import com.matag.cards.sets.MtgSets
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.TestRestTemplate

class GameControllerTest(
    @param:Autowired val mtgSets: MtgSets
) : AbstractApplicationTest() {

    @Test
    fun `should retrieve game info for a random deck of 2 colors for all sets`() {
        // Given
        loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME)

        val request = JoinGameRequest(
            GameType.UNLIMITED,
            objectMapper.writeValueAsString(
                DeckMetadata(
                    "random",
                    DeckMetadataOptions(setOf(ALL_SETS), setOf(Color.WHITE, Color.RED))
                )
            )
        )

        postForEntity("/game", request, JoinGameResponse::class.java, USER_1_SESSION_TOKEN)

        // When
        val deckInfo = getForObject("/game/active-deck", DeckInfo::class.java, USER_1_SESSION_TOKEN)

        // Then
        assertThat(deckInfo.cards).hasSize(60)
        assertThat(deckInfo.cards.filter { it.types.contains(Type.LAND) }).hasSize(24)
        assertThat(deckInfo.cards.filter { it.colors.contains(Color.BLUE) }).isEmpty()
        assertThat(deckInfo.cards.filter { it.colors.contains(Color.BLACK) }).isEmpty()
        assertThat(deckInfo.cards.filter { it.colors.contains(Color.GREEN) }).isEmpty()
        assertThat(deckInfo.cards.filter { it.types.contains(Type.CREATURE) }).hasSizeGreaterThanOrEqualTo(20)
    }

    @Test
    fun `should retrieve game info for a random deck of 2 sets`() {
        // Given
        loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME)

        val request = JoinGameRequest(
            GameType.UNLIMITED,
            objectMapper.writeValueAsString(
                DeckMetadata(
                    "random",
                    DeckMetadataOptions(setOf("M19", "M20"), setOf(Color.WHITE, Color.RED))
                )
            )
        )

        postForEntity("/game", request, JoinGameResponse::class.java, USER_1_SESSION_TOKEN)

        // When
        val deckInfo = getForObject("/game/active-deck", DeckInfo::class.java, USER_1_SESSION_TOKEN)

        // Then
        val m19Cards = deckInfo.cards.filter { mtgSets.get("M19").cards.contains(it.name) }
        val m20Cards = deckInfo.cards.filter { mtgSets.get("M20").cards.contains(it.name) }

        assertThat(m19Cards).hasSizeGreaterThan(1)
        assertThat(m20Cards).hasSizeGreaterThan(1)
        assertThat(m19Cards + m20Cards).hasSizeBetween(40, 60) // Only few cards are implemented... sometime a deck cannot be fulfilled.
    }
}