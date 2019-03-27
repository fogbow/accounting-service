package accouting.services;

import accouting.authentication.AccountingPublicKeysHolder;
import accouting.datastore.DatabaseManager;
import accouting.exceptions.InvalidIntervalException;
import accouting.model.Record;
import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.common.models.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

@Service
public class RecordService {

    @Autowired
    private DatabaseManager dbManager;

    public List<Record> getUserRecords(String userId, String requestingMember, String providingMember,
                                       String resourceType, String beginPeriod, String endPeriod, String systemUserToken) throws Exception{
        SystemUser requester = AuthenticationUtil.authenticate(
                AccountingPublicKeysHolder.getInstance().getAsPublicKey(), systemUserToken);

        Date initialDate = new SimpleDateFormat("yyyy-MM-dd").parse(beginPeriod);
        Timestamp begin = new Timestamp(initialDate.getTime());
        System.out.println("timeeeeeeeeee");
        System.out.println(begin);

        Date finalDate = new SimpleDateFormat("yyyy-MM-dd").parse(endPeriod);
        Timestamp end = new Timestamp(finalDate.getTime());

        checkInterval(begin, end);

        List<Record> closedRecords = dbManager.getClosedRecords(userId, requestingMember, providingMember, resourceType, begin, end);
        List<Record> openedRecords = dbManager.getOpenedRecords(userId, requestingMember, providingMember, resourceType, begin, end);

        setOpenedRecordsDuration(openedRecords);

        openedRecords.addAll(closedRecords);
        List<Record> records = openedRecords;

        return records;
    }

    private void setOpenedRecordsDuration(List<Record> openedRecords) {
        long now = new Date().getTime();

        for(Record rec : openedRecords) {
            rec.setDuration(now - rec.getStartTime().getTime());
        }
    }

    private void checkInterval(Timestamp begin, Timestamp end) {
        long now = new Date().getTime();
        System.out.println(now);
        System.out.println(begin.getTime());
        System.out.println(end.getTime());
        if(begin.getTime() > end.getTime()) {
            throw new InvalidIntervalException("Begin time must not be greater than end time");
        } else if(end.getTime() > now || begin.getTime() > now) {
            throw new InvalidIntervalException("Billing predictions are not allowed");
        }
    }
}
