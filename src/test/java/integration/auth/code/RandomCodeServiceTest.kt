package integration.auth.code

import com.matag.admin.auth.codes.RandomCodeService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class RandomCodeServiceTest {
    private val randomCodeService = RandomCodeService()

    @Test
    fun generateRandomCode() {
        val code = randomCodeService.generatesRandomCode()

        Assertions.assertThat(code).hasSize(10)
    }
}
