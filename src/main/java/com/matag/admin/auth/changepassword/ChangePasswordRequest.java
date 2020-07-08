package com.matag.admin.auth.changepassword;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record ChangePasswordRequest(String oldPassword, String newPassword) {
}
