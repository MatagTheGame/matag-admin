package com.matag.admin.auth.login;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.matag.admin.user.profile.CurrentUserProfileDto;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = LoginResponse.LoginResponseBuilder.class)
@Builder
public class LoginResponse {
  String token;
  CurrentUserProfileDto profile;
  String error;

  @JsonPOJOBuilder(withPrefix = "")
  public static class LoginResponseBuilder {

  }
}
