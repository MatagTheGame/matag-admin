package com.matag.admin.auth.login;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginRequest(
  @JsonProperty("emailOrUsername")String emailOrUsername,
  @JsonProperty("password")String password) {
}
