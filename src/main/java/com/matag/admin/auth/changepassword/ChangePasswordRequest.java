package com.matag.admin.auth.changepassword;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = ChangePasswordRequest.ChangePasswordRequestBuilder.class)
@Builder(toBuilder = true)
public class ChangePasswordRequest {
  String oldPassword;
  String newPassword;

  @JsonPOJOBuilder(withPrefix = "")
  public static class ChangePasswordRequestBuilder {

  }
}
