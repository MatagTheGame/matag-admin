package com.matag.admin.stats;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.util.List;

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
