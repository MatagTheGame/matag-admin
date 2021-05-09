package com.matag.admin.stats;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = StatsResponse.StatsResponseBuilder.class)
@Builder
public class StatsResponse {
  long totalUsers;
  List<String> onlineUsers;
  int totalCards;
  int totalSets;

  @JsonPOJOBuilder(withPrefix = "")
  public static class StatsResponseBuilder {

  }
}
