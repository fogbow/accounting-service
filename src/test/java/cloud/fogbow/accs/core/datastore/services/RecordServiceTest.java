package cloud.fogbow.accs.core.datastore.services;

import cloud.fogbow.accs.constants.Messages;
import cloud.fogbow.accs.core.datastore.orderstorage.RecordRepository;
import cloud.fogbow.accs.core.exceptions.InvalidIntervalException;
import cloud.fogbow.accs.core.models.AccountingUser;
import cloud.fogbow.accs.core.models.Record;
import cloud.fogbow.accs.core.models.UserIdentity;
import cloud.fogbow.accs.core.models.orders.OrderState;
import cloud.fogbow.common.models.SystemUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@PowerMockIgnore({"javax.management.*"})
@PrepareForTest({RecordRepository.class, RecordService.class})
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@SpringBootTest
public class RecordServiceTest extends ServicesBaseUnitTest{

    private final String ANY_VALUE = "any";
    private final String TEST_DATE = "2000-01-01";
    private final String DEFAULT_RESOURCE_TYPE = "compute";

    //test case: When the start time is greater than the end time the
    //message checked below must be part of the threw exception.
    @Test
    public void testCheckIntervalWhenStartTimeIsGreaterThanAllowed() {
        //setup
        Timestamp endTimeStamp = new Timestamp(System.currentTimeMillis());
        Timestamp startTimeStamp = new Timestamp(System.currentTimeMillis() + 1);

        try {
            //exercise
            recordService.checkInterval(startTimeStamp, endTimeStamp);
        } catch (InvalidIntervalException ex) {
            //verify
            Assert.assertEquals(Messages.Exception.START_TIME_GREATER_THAN_END_TIME, ex.getMessage());
        }
    }

    //test case: When the start time is greater than now the
    //message checked below must be part of the threw exception.
    @Test
    public void testCheckIntervalWhenBillingPredictions() {
        //setup
        Timestamp startTimestamp = new Timestamp(System.currentTimeMillis() + 60000*2);
        Timestamp endTimestamp = new Timestamp(System.currentTimeMillis() + 60000*3);

        try {
            //exercise
            recordService.checkInterval(startTimestamp, endTimestamp);
        } catch(InvalidIntervalException ex) {
            //verify
            Assert.assertEquals(Messages.Exception.BILLING_PREDICTIONS, ex.getMessage());
        }
    }

    //test case: check if the timestamp returned is the expected one.
    @Test
    public void testGetTimestampFromString() throws ParseException{
        //setup
        Timestamp twoThousandTimestamp = Timestamp.valueOf("2000-01-01 00:00:00");

        //exercise/verify
        Assert.assertEquals(twoThousandTimestamp, recordService.getTimestampFromString(TEST_DATE));
    }

    //test case: just exercise the method by checking if it returned what it should return
    //(exactly what the repository returned, once its up to spring to test the correctness of the repository).
    @Test
    public void testGetOpenedRecords() {
        //setup
        mockDatabaseOperations(RECORDS_BY_STATE, OrderState.FULFILLED, null, null, 2);

        //exercise
        List<Record> records = recordService.getOpenedRecords(getAccountingUser(),"mockedRequestingMember", DEFAULT_RESOURCE_TYPE,
            new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

        //verify
        Assert.assertEquals(2, records.size());
        Assert.assertEquals(2, records.stream().filter(rec -> rec.getState().equals(OrderState.FULFILLED)).collect(Collectors.toList()).size());
    }

    //test case: just exercise the method by checking if it returned what it should return
    //(exactly what the repository returned, once its up to spring to test the correctness of the repository).
    @Test
    public void testGetClosedRecords() {
        //setup
        mockDatabaseOperations(RECORDS_BY_STATE, OrderState.CLOSED, null, null, 2);

        //exercise
        List<Record> records = recordService.getClosedRecords(getAccountingUser(),"mockedRequestingMember", DEFAULT_RESOURCE_TYPE,
                new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

        //verify
        Assert.assertEquals(records.size(), 2);
        Assert.assertEquals(records.stream().filter(rec -> rec.getState().equals(OrderState.CLOSED)).collect(Collectors.toList()).size(), 2);
    }

    //test case: check if the tested method make the expected calls to aux methods.
    @Test
    public void testGetSelfRecords() throws ParseException {
        //setup
        setup();
        mockDatabaseOperations(RECORDS_BY_USER, null, SELF, null, 2);
        Mockito.when(recordService.getSelfRecords(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(SystemUser.class)))
            .thenCallRealMethod();
        SystemUser systemUser = new SystemUser(SELF_USER_KEY, SELF, SELF_USER_PROVIDER_ID);

        //exercise
        List<Record> records = recordService.getSelfRecords(SELF, DEFAULT_RESOURCE_TYPE, TEST_DATE, TEST_DATE, systemUser);

        //verify
        Mockito.verify(recordService, Mockito.times(1)).getOpenedRecords(Mockito.any(AccountingUser.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.any(Timestamp.class));
        Mockito.verify(recordService, Mockito.times(1)).getClosedRecords(Mockito.any(AccountingUser.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.any(Timestamp.class));
        Mockito.verify(recordService, Mockito.times(1)).checkInterval(Mockito.any(Timestamp.class), Mockito.any(Timestamp.class));
        Mockito.verify(recordService, Mockito.times(1)).setOpenedRecordsDuration(Mockito.any(List.class));
    }

    //test case: check if the tested method make the expected calls to aux methods.
    @Test
    public void testGetUserRecords() throws Exception {
        //setup
        setup();
        AccountingUser user = new AccountingUser(new UserIdentity(  ANY_VALUE, ANY_VALUE));
        mockDatabaseOperations(RECORDS_BY_USER, null, OTHER_USER, user, 2);
        Mockito.when(recordService.getUserRecords(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenCallRealMethod();
        SystemUser systemUser = new SystemUser(ANY_VALUE, ANY_VALUE, ANY_VALUE);

        //exercise
        List<Record> records = recordService.getUserRecords(ANY_VALUE, ANY_VALUE, ANY_VALUE, DEFAULT_RESOURCE_TYPE,
                TEST_DATE, TEST_DATE);

        //verify
        Mockito.verify(recordService, Mockito.times(1)).getOpenedRecords(Mockito.any(AccountingUser.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.any(Timestamp.class));
        Mockito.verify(recordService, Mockito.times(1)).getClosedRecords(Mockito.any(AccountingUser.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.any(Timestamp.class));
        Mockito.verify(recordService, Mockito.times(1)).checkInterval(Mockito.any(Timestamp.class), Mockito.any(Timestamp.class));
        Mockito.verify(recordService, Mockito.times(1)).setOpenedRecordsDuration(Mockito.any(List.class));
    }

}
