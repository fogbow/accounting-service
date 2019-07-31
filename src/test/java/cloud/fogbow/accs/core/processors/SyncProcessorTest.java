package cloud.fogbow.accs.core.processors;

import cloud.fogbow.accs.core.datastore.DatabaseManager;
import cloud.fogbow.accs.core.datastore.orderstorage.AuditableOrderIdRecorder;
import cloud.fogbow.accs.core.datastore.orderstorage.AuditableOrderStateChange;
import cloud.fogbow.accs.core.models.Record;
import cloud.fogbow.accs.core.models.orders.Order;
import cloud.fogbow.accs.core.models.orders.OrderState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
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

@PowerMockIgnore({"javax.management.*"})
@PowerMockRunnerDelegate(SpringRunner.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest(SyncProcessor.class)
@SpringBootTest
public class SyncProcessorTest extends ProcessorsBaseUnitTest {

    private final String FAKE_ORDER_ID = "SINGLE_FAKE_ORDER_ID";

    @Autowired
    private SyncProcessor syncProcessor;

    @MockBean
    private DatabaseManager dbManager;

    //test case: test if the method works properly with each order state.
    @Test
    public void orderHasFinished() {
        // exercise/verify
        Assert.assertTrue(syncProcessor.orderHasFinished(OrderState.CLOSED));
        Assert.assertTrue(syncProcessor.orderHasFinished(OrderState.FAILED_AFTER_SUCCESSFUL_REQUEST));
        Assert.assertTrue(syncProcessor.orderHasFinished(OrderState.DEACTIVATED));
        Assert.assertFalse(syncProcessor.orderHasFinished(OrderState.FULFILLED));
    }

    //test case: checks if getDuration is doing the right math.
    @Test
    public void testGetDuration() {
        //setup
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp future = new Timestamp(System.currentTimeMillis() + 10000);

        //exercise/verify
        Assert.assertEquals(future.getTime() - now.getTime(), syncProcessor.getDuration(future, now));
    }

    //test case: edge test. Check if the method deals with null values properly.
    @Test
    public void testGetDurationWithNullIntervals() {
        //exercise/verify
        Assert.assertEquals(0, syncProcessor.getDuration(null, null));
    }

    //test case: Check getDuration with equal intervals.
    @Test
    public void testGetDurationWithTheSameTimestamp() {
        //setup
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //exercise/verify
        Assert.assertEquals(0, syncProcessor.getDuration(timestamp, timestamp));
    }

    //test case: Check if the method keeps record's duration when the order is fulfilled.
    @Test
    public void testSetClosedOrderDurationWithFulfilledOrder() {
        //setup
        Record rec = new Record();
        Timestamp startTime = new Timestamp(System.currentTimeMillis());
        Timestamp endTime = new Timestamp(System.currentTimeMillis() + 10000);
        rec.setStartTime(startTime);
        rec.setEndTime(endTime);
        AuditableOrderStateChange auditableOrderStateChange = new AuditableOrderStateChange();
        auditableOrderStateChange.setNewState(OrderState.FULFILLED);
        //verify
        Assert.assertEquals(0, rec.getDuration());
        //exercise
        syncProcessor.setClosedOrderDuration(auditableOrderStateChange, rec);
        //verify
        Assert.assertEquals(0, rec.getDuration());
    }

    //test case: Check if the method keeps record's duration when it has been already set.
    @Test
    public void testSetClosedOrderDurationWhenItIsAlreadySet() {
        //setup
        Record rec = new Record();
        Timestamp startTime = new Timestamp(System.currentTimeMillis());
        Timestamp endTime = new Timestamp(System.currentTimeMillis() + 10000);
        rec.setStartTime(startTime);
        rec.setEndTime(endTime);
        rec.setDuration(1000);
        AuditableOrderStateChange auditableOrderStateChange = new AuditableOrderStateChange();
        auditableOrderStateChange.setNewState(OrderState.CLOSED);
        //verify
        Assert.assertEquals(1000, rec.getDuration());
        //exercise
        syncProcessor.setClosedOrderDuration(auditableOrderStateChange, rec);
        //verify
        Assert.assertEquals(1000, rec.getDuration());
    }

    //test case: test if the method really set the duration when it has to.
    @Test
    public void testSetClosedOrderDuration() {
        //setup
        Record rec = new Record();
        Timestamp startTime = new Timestamp(System.currentTimeMillis());
        Timestamp endTime = new Timestamp(System.currentTimeMillis() + 10000);
        rec.setStartTime(startTime);
        rec.setEndTime(endTime);
        AuditableOrderStateChange auditableOrderStateChange = new AuditableOrderStateChange();
        auditableOrderStateChange.setNewState(OrderState.CLOSED);
        //verify
        Assert.assertEquals(0, rec.getDuration());
        //exercise
        syncProcessor.setClosedOrderDuration(auditableOrderStateChange, rec);
        //verify
        Assert.assertEquals(syncProcessor.getDuration(endTime, startTime), rec.getDuration());
    }

    //test case: check if only startime is changed when the stateChange is a fulfilled one.
    @Test
    public void testSetTimeAttributesWithFulFilledStateChange() {
        //setup
        Order order = createOrder(FAKE_ORDER_ID);
        AuditableOrderStateChange auditableOrderStateChange = createAuditableOrderStateChange(order);
        auditableOrderStateChange.setTimestamp(NOW);
        auditableOrderStateChange.setNewState(OrderState.FULFILLED);
        Record record = createRecord(order.getId());
        Mockito.when(dbManager.getFulfilledStateChange(Mockito.anyString())).thenReturn(auditableOrderStateChange);
        //exercise
        syncProcessor.setTimeAttributes(order, auditableOrderStateChange, record);
        //verify
        Assert.assertEquals(auditableOrderStateChange.getTimestamp(), record.getStartTime());
        Assert.assertEquals(0, record.getDuration());
    }

    //test case: test if the expected attributes are set when the stateChange has failed in newState.
    @Test
    public void testSetTimeAttributesWithFailedStateChange() {
        //setup
        Order order = createOrder(FAKE_ORDER_ID);
        AuditableOrderStateChange fulfilledStateChange = createAuditableOrderStateChange(order);
        fulfilledStateChange.setTimestamp(NOW);
        fulfilledStateChange.setNewState(OrderState.FULFILLED);

        AuditableOrderStateChange failedStateChange = createAuditableOrderStateChange(order);
        Timestamp endTime = new Timestamp(System.currentTimeMillis() + 1000);
        failedStateChange.setTimestamp(endTime);
        failedStateChange.setNewState(OrderState.FAILED_AFTER_SUCCESSFUL_REQUEST);

        Record record = createRecord(order.getId());

        Mockito.when(dbManager.getFulfilledStateChange(Mockito.anyString())).thenReturn(fulfilledStateChange);
        //exercise
        syncProcessor.setTimeAttributes(order, failedStateChange, record);
        //verify
        Assert.assertEquals(fulfilledStateChange.getTimestamp(), record.getStartTime());
        Assert.assertEquals(failedStateChange.getTimestamp(), record.getEndTime());
        Assert.assertEquals(syncProcessor.extractDateFromTimestamp(failedStateChange.getTimestamp()), record.getEndDate());
        Assert.assertEquals(syncProcessor.getDuration(record.getEndTime(), record.getStartTime()), record.getDuration());
    }

    //test case: test if duration is set when the stateChange has UnableToCheckStatus as newState.
    @Test
    public void testSetTimeAttributesWithUnableToCheckStatusStateChange() {
        //setup
        Order order = createOrder("SINGLE_FAKE_ORDER_ID");
        AuditableOrderStateChange fulfilledStateChange = createAuditableOrderStateChange(order);
        fulfilledStateChange.setTimestamp(NOW);
        fulfilledStateChange.setNewState(OrderState.FULFILLED);

        AuditableOrderStateChange unableToCheckStatusStateChange = createAuditableOrderStateChange(order);
        Timestamp endTime = new Timestamp(System.currentTimeMillis() + 1000);
        unableToCheckStatusStateChange.setTimestamp(endTime);
        unableToCheckStatusStateChange.setNewState(OrderState.UNABLE_TO_CHECK_STATUS);

        Record record = createRecord(order.getId());

        Mockito.when(dbManager.getFulfilledStateChange(Mockito.anyString())).thenReturn(fulfilledStateChange);
        //exercise
        syncProcessor.setTimeAttributes(order, unableToCheckStatusStateChange, record);
        //verify
        Assert.assertEquals(fulfilledStateChange.getTimestamp(), record.getStartTime());
        Assert.assertEquals(syncProcessor.getDuration(unableToCheckStatusStateChange.getTimestamp(), record.getStartTime()), record.getDuration());
        Assert.assertEquals(null, record.getEndTime());
    }

    //test case: test if record's attributes has been set when a ClosedStateChange arrives.
    @Test
    public void testManageRecordWithClosedStateChange() {
        //setup
        Order order = createOrder(FAKE_ORDER_ID);
        AuditableOrderStateChange auditableOrderStateChange = createAuditableOrderStateChange(order);
        auditableOrderStateChange.setNewState(OrderState.CLOSED);
        auditableOrderStateChange.setTimestamp(NOW);
        Record record = createRecord(order.getId());

        Mockito.when(dbManager.getRecordByOrderId(Mockito.anyString())).thenReturn(record);
        //exercise
        syncProcessor.manageRecord(auditableOrderStateChange);
        //verify
        Assert.assertEquals(auditableOrderStateChange.getNewState(), record.getState());
        Assert.assertEquals(auditableOrderStateChange.getTimestamp(), record.getEndTime());
        Assert.assertEquals(syncProcessor.extractDateFromTimestamp(auditableOrderStateChange.getTimestamp()), record.getEndDate());
        Assert.assertEquals(syncProcessor.getDuration(record.getEndTime(), record.getStartTime()), record.getDuration());
    }

    //test case: tests if record's duration and state are set when an UnableToCheckStatus event arrives.
    @Test
    public void testManageRecordWithUnableToCheckStatusStateChange() {
        //setup
        Order order = createOrder(FAKE_ORDER_ID);
        AuditableOrderStateChange auditableOrderStateChange = createAuditableOrderStateChange(order);
        auditableOrderStateChange.setNewState(OrderState.UNABLE_TO_CHECK_STATUS);
        auditableOrderStateChange.setTimestamp(NOW);
        Record record = createRecord(order.getId());

        Mockito.when(dbManager.getRecordByOrderId(Mockito.anyString())).thenReturn(record);
        //exercise
        syncProcessor.manageRecord(auditableOrderStateChange);
        //verify
        Assert.assertEquals(syncProcessor.getDuration(auditableOrderStateChange.getTimestamp(), record.getStartTime()), record.getDuration());
        Assert.assertEquals(OrderState.UNABLE_TO_CHECK_STATUS, record.getState());
    }

    //test case: test if the method works properly when a fulfilled state change event arrives.
    //it should not change record startime if set, but if not it should set to the event's timestamp.
    @Test
    public void testManageRecordWithFulfilledStateChange() {
        //setup
        Order order = createOrder(FAKE_ORDER_ID);
        AuditableOrderStateChange auditableOrderStateChange = createAuditableOrderStateChange(order);
        auditableOrderStateChange.setNewState(OrderState.FULFILLED);
        auditableOrderStateChange.setTimestamp(NOW);
        Record record = createRecord(order.getId());
        Timestamp recordStartTime = new Timestamp(System.currentTimeMillis()+1000);
        record.setStartTime(recordStartTime);

        Mockito.when(dbManager.getRecordByOrderId(Mockito.anyString())).thenReturn(record);
        //exercise
        syncProcessor.manageRecord(auditableOrderStateChange);
        //verify
        Assert.assertEquals(0, record.getDuration());
        Assert.assertEquals(OrderState.FULFILLED, record.getState());
        Assert.assertEquals(recordStartTime, record.getStartTime());
        //setup
        record.setStartTime(null);
        //exercise
        syncProcessor.manageRecord(auditableOrderStateChange);
        //verify
        Assert.assertEquals(NOW, record.getStartTime());
    }

    //test case: check if checkOrdersHistory make the calls properly.
    @Test
    public void checkOrdersHistory() {
        //setup
        Order order = createOrder(FAKE_ORDER_ID);
        AuditableOrderStateChange auditableOrderStateChange = createAuditableOrderStateChange(order);
        auditableOrderStateChange.setNewState(OrderState.FULFILLED);
        auditableOrderStateChange.setId(1L);
        AuditableOrderStateChange secondAuditableOrderStateChange = createAuditableOrderStateChange(order);
        secondAuditableOrderStateChange.setNewState(OrderState.CLOSED);
        secondAuditableOrderStateChange.setId(1L);
        secondAuditableOrderStateChange.setTimestamp(NOW);
        List<AuditableOrderStateChange> stateChanges = new ArrayList<>();
        stateChanges.add(auditableOrderStateChange);
        stateChanges.add(secondAuditableOrderStateChange);
        Mockito.when(dbManager.getAllAuditableOrdersFromCurrentId(Mockito.anyLong())).thenReturn(stateChanges);
        AuditableOrderIdRecorder recorder = new AuditableOrderIdRecorder();
        recorder.setCurrentId(1L);
        recorder.setId("recorder");
        Record record = createRecord(order.getId());
        Mockito.when(dbManager.getRecordByOrderId(Mockito.anyString())).thenReturn(record);
        Mockito.when(dbManager.getIdRecorder()).thenReturn(recorder);
        syncProcessor.checkIdRecorder();
        //exercise
        syncProcessor.checkOrdersHistory();
        //verify
        Mockito.verify(dbManager, Mockito.times(stateChanges.size())).getRecordByOrderId(Mockito.anyString());
        Mockito.verify(dbManager, Mockito.times(1)).saveIdRecorder(Mockito.any(AuditableOrderIdRecorder.class));
    }
}
