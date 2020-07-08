package com.matag.admin.auth.register;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterRequest(
  @JsonProperty("email")String email,
  @JsonProperty("username")String username,
  @JsonProperty("password")String password) {
}
