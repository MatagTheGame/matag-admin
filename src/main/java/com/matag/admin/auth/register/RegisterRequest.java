package com.matag.admin.auth.register;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = RegisterRequest.RegisterRequestBuilder.class)
@Builder
public class RegisterRequest {
  String email;
  String username;
  String password;

  @JsonPOJOBuilder(withPrefix = "")
  public static class RegisterRequestBuilder {

  }
}
