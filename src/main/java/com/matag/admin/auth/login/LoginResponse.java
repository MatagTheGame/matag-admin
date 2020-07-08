package com.matag.admin.auth.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.matag.admin.user.profile.CurrentUserProfileDto;

public record LoginResponse (
  @JsonProperty("token")String token,
  @JsonProperty("profile")CurrentUserProfileDto profile,
  @JsonProperty("error")String error) {}
