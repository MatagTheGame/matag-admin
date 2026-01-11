package com.matag.admin.user.profile;

import com.matag.admin.auth.login.CurrentUserProfileDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matag.admin.auth.SecurityContextHolderHelper;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/profile")
public class CurrentUserProfileController {
  private final SecurityContextHolderHelper securityContextHolderHelper;
  private final CurrentUserProfileService currentUserProfileService;

  @PreAuthorize("hasAnyRole('USER', 'GUEST')")
  @GetMapping
  public CurrentUserProfileDto getProfile() {
    return currentUserProfileService.getProfile(
      securityContextHolderHelper.getUser(),
      securityContextHolderHelper.getSession()
    );
  }
}
