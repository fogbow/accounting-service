package cloud.fogbow.accs.services;

import cloud.fogbow.accs.core.AccountingPublicKeysHolder;
import cloud.fogbow.accs.core.models.AccountingUser;
import cloud.fogbow.accs.core.models.Record;
import cloud.fogbow.accs.core.models.UserIdentity;
import cloud.fogbow.accs.core.models.orders.OrderState;
import cloud.fogbow.accs.core.datastore.services.RecordService;
import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.common.models.SystemUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AuthenticationUtil.class)
public class RecordServiceTest {

    private RecordService recordService;
    private AuthenticationUtil authenticationUtil;

    @Before
    public void setup() throws Exception{
        mockDatabaseOperations("mockedRequestingMember", "compute",
            new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        recordService = Mockito.mock(RecordService.class);
    }

    @Test
    public void testGetUserRecords() throws Exception{
        SystemUser sysUser = getSysUser();

        recordService.getUserRecords(sysUser.getId(), "mockedRequestingMember", sysUser.getIdentityProviderId(), "compute",
                "", "");

        Mockito.verify(authenticationUtil.authenticate(
                AccountingPublicKeysHolder.getInstance().getAsPublicKey(),
                "poakdpadopdopddosp"
        ), Mockito.times(1));

    }

    public SystemUser getSysUser() {
        return new SystemUser(
        "mockedId",
        "mockedName",
        "mockedProvider"
        );
    }

    public AccountingUser getAccountingUser() {
        return new AccountingUser(
            new UserIdentity("mockedProvider", "mockedId")
        );
    }

    public void mockDatabaseOperations(String requestingMember, String resourceType, Timestamp begin, Timestamp end) {
        AccountingUser mockedUser = getAccountingUser();
        RecordService databaseManager = Mockito.mock(RecordService.class);
        Mockito.when(databaseManager.getClosedRecords(mockedUser, requestingMember, resourceType, begin, end)).thenReturn(getClosedRecords());
        Mockito.when(databaseManager.getOpenedRecords(mockedUser, requestingMember, resourceType, begin, end)).thenReturn(getOpenedRecords());

        authenticationUtil = Mockito.mock(AuthenticationUtil.class);
        PowerMockito.mockStatic(AuthenticationUtil.class);

    }

    public List<Record> getClosedRecords() {
        List<Record> records = new ArrayList<>();
        Record rec1 = new Record();
        rec1.setState(OrderState.CLOSED);
        Record rec2 = new Record();
        rec2.setState(OrderState.CLOSED);
        records.add(rec1);
        records.add(rec2);
        return records;
    }

    public List<Record> getOpenedRecords() {
        List<Record> records = new ArrayList<>();
        Record rec1 = new Record();
        rec1.setState(OrderState.FULFILLED);
        Record rec2 = new Record();
        rec2.setState(OrderState.FULFILLED);
        records.add(rec1);
        records.add(rec2);
        return records;
    }

    public Timestamp convertStringDateToTimestamp(String date) throws ParseException {
        Date tempDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        return new Timestamp(tempDate.getTime());
    }
}
