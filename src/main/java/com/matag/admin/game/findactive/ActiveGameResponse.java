package com.matag.admin.game.findactive;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = ActiveGameResponse.ActiveGameResponseBuilder.class)
@Builder
public class ActiveGameResponse {
  Long gameId;
  LocalDateTime createdAt;
  String playerName;
  String playerOptions;
  String opponentName;
  String opponentOptions;

  @JsonPOJOBuilder(withPrefix = "")
  public static class ActiveGameResponseBuilder {

  }
}
