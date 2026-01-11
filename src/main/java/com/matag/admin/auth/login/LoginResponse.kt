package com.matag.admin.auth.login;

import com.matag.admin.user.profile.CurrentUserProfileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
  String token;
  CurrentUserProfileDto profile;
  String error;
}
