package cloud.fogbow.accs.core.datastore.services;

import cloud.fogbow.accs.constants.Messages;
import cloud.fogbow.accs.core.BaseUnitTests;
import cloud.fogbow.accs.core.TestUtils;
import cloud.fogbow.accs.core.datastore.accountingstorage.RecordRepository;
import cloud.fogbow.accs.core.exceptions.InvalidIntervalException;
import cloud.fogbow.accs.core.models.AccountingUser;
import cloud.fogbow.accs.core.models.Record;
import cloud.fogbow.accs.core.models.UserIdentity;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.ras.core.models.orders.OrderState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecordServiceTest extends BaseUnitTests {

    private final String ANY_VALUE = "any";
    private final String TEST_DATE = "2000-01-01";
    private final String DEFAULT_RESOURCE_TYPE = "compute";
    private final String FAKE_REQ_MEMBER = "mockedRequestingMember";
    private final int DEFAULT_RECORDS_SIZE = 2;
    private final int TWO_MINUTES = 120000;
    private final int THREE_MINUTES = 180000;

    private RecordService recordService;

    private RecordRepository recordRepository;

    @Before
    public void setup() {
        super.setup();
        recordRepository = Mockito.mock(RecordRepository.class);
        PowerMockito.mockStatic(RecordRepository.class);
        recordService = Mockito.spy(new RecordService());
        recordService.setRecordRepository(recordRepository);
    }

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
            Assert.fail();
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
        Timestamp startTimestamp = new Timestamp(System.currentTimeMillis() + TWO_MINUTES);
        Timestamp endTimestamp = new Timestamp(System.currentTimeMillis() + THREE_MINUTES);

        try {
            //exercise
            recordService.checkInterval(startTimestamp, endTimestamp);
            Assert.fail();
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
        mockDatabaseOperations(testUtils.RECORDS_BY_STATE, OrderState.FULFILLED, null, null, DEFAULT_RECORDS_SIZE);

        //exercise
        List<Record> records = recordService.getOpenedRecords(testUtils.getAccountingUser(), FAKE_REQ_MEMBER, DEFAULT_RESOURCE_TYPE,
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
        mockDatabaseOperations(testUtils.RECORDS_BY_STATE, OrderState.CLOSED, null, null, DEFAULT_RECORDS_SIZE);

        //exercise
        List<Record> records = recordService.getClosedRecords(testUtils.getAccountingUser(),FAKE_REQ_MEMBER, DEFAULT_RESOURCE_TYPE,
                new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

        //verify
        Assert.assertEquals(records.size(), 2);
        Assert.assertEquals(records.stream().filter(rec -> rec.getState().equals(OrderState.CLOSED)).collect(Collectors.toList()).size(), DEFAULT_RECORDS_SIZE);
    }

    //test case: check if the tested method make the expected calls to aux methods.
    @Test
    public void testGetSelfRecords() throws ParseException {
        //setup
        mockDatabaseOperations(testUtils.RECORDS_BY_USER, null, testUtils.SELF, null, DEFAULT_RECORDS_SIZE);
        SystemUser systemUser = new SystemUser(testUtils.SELF_USER_KEY, testUtils.SELF, testUtils.SELF_USER_PROVIDER_ID);

        //exercise
        recordService.getSelfRecords(testUtils.SELF, DEFAULT_RESOURCE_TYPE, TEST_DATE, TEST_DATE, systemUser);

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
        AccountingUser user = new AccountingUser(new UserIdentity(  ANY_VALUE, ANY_VALUE));
        mockDatabaseOperations(testUtils.RECORDS_BY_USER, null, testUtils.OTHER_USER, user, DEFAULT_RECORDS_SIZE);

        //exercise
        recordService.getUserRecords(ANY_VALUE, ANY_VALUE, ANY_VALUE, DEFAULT_RESOURCE_TYPE, TEST_DATE, TEST_DATE);

        //verify
        Mockito.verify(recordService, Mockito.times(1)).getOpenedRecords(Mockito.any(AccountingUser.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.any(Timestamp.class));
        Mockito.verify(recordService, Mockito.times(1)).getClosedRecords(Mockito.any(AccountingUser.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.any(Timestamp.class));
        Mockito.verify(recordService, Mockito.times(1)).checkInterval(Mockito.any(Timestamp.class), Mockito.any(Timestamp.class));
        Mockito.verify(recordService, Mockito.times(1)).setOpenedRecordsDuration(Mockito.any(List.class));
    }

    private void mockDatabaseOperations(String operation, OrderState state, String userType, AccountingUser user, int size) {
        switch (operation) {
            case TestUtils.RECORDS_BY_STATE:
                mockRecordsByState(state, size);
                break;
            case TestUtils.RECORDS_BY_USER:
                mockRecordsByUser(userType, user, size);
                break;
        }
    }

    private void mockRecordsByUser(String userType, AccountingUser user, int size) {
        switch (userType) {
            case TestUtils.SELF:
                Mockito.doReturn(testUtils.createSelfUserRecords(size)).when(recordService)
                        .getOpenedRecords(Mockito.any(AccountingUser.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.any(Timestamp.class));
                Mockito.doReturn(testUtils.createSelfUserRecords(size)).when(recordService)
                        .getClosedRecords(Mockito.any(AccountingUser.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.any(Timestamp.class));
                break;
            case TestUtils.OTHER_USER:
                Mockito.doReturn(testUtils.createRecordsOwnedByUser(user, size)).when(recordService)
                        .getOpenedRecords(Mockito.any(AccountingUser.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.any(Timestamp.class));
                Mockito.doReturn(new ArrayList<>()).when(recordService)
                        .getClosedRecords(Mockito.any(AccountingUser.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class), Mockito.any(Timestamp.class));
                break;
        }
    }

    private void mockRecordsByState(OrderState state, int size) {
        switch (state) {
            case FULFILLED:
                Mockito.doReturn(testUtils.createOpenedRecords(size)).when(recordRepository).findByUserAndRequestingMemberAndResourceTypeAndStartDateLessThanEqualAndStartDateGreaterThanEqualAndStateEquals(
                        Mockito.any(AccountingUser.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class),
                        Mockito.any(Timestamp.class), Mockito.any(OrderState.class));
                break;
            case CLOSED:
                Mockito.doReturn(testUtils.createClosedRecords(size)).when(recordRepository).findByUserAndRequestingMemberAndResourceTypeAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        Mockito.any(AccountingUser.class), Mockito.anyString(), Mockito.anyString(), Mockito.any(Timestamp.class),
                        Mockito.any(Timestamp.class));
                break;
        }
    }

}
