package integration.auth;

import com.matag.admin.MatagAdminWebSecurityConfiguration;
import org.junit.jupiter.api.Test;

public class PasswordEncoderTest {
  @Test
  public void encodePassword() {
    var matagAdminWebSecurityConfiguration = new MatagAdminWebSecurityConfiguration();
    var passwordEncoder = matagAdminWebSecurityConfiguration.passwordEncoder();
    var encodedPassword = passwordEncoder.encode("password");
    System.out.println(encodedPassword);
  }
}
