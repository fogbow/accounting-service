package cloud.fogbow.accouting.services;

import cloud.fogbow.accouting.authentication.AccountingPublicKeysHolder;
import cloud.fogbow.accouting.datastore.DatabaseManager;
import cloud.fogbow.accouting.exceptions.InvalidIntervalException;
import cloud.fogbow.accouting.models.*;
import cloud.fogbow.accouting.plugins.AccountingAuthPlugin;
import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.common.exceptions.UnauthorizedRequestException;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.plugins.authorization.AuthorizationPlugin;
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

    private AuthorizationPlugin authPlugin = new AccountingAuthPlugin();

    public List<Record> getUserRecords(String userId, String requestingMember, String providingMember,
                                       String resourceType, String intervalStart, String intervalEnd, String systemUserToken, AccountingOperationType operationType) throws Exception{
        SystemUser requester = AuthenticationUtil.authenticate(
                AccountingPublicKeysHolder.getInstance().getAsPublicKey(), systemUserToken);

        AccountingOperation operation = new AccountingOperation(operationType, userId);

        checkAuthorization(requester, operation);

        Date initialDate = new SimpleDateFormat("yyyy-MM-dd").parse(intervalStart);
        Timestamp begin = new Timestamp(initialDate.getTime());

        Date finalDate = new SimpleDateFormat("yyyy-MM-dd").parse(intervalEnd);
        Timestamp end = new Timestamp(finalDate.getTime());

        checkInterval(begin, end);

        AccountingUser user = new AccountingUser(
                new UserIdentity(providingMember, userId)
        );

        List<Record> closedRecords = dbManager.getClosedRecords(user, requestingMember, providingMember, resourceType, begin, end);
        List<Record> openedRecords = dbManager.getOpenedRecords(user, requestingMember, providingMember, resourceType, begin, end);

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

        if(begin.getTime() > end.getTime()) {
            throw new InvalidIntervalException("Begin time must not be greater than end time");
        } else if(end.getTime() > now || begin.getTime() > now) {
            throw new InvalidIntervalException("Billing predictions are not allowed");
        }
    }

    private void checkAuthorization(SystemUser systemUser, AccountingOperation operation) throws UnauthorizedRequestException, UnexpectedException {
        if(!authPlugin.isAuthorized(systemUser, operation)) {
            throw new UnauthorizedRequestException("The user is not authorized to do this operation");
        }
    }
}
