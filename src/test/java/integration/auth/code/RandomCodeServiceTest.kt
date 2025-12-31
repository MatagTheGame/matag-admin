package integration.auth.code;

import static org.assertj.core.api.Assertions.assertThat;

import com.matag.admin.auth.codes.RandomCodeService;
import org.junit.jupiter.api.Test;

public class RandomCodeServiceTest {
  private RandomCodeService randomCodeService = new RandomCodeService();

  @Test
  public void generateRandomCode() {
    var code = randomCodeService.generatesRandomCode();

    assertThat(code).hasSize(10);
  }
}
