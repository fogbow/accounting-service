package cloud.fogbow.accs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.powermock.core.classloader.annotations.PowerMockIgnore;

@PowerMockIgnore("jdk.internal.reflect.*")
@RunWith(PowerMockRunner.class)
@SpringBootTest
public class FogbowAccountingApplicationTests {

	@Test
	public void contextLoads() {
	}

}
