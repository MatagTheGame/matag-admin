package com.matag.admin.game.join;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.matag.admin.game.game.GameType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = JoinGameRequest.JoinGameRequestBuilder.class)
@Builder(toBuilder = true)
public class JoinGameRequest {
  GameType gameType;
  String playerOptions;

  @JsonPOJOBuilder(withPrefix = "")
  public static class JoinGameRequestBuilder {

  }
}
