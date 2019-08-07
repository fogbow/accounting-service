package cloud.fogbow.accs.core.datastore;

import cloud.fogbow.accs.core.datastore.orderstorage.RecordRepository;
import cloud.fogbow.accs.core.datastore.services.RecordService;
import cloud.fogbow.accs.core.models.AccountingUser;
import cloud.fogbow.accs.core.models.Record;
import cloud.fogbow.accs.core.models.UserIdentity;
import cloud.fogbow.accs.core.models.orders.OrderState;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

// to avoid classLoader conflict
@PowerMockIgnore({"javax.management.*"})
@PrepareForTest({RecordRepository.class, RecordService.class})
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@SpringBootTest
public class BaseUnitTests {

    protected TestUtils testUtils;

    @Before
    public void setup() {
        this.testUtils = new TestUtils();
    }
}
