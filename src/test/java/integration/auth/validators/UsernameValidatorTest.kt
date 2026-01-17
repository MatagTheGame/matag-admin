package integration.auth.validators

import com.matag.admin.auth.validators.UsernameValidator
import com.matag.admin.auth.validators.ValidationException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

class UsernameValidatorTest {
    private val usernameValidator = UsernameValidator()

    @Test
    fun validUsernames() {
        usernameValidator.validate("antonio")
        usernameValidator.validate("antonio e antonio")
        usernameValidator.validate("_-.@+=*&")
    }

    @Test
    fun tooShortUsername() {
        Assertions.assertThrows(
            ValidationException::class.java
        ) { usernameValidator.validate("AB") }
    }

    @Test
    fun notSureWhy() {
        Assertions.assertThrows(
            ValidationException::class.java
        ) { usernameValidator.validate("012345678901234567890123456") }
    }
}