package accouting.services;

import accouting.authentication.AccountingPublicKeysHolder;
import accouting.datastore.DataBaseManager;
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
    private DataBaseManager dbManager;

    public List<Record> getUserRecords(String userId, String requestingMember, String providingMember,
                                       String resourceType, String beginPeriod, String endPeriod, String systemUserToken) throws Exception{
        SystemUser requester = AuthenticationUtil.authenticate(AccountingPublicKeysHolder.getInstance().getAsPublicKey(), systemUserToken);
        Date initialDate = new SimpleDateFormat("yyyy-MM-dd").parse(beginPeriod);
        Timestamp begin = new Timestamp(initialDate.getTime());

        Date finalDate = new SimpleDateFormat("yyyy-MM-dd").parse(endPeriod);
        Timestamp end = new Timestamp(finalDate.getTime());

        List<Record> records = dbManager.getRecords(userId, requestingMember, providingMember, resourceType);

        for (Record rec : records) {
            rec.setDuration(getRealDuration(rec, begin, end));
        }

        return records;
    }

    private long getRealDuration(Record record, Timestamp begin, Timestamp end) {

        long currentDuration = record.getDuration();

        // Order not closed, so the current duration of order is now - start_time
        if (currentDuration == -1) {
            long now = new Date().getTime();
            currentDuration = now - record.getStartTime().getTime();
        }

        long newDuration = currentDuration;

        // Calculates the real duration, i.e when the order was fulfilled in interest interval.
        if (record.getStartTime().before(begin)) {
            newDuration -= begin.getTime() - record.getStartTime().getTime();
        }

        if (end.getTime() < record.getStartTime().getTime() + currentDuration) {
            newDuration -= (record.getStartTime().getTime() + currentDuration) - end.getTime();
        }

        return Math.max(0,newDuration);
    }

}
