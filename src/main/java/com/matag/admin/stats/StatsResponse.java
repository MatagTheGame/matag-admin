package com.matag.admin.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsResponse {
  long totalUsers;
  List<String> onlineUsers;
  int totalCards;
  int totalSets;
}
