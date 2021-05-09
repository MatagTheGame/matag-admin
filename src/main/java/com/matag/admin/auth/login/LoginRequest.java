package com.matag.admin.auth.login;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = LoginRequest.LoginRequestBuilder.class)
@Builder
public class LoginRequest {
  String emailOrUsername;
  String password;

  @JsonPOJOBuilder(withPrefix = "")
  public static class LoginRequestBuilder {

  }
}
