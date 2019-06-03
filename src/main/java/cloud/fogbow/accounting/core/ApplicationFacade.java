package cloud.fogbow.accounting.core;

import cloud.fogbow.accounting.api.http.response.Record;
import cloud.fogbow.accounting.constants.Messages;
import cloud.fogbow.accounting.core.datastore.DatabaseManager;
import cloud.fogbow.accounting.core.models.AccountingOperation;
import cloud.fogbow.accounting.core.models.AccountingOperationType;
import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnauthorizedRequestException;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.plugins.authorization.AuthorizationPlugin;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationFacade {

    private static ApplicationFacade instance;
    private AuthorizationPlugin authorizationPlugin;

    public static synchronized ApplicationFacade getInstance() {
        if (instance == null) {
            instance = new ApplicationFacade();
        }
        return instance;
    }

    public List<Record> getSelfRecords(String requestingMember, String resourceType, String intervalStart,
                                       String intervalEnd, String systemUserToken) throws FogbowException, ParseException {
        SystemUser requester = handleAuthIssues(systemUserToken, AccountingOperationType.OWN_BILLING);

        List<Record> records = new ArrayList<>();
        List<cloud.fogbow.accounting.core.models.Record> dbRecords = DatabaseManager.getInstance().getSelfRecords(
                requestingMember, resourceType, intervalStart, intervalEnd, requester);

        for (cloud.fogbow.accounting.core.models.Record record : dbRecords) {
            records.add(this.mountResponseRecord(record));
        }

        return records;
    }

    public List<Record> getUserRecords(String userId, String requestingMember, String providingMember, String resourceType,
                                       String intervalStart, String intervalEnd, String systemUserToken) throws Exception{
        handleAuthIssues(systemUserToken, AccountingOperationType.OTHERS_BILLING);

        List<Record> records = new ArrayList<>();
        List<cloud.fogbow.accounting.core.models.Record> dbRecords = DatabaseManager.getInstance().getUserRecords(
                userId, requestingMember, providingMember, resourceType, intervalStart, intervalEnd);

        for (cloud.fogbow.accounting.core.models.Record record : dbRecords) {
            records.add(this.mountResponseRecord(record));
        }

        return records;
    }

    public String getPublicKey() throws UnexpectedException {
        // There is no need to authenticate the user or authorize this operation
        try {
            return CryptoUtil.savePublicKey(ServiceAsymmetricKeysHolder.getInstance().getPublicKey());
        } catch (IOException | GeneralSecurityException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }
    }

    public void setAuthorizationPlugin(AuthorizationPlugin authorizationPlugin) {
        this.authorizationPlugin = authorizationPlugin;
    }

    private Record mountResponseRecord(cloud.fogbow.accounting.core.models.Record dbRecord) {
        return (new Record(dbRecord.getId(), dbRecord.getOrderId(), dbRecord.getResourceType(), dbRecord.getSpec(),
                           dbRecord.getRequestingMember(), dbRecord.getStartTime(), dbRecord.getStartDate(),
                           dbRecord.getEndTime(), dbRecord.getEndDate(), dbRecord.getDuration(), dbRecord.getState()));

    }

    private SystemUser handleAuthIssues(String systemUserToken, AccountingOperationType operationType) throws FogbowException{
        SystemUser requester = AuthenticationUtil.authenticate(
                AccountingPublicKeysHolder.getInstance().getAsPublicKey(), systemUserToken);

        AccountingOperation operation = new AccountingOperation(operationType, requester.getName());

        checkAuthorization(requester, operation);

        return requester;
    }

    private void checkAuthorization(SystemUser systemUser, AccountingOperation operation) throws UnauthorizedRequestException, UnexpectedException {
        if(!authorizationPlugin.isAuthorized(systemUser, operation)) {
            throw new UnauthorizedRequestException(Messages.Exception.UNAUTHORIZED_OPERATION);
        }
    }
}
