package com.matag.admin.auth.register;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = VerifyResponse.VerifyResponseBuilder.class)
@Builder(toBuilder = true)
public class VerifyResponse {
  String message;
  String error;

  @JsonPOJOBuilder(withPrefix = "")
  public static class VerifyResponseBuilder {

  }
}
