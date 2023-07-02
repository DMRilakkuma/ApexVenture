import cn.dev33.satoken.oauth2.logic.SaOAuth2Util;
import org.junit.jupiter.api.Test;

/**
 * Saoauth2Test
 *
 * @author MC
 * @version 1.0
 * @description
 * @date 2023/7/2 15:06
 */

public class Saoauth2Test {

	@Test
	public void test() {
		System.out.println("test");
		System.out.println(SaOAuth2Util.checkClientModel("f4883dc4682cdba14d91"));
	}


}
