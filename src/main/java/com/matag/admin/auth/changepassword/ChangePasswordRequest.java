package com.matag.admin.auth.changepassword;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChangePasswordRequest(
  @JsonProperty("oldPassword")String oldPassword,
  @JsonProperty("newPassword")String newPassword) {
}
