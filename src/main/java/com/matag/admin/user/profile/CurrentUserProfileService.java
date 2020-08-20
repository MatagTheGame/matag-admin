package com.matag.admin.user.profile;

import com.matag.admin.session.MatagSession;
import com.matag.admin.user.MatagUser;
import org.springframework.stereotype.Component;

import static com.matag.admin.user.MatagUserType.GUEST;

@Component
public class CurrentUserProfileService {
  public CurrentUserProfileDto getProfile(MatagUser matagUser, MatagSession session) {
    return CurrentUserProfileDto.builder()
      .username(getUsername(matagUser, session))
      .type(matagUser.getType().toString())
      .build();
  }

  private String getUsername(MatagUser matagUser, MatagSession session) {
    if (matagUser.getType() == GUEST) {
      return matagUser.getUsername() + "-" + session.getId();
    } else {
      return matagUser.getUsername();
    }
  }
}
