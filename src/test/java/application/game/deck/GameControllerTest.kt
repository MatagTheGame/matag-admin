package application.game.deck;

import application.AbstractApplicationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matag.admin.game.deck.DeckMetadata;
import com.matag.admin.game.deck.DeckMetadataOptions;
import com.matag.admin.game.join.JoinGameRequest;
import com.matag.admin.game.join.JoinGameResponse;
import com.matag.adminentities.DeckInfo;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static application.TestUtils.USER_1_SESSION_TOKEN;
import static application.TestUtils.USER_1_USERNAME;
import static com.matag.admin.game.game.GameType.UNLIMITED;
import static com.matag.cards.properties.Color.RED;
import static com.matag.cards.properties.Color.WHITE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class GameControllerTest extends AbstractApplicationTest {
  @Autowired
  private ObjectMapper objectMapper;

  @SneakyThrows
  @Test
  public void shouldRetrieveGameInfo() {
    // Given
    loginUser(USER_1_SESSION_TOKEN, USER_1_USERNAME);
    var metadataOptions = DeckMetadataOptions.builder().colors(Set.of(WHITE, RED)).build();
    var metadata = DeckMetadata.builder().type("random").options(metadataOptions).build();
    var request = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions(objectMapper.writeValueAsString(metadata))
      .build();

    postForEntity("/game", request, JoinGameResponse.class, USER_1_SESSION_TOKEN);

    // When
    var deckInfo = getForEntity("/game/active-deck", DeckInfo.class, USER_1_SESSION_TOKEN);

    // Then
    assertThat(deckInfo.getStatus()).isEqualTo(OK);
    assertThat(deckInfo.getResponseBody().getCards()).hasSize(60);
  }
}