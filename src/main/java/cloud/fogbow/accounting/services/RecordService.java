package cloud.fogbow.accounting.services;

import cloud.fogbow.accounting.authentication.AccountingPublicKeysHolder;
import cloud.fogbow.accounting.constants.Messages;
import cloud.fogbow.accounting.datastore.DatabaseManager;
import cloud.fogbow.accounting.exceptions.InvalidIntervalException;
import cloud.fogbow.accounting.models.*;
import cloud.fogbow.accounting.plugins.AccountingAuthPlugin;
import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnauthorizedRequestException;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.plugins.authorization.AuthorizationPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

@Service
public class RecordService {

    @Autowired
    private DatabaseManager dbManager;

    private AuthorizationPlugin authPlugin = new AccountingAuthPlugin();

    private final String simpleDateFormat = "yyyy-MM-dd";

    public List<Record> getSelfRecords(String requestingMember, String resourceType, String intervalStart,
        String intervalEnd, String sysUserToken) throws FogbowException, ParseException {
        SystemUser requester = handleAuthIssues(sysUserToken, AccountingOperationType.OWN_BILLING);

        Timestamp start = getTimestampFromString(intervalStart);
        Timestamp end = getTimestampFromString(intervalEnd);

        checkInterval(start, end);

        AccountingUser user = new AccountingUser(
            new UserIdentity(requester.getIdentityProviderId(), requester.getId())
        );

        List<Record> closedRecords = dbManager.getClosedRecords(user, requestingMember, resourceType, start, end);
        List<Record> openedRecords = dbManager.getOpenedRecords(user, requestingMember, resourceType, start, end);

        openedRecords.addAll(closedRecords);
        List<Record> records = openedRecords;

        return records;
    }

    public List<Record> getUserRecords(String userId, String requestingMember, String providingMember, String resourceType,
        String intervalStart, String intervalEnd, String systemUserToken, AccountingOperationType operationType) throws Exception{
        SystemUser requester = handleAuthIssues(systemUserToken, AccountingOperationType.OTHERS_BILLING);

        Timestamp start = getTimestampFromString(intervalStart);
        Timestamp end = getTimestampFromString(intervalEnd);

        checkInterval(start, end);

        AccountingUser user = new AccountingUser(
            new UserIdentity(providingMember, userId)
        );

        List<Record> closedRecords = dbManager.getClosedRecords(user, requestingMember, resourceType, start, end);
        List<Record> openedRecords = dbManager.getOpenedRecords(user, requestingMember, resourceType, start, end);

        setOpenedRecordsDuration(openedRecords);

        openedRecords.addAll(closedRecords);
        List<Record> records = openedRecords;

        return records;
    }

    private SystemUser handleAuthIssues(String sysUserToken, AccountingOperationType operationType) throws FogbowException{
        SystemUser requester = AuthenticationUtil.authenticate(
                AccountingPublicKeysHolder.getInstance().getAsPublicKey(), sysUserToken);

        AccountingOperation operation;
        operation = new AccountingOperation(operationType, requester.getName());

        checkAuthorization(requester, operation);

        return requester;
    }

    private Timestamp getTimestampFromString(String date) throws ParseException{
        Date dateRepresentation = new SimpleDateFormat(simpleDateFormat).parse(date);
        return new Timestamp(dateRepresentation.getTime());
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
            throw new InvalidIntervalException(Messages.Exception.START_TIME_GREATER_THAN_END_TIME);
        } else if(end.getTime() > now || begin.getTime() > now) {
            throw new InvalidIntervalException(Messages.Exception.BILLING_PREDICTIONS);
        }
    }

    private void checkAuthorization(SystemUser systemUser, AccountingOperation operation) throws UnauthorizedRequestException, UnexpectedException {
        if(!authPlugin.isAuthorized(systemUser, operation)) {
            throw new UnauthorizedRequestException(Messages.Exception.UNAUTHORIZED_OPERATION);
        }
    }
}
