package cloud.fogbow.accs.services;

import cloud.fogbow.accs.core.datastore.orderstorage.RecordRepository;
import cloud.fogbow.accs.core.datastore.services.RecordService;
import cloud.fogbow.accs.core.models.AccountingUser;
import cloud.fogbow.accs.core.models.Record;
import cloud.fogbow.accs.core.models.UserIdentity;
import cloud.fogbow.accs.core.models.orders.OrderState;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ServicesBaseUnitTest {

    protected static final String SELF_USER_PROVIDER_ID = "userProviderId";
    protected static final String SELF_USER_KEY = "selfUserKey";
    protected final String RECORDS_BY_STATE = "RECORDS_BY_STATE";
    protected final String RECORDS_BY_USER = "RECORDS_BY_USER";
    protected final String SELF = "SELF";
    protected final String OTHER_USER = "OTHER";

    @Autowired
    protected RecordService recordService;

    @MockBean
    protected RecordRepository recordRepository;

    public void setup() {
        recordRepository = Mockito.mock(RecordRepository.class);
        PowerMockito.mockStatic(RecordRepository.class);
        recordService = Mockito.mock(RecordService.class);
        PowerMockito.mockStatic(RecordService.class);
        recordService.setRecordRepository(recordRepository);
    }

    protected AccountingUser getAccountingUser() {
        return new AccountingUser(
                new UserIdentity("mockedProvider", "mockedId")
        );
    }

    protected void mockDatabaseOperations(String operation, OrderState state, String userType, AccountingUser user, int size) {
        switch (operation) {
            case RECORDS_BY_STATE:
                mockRecordsByState(state, size);
                break;
            case RECORDS_BY_USER:
                mockRecordsByUser(userType, user, size);
                break;
        }
    }

    protected void mockRecordsByUser(String userType, AccountingUser user, int size) {
        switch (userType) {
            case SELF:
                Mockito.when(recordService.getOpenedRecords(Mockito.any(AccountingUser.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.any(Timestamp.class)))
                        .thenReturn(getSelfUserRecords(size));
                Mockito.when(recordService.getClosedRecords(Mockito.any(AccountingUser.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.any(Timestamp.class)))
                        .thenReturn(new ArrayList<>());
                break;
            case OTHER_USER:
                Mockito.when(recordService.getOpenedRecords(Mockito.any(AccountingUser.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.any(Timestamp.class)))
                        .thenReturn(getRecordsOwnedByUser(user, size));
                Mockito.when(recordService.getClosedRecords(Mockito.any(AccountingUser.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.any(Timestamp.class)))
                        .thenReturn(new ArrayList<>());
                break;
        }
    }

    protected void mockRecordsByState(OrderState state, int size) {
        switch (state) {
            case FULFILLED:
                Mockito.when(recordRepository.findByUserAndRequestingMemberAndResourceTypeAndStartDateLessThanEqualAndStartDateGreaterThanEqualAndStateEquals(
                        Mockito.any(AccountingUser.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class),
                        Mockito.any(Timestamp.class), Mockito.any(OrderState.class))).thenReturn(getOpenedRecords(size));
                break;
            case CLOSED:
                Mockito.when(recordRepository.findByUserAndRequestingMemberAndResourceTypeAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        Mockito.any(AccountingUser.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class),
                        Mockito.any(Timestamp.class))).thenReturn(getClosedRecords(size));
                break;
        }
    }

    protected List<Record> getClosedRecords(int size) {
        List<Record> records = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Record rec = new Record();
            rec.setState(OrderState.CLOSED);
            records.add(rec);
        }

        return records;
    }

    protected List<Record> getOpenedRecords(int size) {
        List<Record> records = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Record rec = new Record();
            rec.setState(OrderState.FULFILLED);
            records.add(rec);
        }

        return records;
    }

    protected List<Record> getSelfUserRecords(int size) {
        AccountingUser selfUser = new AccountingUser(new UserIdentity(SELF_USER_PROVIDER_ID, SELF_USER_KEY));

        List<Record> records = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            Record rec = new Record();
            rec.setUser(selfUser);
            records.add(rec);
        }

        return records;
    }

    protected List<Record> getRecordsOwnedByUser(AccountingUser user, int size) {
        List<Record> records = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            Record rec = new Record();
            rec.setUser(user);
            records.add(rec);
        }

        return records;
    }
}
