package com.matag.admin.game.player;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.session.MatagSession;
import com.matag.admin.user.MatagUserType;
import com.matag.adminentities.PlayerInfo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/player/info")
public class PlayerInfoRetrieverController {
  private final SecurityContextHolderHelper securityContextHolderHelper;

  @PreAuthorize("hasAnyRole('USER', 'GUEST')")
  @GetMapping
  public PlayerInfo deckInfo() {
    var session = securityContextHolderHelper.getSession();
    return new PlayerInfo(getUsername(session));
  }

  private String getUsername(MatagSession session) {
    var matagUser = session.getMatagUser();
    if (matagUser.getType() == MatagUserType.GUEST) {
      return matagUser.getUsername() +  "-" + session.getId();
    } else {
      return matagUser.getUsername();
    }
  }
}
