package com.matag.admin.auth.register;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = RegisterResponse.RegisterResponseBuilder.class)
@Builder(toBuilder = true)
public class RegisterResponse {
  private final String message;
  private final String error;

  @JsonPOJOBuilder(withPrefix = "")
  public static class RegisterResponseBuilder {

  }
}
