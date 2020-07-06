package integration.auth.code;

import com.matag.admin.auth.codes.RandomCodeService;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomCodeServiceTest {
  private RandomCodeService randomCodeService = new RandomCodeService();

  @Test
  public void generateRandomCode() {
    var code = randomCodeService.generatesRandomCode();

    assertThat(code).hasSize(10);
  }
}
