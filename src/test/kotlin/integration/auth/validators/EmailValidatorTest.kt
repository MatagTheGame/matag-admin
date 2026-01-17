package integration.auth.validators

import com.matag.admin.auth.validators.EmailValidator
import com.matag.admin.auth.validators.ValidationException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

class EmailValidatorTest {
    private val emailValidator = EmailValidator()

    @Test
    fun validEmail() {
        emailValidator.validate("antonio@mtg.com")
    }

    @Test
    fun invalidEmail() {
        Assertions.assertThrows(
            ValidationException::class.java
        ) { emailValidator.validate("antonio") }
    }

    @Test
    fun tooLongEmail() {
        Assertions.assertThrows(
            ValidationException::class.java
        ) { emailValidator.validate("antonio123".repeat(10) + "@mtg.com") }
    }
}