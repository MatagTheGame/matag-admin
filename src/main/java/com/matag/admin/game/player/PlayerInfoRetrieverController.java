package com.matag.admin.game.player;

import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.session.MatagSession;
import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserType;
import com.matag.adminentities.PlayerInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/player/info")
public class PlayerInfoRetrieverController {
  private final SecurityContextHolderHelper securityContextHolderHelper;

  @GetMapping
  public PlayerInfo deckInfo() {
    MatagSession session = securityContextHolderHelper.getSession();
    return new PlayerInfo(getUsername(session));
  }

  private String getUsername(MatagSession session) {
    MatagUser matagUser = session.getMatagUser();
    if (matagUser.getType() == MatagUserType.GUEST) {
      return matagUser.getUsername() +  "-" + session.getId();
    } else {
      return matagUser.getUsername();
    }
  }
}
