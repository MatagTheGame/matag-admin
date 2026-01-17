package integration.auth

import com.matag.admin.MatagAdminWebSecurityConfiguration
import org.junit.jupiter.api.Test

class PasswordEncoderTest {
    @Test
    fun encodePassword() {
        val matagAdminWebSecurityConfiguration = MatagAdminWebSecurityConfiguration()
        val passwordEncoder = matagAdminWebSecurityConfiguration.passwordEncoder()
        val encodedPassword = passwordEncoder.encode("password")
        println(encodedPassword)
    }
}
