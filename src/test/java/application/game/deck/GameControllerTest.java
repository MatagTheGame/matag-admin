package application.game.deck;

import application.AbstractApplicationTest;
import com.matag.admin.game.join.JoinGameRequest;
import com.matag.admin.game.join.JoinGameResponse;
import com.matag.adminentities.DeckInfo;
import org.junit.Test;

import java.util.Set;

import static application.TestUtils.USER_1_SESSION_TOKEN;
import static application.TestUtils.USER_1_USERNAME;
import static com.matag.admin.game.game.GameType.UNLIMITED;
import static com.matag.cards.properties.Color.RED;
import static com.matag.cards.properties.Color.WHITE;
import static org.assertj.core.api.Assertions.assertThat;

public class GameControllerTest extends AbstractApplicationTest {
  @Test
  public void shouldRetrieveGameInfo() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var request = new JoinGameRequest(UNLIMITED, "{\"randomColors\": [\"WHITE\", \"RED\"]}}");

    restTemplate.postForObject("/game", request, JoinGameResponse.class);

    // When
    var deckInfo = restTemplate.getForObject("/game/active-deck", DeckInfo.class);

    // Then
    assertThat(deckInfo.getRandomColors()).isEqualTo(Set.of(WHITE, RED));
  }
}