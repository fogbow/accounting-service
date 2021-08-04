package cloud.fogbow.accs.core;

import cloud.fogbow.accs.core.datastore.DatabaseManager;
import cloud.fogbow.accs.core.models.AccountingUser;
import cloud.fogbow.accs.core.models.OrderStateHistory;
import cloud.fogbow.accs.core.models.Record;
import cloud.fogbow.accs.core.models.UserIdentity;
import cloud.fogbow.accs.core.models.orders.AuditableOrderStateChange;
import cloud.fogbow.accs.core.models.orders.Order;
import cloud.fogbow.accs.core.models.orders.OrderState;
import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.models.SystemUser;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import java.security.interfaces.RSAPublicKey;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestUtils {
    public final String SELF_USER_PROVIDER_ID = "userProviderId";
    public final String SELF_USER_KEY = "selfUserKey";
    public static final String RECORDS_BY_STATE = "RECORDS_BY_STATE";
    public static final String RECORDS_BY_USER = "RECORDS_BY_USER";
    public static final String SELF = "SELF";
    public static final String OTHER_USER = "OTHER";
    protected final String FAKE_USER_TOKEN = "FAKE_TOKEN";
    protected final String FAKE_USER_ID = "FAKE_USER_ID";
    protected final String FAKE_USER_NAME = "FAKE_USER_NAME";
    protected final String FAKE_IDP = "FAKE_IDENTITY_PROVIDER";
    protected final String FAKE_REQUESTING_MEMBER = "REQUESTING_MEMBER";
    protected final String FAKE_PROVIDER_MEMBER = "PROVIDER";
    protected final String FAKE_INTERVAL = "2000-01-01";
    protected final String DEFAULT_RESOURCE_TYPE = "compute";
    public final int TEN_SECONDS = 10000;
    public final String FAKE_PROVIDER = "mockedProvider";
    public final String FAKE_ID = "mockedId";

    public TestUtils() {}

    public AccountingUser getAccountingUser() {
        return new AccountingUser(
            new UserIdentity(FAKE_PROVIDER, FAKE_ID)
        );
    }

    public List<Record> createClosedRecords(int size) {
        List<Record> records = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Record rec = new Record();
            rec.setStateHistory(new OrderStateHistory());
            rec.setStartTime(new Timestamp(new Date().getTime()));
            rec.updateState(OrderState.CLOSED, new Timestamp(new Date().getTime()));
            records.add(rec);
        }

        return records;
    }

    public List<Record> createOpenedRecords(int size) {
        List<Record> records = new ArrayList<>();

        System.out.println(size);
        for (int i = 0; i < size; i++) {
            Record rec = new Record();
            rec.setStateHistory(new OrderStateHistory());
            rec.setStartTime(new Timestamp(new Date().getTime()));
            rec.updateState(OrderState.FULFILLED, new Timestamp(new Date().getTime()));
            records.add(rec);
        }

        return records;
    }
    
    public List<Record> createRecordsWithState(int size, OrderState state) {
        List<Record> records = new ArrayList<>();

        System.out.println(size);
        for (int i = 0; i < size; i++) {
            Record rec = new Record();
            rec.setStateHistory(new OrderStateHistory());
            rec.setStartTime(new Timestamp(new Date().getTime()));
            rec.updateState(state, new Timestamp(new Date().getTime()));
            records.add(rec);
        }

        return records;
    }

    public List<Record> createSelfUserRecords(int size) {
        AccountingUser selfUser = new AccountingUser(new UserIdentity(SELF_USER_PROVIDER_ID, SELF_USER_KEY));

        List<Record> records = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            Record rec = new Record();
            rec.setStateHistory(new OrderStateHistory());
            rec.setUser(selfUser);
            rec.setStartTime(new Timestamp(new Date().getTime()));
            records.add(rec);
        }

        return records;
    }

    public List<Record> createRecordsOwnedByUser(AccountingUser user, int size) {
        List<Record> records = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            Record rec = new Record();
            rec.setStateHistory(new OrderStateHistory());
            rec.setUser(user);
            rec.setStartTime(new Timestamp(new Date().getTime()));
            records.add(rec);
        }

        return records;
    }

    public SystemUser getSysUser() {
        return new SystemUser(FAKE_USER_ID, FAKE_USER_NAME, FAKE_IDP);
    }

    public void mockAuthentication() throws FogbowException {
        RSAPublicKey key = Mockito.mock(RSAPublicKey.class);
        AccountingPublicKeysHolder publicKeysHolder = Mockito.mock(AccountingPublicKeysHolder.class);
        PowerMockito.mockStatic(AccountingPublicKeysHolder.class);
        BDDMockito.given(AccountingPublicKeysHolder.getInstance()).willReturn(publicKeysHolder);
        BDDMockito.given(publicKeysHolder.getAsPublicKey()).willReturn(key);
        PowerMockito.mockStatic(AuthenticationUtil.class);
        BDDMockito.given(AuthenticationUtil.authenticate(Mockito.any(RSAPublicKey.class), Mockito.anyString())).willReturn(getSysUser());
    }

    public DatabaseManager mockDbManager() {
        DatabaseManager dbManager = Mockito.mock(DatabaseManager.class);
        PowerMockito.mockStatic(DatabaseManager.class);
        BDDMockito.given(DatabaseManager.getInstance()).willReturn(dbManager);
        return dbManager;
    }
    
    public Order createOrder(String id) {
        Order order = new Order();
        order.setId(id);
        return order;
    }

    public AuditableOrderStateChange createAuditableOrderStateChange(Order order, OrderState newState) {
        AuditableOrderStateChange auditableOrderStateChange = new AuditableOrderStateChange();
        auditableOrderStateChange.setOrder(order);
        auditableOrderStateChange.setNewState(newState);
        return auditableOrderStateChange;
    }

    public Record createRecord(String orderId) {
        Record record = new Record();
        record.setOrderId(orderId);
        record.setStateHistory(Mockito.mock(OrderStateHistory.class));
        Timestamp startTime = new Timestamp(System.currentTimeMillis());
        Timestamp endTime = new Timestamp(System.currentTimeMillis() + TEN_SECONDS);
        record.setStartTime(startTime);
        record.setEndTime(endTime);
        return record;
    }

    public Record createSimplestRecord(String orderId) {
        Record record = new Record();
        record.setOrderId(orderId);
        record.setStateHistory(Mockito.mock(OrderStateHistory.class));
        return record;
    }

    public Timestamp getNOW() {
        return new Timestamp(System.currentTimeMillis());
    }
}
