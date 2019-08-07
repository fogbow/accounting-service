package cloud.fogbow.accs.core.datastore;

import cloud.fogbow.accs.core.models.AccountingUser;
import cloud.fogbow.accs.core.models.Record;
import cloud.fogbow.accs.core.models.UserIdentity;
import cloud.fogbow.accs.core.models.orders.OrderState;

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

    public TestUtils() {}

    public AccountingUser getAccountingUser() {
        return new AccountingUser(
            new UserIdentity("mockedProvider", "mockedId")
        );
    }

    public List<Record> createClosedRecords(int size) {
        List<Record> records = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Record rec = new Record();
            rec.setState(OrderState.CLOSED);
            rec.setStartTime(new Timestamp(new Date().getTime()));
            records.add(rec);
        }

        return records;
    }

    public List<Record> createOpenedRecords(int size) {
        List<Record> records = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Record rec = new Record();
            rec.setState(OrderState.FULFILLED);
            rec.setStartTime(new Timestamp(new Date().getTime()));
            records.add(rec);
        }

        return records;
    }

    public List<Record> createSelfUserRecords(int size) {
        AccountingUser selfUser = new AccountingUser(new UserIdentity(SELF_USER_PROVIDER_ID, SELF_USER_KEY));

        List<Record> records = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            Record rec = new Record();
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
            rec.setUser(user);
            rec.setStartTime(new Timestamp(new Date().getTime()));
            records.add(rec);
        }

        return records;
    }
}
