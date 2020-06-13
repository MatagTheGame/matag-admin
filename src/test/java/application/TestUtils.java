package application;

import com.matag.admin.user.MatagUser;

import static com.matag.admin.user.MatagUserStatus.ACTIVE;
import static com.matag.admin.user.MatagUserStatus.INACTIVE;
import static com.matag.admin.user.MatagUserType.GUEST;
import static com.matag.admin.user.MatagUserType.USER;

public class TestUtils {
  public static final String GUEST_USERNAME = "Guest";
  public static final String GUEST_SESSION_TOKEN_1 = "00000000-0000-0000-0001-000000000001";
  public static final String GUEST_SESSION_TOKEN_2 = "00000000-0000-0000-0001-000000000002";

  public static final String USER_1_SESSION_TOKEN = "00000000-0000-0000-0000-000000000001";
  public static final String USER_1_USERNAME = "User1";

  public static final String USER_2_SESSION_TOKEN = "00000000-0000-0000-0000-000000000002";
  public static final String USER_2_USERNAME = "User2";

  public static final String INACTIVE_USER_USERNAME = "InactiveUser";

  public static String PASSWORD = "password";
  public static String PASSWORD_ENCODED = "{argon2}$argon2id$v=19$m=65536,t=4,p=8$kfWxCBLq0XIjaaG8LxfrQg$FkuvunHdrO2m+Dw85b33OUSY7uONpyVCgppJg+BYjsM";

  public static MatagUser guest() {
    return MatagUser.builder()
      .username(GUEST_USERNAME)
      .password(PASSWORD_ENCODED)
      .emailAddress("guest@matag.com")
      .status(ACTIVE)
      .type(GUEST)
      .build();
  }

  public static MatagUser user1() {
    return MatagUser.builder()
      .username(USER_1_USERNAME)
      .password(PASSWORD_ENCODED)
      .emailAddress("user1@matag.com")
      .status(ACTIVE)
      .type(USER)
      .build();
  }

  public static MatagUser user2() {
    return MatagUser.builder()
      .username(USER_2_USERNAME)
      .password(PASSWORD_ENCODED)
      .emailAddress("user2@matag.com")
      .status(ACTIVE)
      .type(USER)
      .build();
  }

  public static MatagUser inactive() {
    return MatagUser.builder()
      .username(INACTIVE_USER_USERNAME)
      .password(PASSWORD_ENCODED)
      .emailAddress("inactiveUser@matag.com")
      .status(INACTIVE)
      .type(USER)
      .build();
  }
}
