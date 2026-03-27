package application.game.player

import application.AbstractApplicationTest
import application.TestUtils.GUEST_SESSION_TOKEN_1
import application.TestUtils.GUEST_USERNAME
import application.TestUtils.USER_1_SESSION_TOKEN
import application.TestUtils.USER_1_USERNAME
import com.matag.admin.game.player.PlayerInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PlayerInfoRetrieverControllerTest : AbstractApplicationTest() {
    @Test
    fun `should retrieve player info for player`() {
        // Given
        loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME)

        // When
        val response = getForObject("/player/info", PlayerInfo::class.java, USER_1_SESSION_TOKEN)

        // Then
        assertThat(response).isEqualTo(PlayerInfo(playerName = USER_1_USERNAME))
    }

    @Test
    fun `should retrieve player info for guest`() {
        // Given
        loginUser(GUEST_SESSION_TOKEN_1, GUEST_USERNAME)

        // When
        val response = getForObject("/player/info", PlayerInfo::class.java, GUEST_SESSION_TOKEN_1)

        // Then
        assertThat(response.playerName).startsWith(GUEST_USERNAME)
    }
}