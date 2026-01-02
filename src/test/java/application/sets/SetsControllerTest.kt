package application.sets

import application.AbstractApplicationTest
import com.matag.admin.sets.MtgSet
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.OK

class SetsControllerTest : AbstractApplicationTest() {
    @Test
    fun `should return all sets`() {
        // When
        val response = getForEntity("/sets", Array<MtgSet>::class.java)

        // Then
        assertThat(response.status).isEqualTo(OK)
        assertThat(response.responseBody).hasSizeGreaterThan(5)
        assertThat(response.responseBody).contains(
            MtgSet(code = "AER", name = "Aether Revolt")
        )
    }
}