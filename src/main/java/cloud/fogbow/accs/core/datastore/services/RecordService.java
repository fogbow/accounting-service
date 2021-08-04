package cloud.fogbow.accs.core.datastore.services;

import cloud.fogbow.accs.constants.Messages;
import cloud.fogbow.accs.constants.SystemConstants;
import cloud.fogbow.accs.core.datastore.accountingstorage.RecordRepository;
import cloud.fogbow.accs.core.exceptions.InvalidIntervalException;
import cloud.fogbow.accs.core.models.*;
import cloud.fogbow.accs.core.models.orders.OrderState;
import cloud.fogbow.common.models.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class RecordService {

    // This list contains all the OrderStates that can be described as 'Opened'.
    // If a Record is opened, then its state is contained in the list.
    private static final List<OrderState> OPENED_RECORDS_STATES = Arrays.asList(OrderState.FULFILLED, 
            OrderState.PAUSING, OrderState.PAUSED, 
            OrderState.HIBERNATING, OrderState.HIBERNATED, OrderState.STOPPING, 
            OrderState.STOPPED, OrderState.SPAWNING);
    
    @Autowired
    private RecordRepository recordRepository;

    public RecordService() {}

    public List<Record> getSelfRecords(String requestingMember, String resourceType, String intervalStart,
                                       String intervalEnd, SystemUser requester) throws ParseException {
        Timestamp start = getTimestampFromString(intervalStart);
        Timestamp end = getTimestampFromString(intervalEnd);

        checkInterval(start, end);

        AccountingUser user = new AccountingUser(new UserIdentity(requester.getIdentityProviderId(), requester.getId()));

        List<Record> closedRecords = this.getClosedRecords(user, requestingMember, resourceType, start, end);
        List<Record> openedRecords = this.getOpenedRecords(user, requestingMember, resourceType, start, end);

        setOpenedRecordsDuration(openedRecords);

        openedRecords.addAll(closedRecords);
        List<Record> records = openedRecords;

        return records;
    }

    public List<Record> getUserRecords(String userId, String requestingMember, String providingMember, String resourceType,
                                       String intervalStart, String intervalEnd) throws Exception{
        Timestamp start = getTimestampFromString(intervalStart);
        Timestamp end = getTimestampFromString(intervalEnd);

        checkInterval(start, end);

        AccountingUser user = new AccountingUser(new UserIdentity(providingMember, userId));

        List<Record> closedRecords = this.getClosedRecords(user, requestingMember, resourceType, start, end);
        List<Record> openedRecords = this.getOpenedRecords(user, requestingMember, resourceType, start, end);

        setOpenedRecordsDuration(openedRecords);

        openedRecords.addAll(closedRecords);
        List<Record> records = openedRecords;

        return records;
    }
    
    public List<Record> getAllResourcesUserRecords(String userId, String requestingMember, String providingMember,
            String intervalStart, String intervalEnd) throws ParseException {
        Timestamp start = getTimestampFromString(intervalStart);
        Timestamp end = getTimestampFromString(intervalEnd);

        checkInterval(start, end);

        AccountingUser user = new AccountingUser(new UserIdentity(providingMember, userId));

        List<Record> closedRecords = this.getClosedRecords(user, requestingMember, start, end);
        List<Record> openedRecords = this.getOpenedRecords(user, requestingMember, start, end);

        setOpenedRecordsDuration(openedRecords);

        openedRecords.addAll(closedRecords);
        List<Record> records = openedRecords;

        return records;
    }

    protected List<Record> getOpenedRecords(AccountingUser user, String requestingMember, Timestamp startTime, Timestamp endTime) {
        List<Record> openedRecords = new ArrayList<Record>();

        for (OrderState state : OPENED_RECORDS_STATES) {
            openedRecords.addAll(this.getRecordsByState(user, requestingMember, 
                    startTime, endTime, state));
        }
        
        return openedRecords;
    }
    
    protected List<Record> getOpenedRecords(AccountingUser user, String requestingMember, String resourceType, 
            Timestamp startTime, Timestamp endTime) {
        List<Record> openedRecords = new ArrayList<Record>(); 

        for (OrderState state : OPENED_RECORDS_STATES) {
            openedRecords.addAll(this.getRecordsByState(user, requestingMember, 
                    resourceType, startTime, endTime, state));
        }

        return openedRecords;
    }

    protected List<Record> getClosedRecords(AccountingUser user, String requestingMember, Timestamp beginTime, 
            Timestamp endTime) {
        return recordRepository.findByUserAndRequestingMemberAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                user, requestingMember, endTime, beginTime
        );
    }

    protected List<Record> getClosedRecords(AccountingUser user, String requestingMember, String resourceType, Timestamp beginTime, Timestamp endTime) {
        return recordRepository.findByUserAndRequestingMemberAndResourceTypeAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                user, requestingMember, resourceType, endTime, beginTime
        );
    }

    protected List<Record> getRecordsByState(AccountingUser user, String requestingMember,
            Timestamp startTime, Timestamp endTime, OrderState state) {
        return recordRepository.findByUserAndRequestingMemberAndStartDateLessThanEqualAndStartDateGreaterThanEqualAndStateEquals(
                user, requestingMember, endTime, startTime, state
        );
    }
    
    protected List<Record> getRecordsByState(AccountingUser user, String requestingMember, String resourceType,
            Timestamp startTime, Timestamp endTime, OrderState state) {
        return recordRepository.findByUserAndRequestingMemberAndResourceTypeAndStartDateLessThanEqualAndStartDateGreaterThanEqualAndStateEquals(
                user, requestingMember, resourceType, endTime, startTime, state
        );
    }
    
    protected Timestamp getTimestampFromString(String date) throws ParseException{
        Date dateRepresentation = null;
        
        try {
            dateRepresentation = new SimpleDateFormat(SystemConstants.COMPLETE_DATE_FORMAT).parse(date);
        } catch (ParseException e) {
            dateRepresentation = new SimpleDateFormat(SystemConstants.SIMPLE_DATE_FORMAT).parse(date);
        }
        
        return new Timestamp(dateRepresentation.getTime());
    }

    protected void setOpenedRecordsDuration(List<Record> openedRecords) {
        long now = new Date().getTime();

        for (Record rec : openedRecords) {
            rec.setDuration(now - rec.getStartTime().getTime());
        }
    }

    protected void checkInterval(Timestamp begin, Timestamp end) {
        long now = new Date().getTime();

        if (begin.getTime() > end.getTime()) {
            throw new InvalidIntervalException(Messages.Exception.START_TIME_GREATER_THAN_END_TIME);
        } else if (end.getTime() > now || begin.getTime() > now) {
            throw new InvalidIntervalException(Messages.Exception.BILLING_PREDICTIONS);
        }
    }

    protected void setRecordRepository(RecordRepository repository) {
        this.recordRepository = repository;
    }
}
