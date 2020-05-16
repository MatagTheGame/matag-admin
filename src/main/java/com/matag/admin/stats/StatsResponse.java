package com.matag.admin.stats;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = StatsResponse.StatsResponseBuilder.class)
@Builder(toBuilder = true)
public class StatsResponse {
  long totalUsers;
  long onlineUsers;
  int totalCards;
  int totalSets;

  @JsonPOJOBuilder(withPrefix = "")
  public static class StatsResponseBuilder {

  }
}
