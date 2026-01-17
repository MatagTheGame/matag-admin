package integration.auth.validators

import com.matag.admin.auth.validators.PasswordValidator
import com.matag.admin.auth.validators.ValidationException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

class PasswordValidatorTest {
    private val passwordValidator = PasswordValidator()

    @Test
    fun validPassword() {
        passwordValidator.validate("valid")
    }

    @Test
    fun tooShortPassword() {
        Assertions.assertThrows(
            ValidationException::class.java
        ) { passwordValidator.validate("1") }
    }
}