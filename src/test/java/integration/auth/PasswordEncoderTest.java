package integration.auth;

import org.junit.Test;

import com.matag.admin.MatagAdminWebSecurityConfiguration;

public class PasswordEncoderTest {
  @Test
  public void encodePassword() {
    var matagAdminWebSecurityConfiguration = new MatagAdminWebSecurityConfiguration();
    var passwordEncoder = matagAdminWebSecurityConfiguration.passwordEncoder();
    var encodedPassword = passwordEncoder.encode("password");
    System.out.println(encodedPassword);
  }
}
