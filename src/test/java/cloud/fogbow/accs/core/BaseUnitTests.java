package cloud.fogbow.accs.core;

import cloud.fogbow.accs.core.datastore.DatabaseManager;
import cloud.fogbow.accs.core.datastore.orderstorage.RecordRepository;
import cloud.fogbow.accs.core.datastore.services.RecordService;
import cloud.fogbow.as.core.util.AuthenticationUtil;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

// to avoid classLoader conflict
@PowerMockIgnore({"javax.management.*"})
@PrepareForTest({RecordRepository.class, RecordService.class, AuthenticationUtil.class, AccountingPublicKeysHolder.class, DatabaseManager.class})
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@SpringBootTest
public class BaseUnitTests {

    protected final Logger LOGGER = Logger.getLogger(BaseUnitTests.class);
    protected TestUtils testUtils;

    @Before
    public void setup() {
        this.testUtils = new TestUtils();
    }
}
