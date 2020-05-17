package application;

import com.matag.admin.user.MatagUser;

import static com.matag.admin.user.MatagUserStatus.ACTIVE;
import static com.matag.admin.user.MatagUserStatus.INACTIVE;
import static com.matag.admin.user.MatagUserType.USER;

public class TestUtils {
  public static final String USER_1_SESSION_TOKEN = "00000000-0000-0000-0000-000000000001";
  public static final String USER_1_USERNAME = "User1";

  public static final String USER_2_SESSION_TOKEN = "00000000-0000-0000-0000-000000000002";
  public static final String USER_2_USERNAME = "User2";

  public static final String INACTIVE_USER_USERNAME = "InactiveUser";

  public static String PASSWORD_ENCODED = "{argon2}$argon2id$v=19$m=65536,t=4,p=8$kfWxCBLq0XIjaaG8LxfrQg$FkuvunHdrO2m+Dw85b33OUSY7uONpyVCgppJg+BYjsM";

  public static MatagUser user1() {
    MatagUser matagUser = new MatagUser();
    matagUser.setUsername(USER_1_USERNAME);
    matagUser.setPassword(PASSWORD_ENCODED);
    matagUser.setEmailAddress("user1@matag.com");
    matagUser.setStatus(ACTIVE);
    matagUser.setType(USER);
    return matagUser;
  }

  public static MatagUser user2() {
    MatagUser matagUser = new MatagUser();
    matagUser.setUsername(USER_2_USERNAME);
    matagUser.setPassword(PASSWORD_ENCODED);
    matagUser.setEmailAddress("user2@matag.com");
    matagUser.setStatus(ACTIVE);
    matagUser.setType(USER);
    return matagUser;
  }

  public static MatagUser inactive() {
    MatagUser matagUser = new MatagUser();
    matagUser.setUsername(INACTIVE_USER_USERNAME);
    matagUser.setPassword(PASSWORD_ENCODED);
    matagUser.setEmailAddress("inactiveUser@matag.com");
    matagUser.setStatus(INACTIVE);
    matagUser.setType(USER);
    return matagUser;
  }
}
