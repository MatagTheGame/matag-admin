package com.matag.admin.auth.changepassword;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = ChangePasswordResponse.ChangePasswordResponseBuilder.class)
@Builder(toBuilder = true)
public class ChangePasswordResponse {
  String message;

  @JsonPOJOBuilder(withPrefix = "")
  public static class ChangePasswordResponseBuilder {

  }
}
