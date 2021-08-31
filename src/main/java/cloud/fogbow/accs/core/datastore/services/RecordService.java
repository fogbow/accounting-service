package cloud.fogbow.accs.core.datastore.services;

import cloud.fogbow.accs.constants.Messages;
import cloud.fogbow.accs.core.datastore.accountingstorage.RecordRepository;
import cloud.fogbow.accs.core.exceptions.InvalidIntervalException;
import cloud.fogbow.accs.core.models.*;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.ras.core.models.orders.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;

    private static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";

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

    protected List<Record> getClosedRecords(AccountingUser user, String requestingMember, String resourceType, Timestamp beginTime, Timestamp endTime) {
        return recordRepository.findByUserAndRequestingMemberAndResourceTypeAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                user, requestingMember, resourceType, endTime, beginTime
        );
    }

    protected List<Record> getOpenedRecords(AccountingUser user, String requestingMember, String resourceType, Timestamp startTime, Timestamp endTime) {
        return recordRepository.findByUserAndRequestingMemberAndResourceTypeAndStartDateLessThanEqualAndStartDateGreaterThanEqualAndStateEquals(
                user, requestingMember, resourceType, endTime, startTime, OrderState.FULFILLED
        );
    }

    protected Timestamp getTimestampFromString(String date) throws ParseException{
        Date dateRepresentation = new SimpleDateFormat(SIMPLE_DATE_FORMAT).parse(date);
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
