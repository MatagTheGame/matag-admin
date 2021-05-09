package integration.auth.code;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.matag.admin.auth.codes.RandomCodeService;

public class RandomCodeServiceTest {
  private RandomCodeService randomCodeService = new RandomCodeService();

  @Test
  public void generateRandomCode() {
    var code = randomCodeService.generatesRandomCode();

    assertThat(code).hasSize(10);
  }
}
