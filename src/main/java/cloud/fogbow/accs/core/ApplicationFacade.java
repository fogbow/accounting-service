package cloud.fogbow.accs.core;

import cloud.fogbow.accs.api.http.response.Record;
import cloud.fogbow.accs.constants.Messages;
import cloud.fogbow.accs.core.datastore.DatabaseManager;
import cloud.fogbow.accs.core.models.AccountingOperation;
import cloud.fogbow.accs.core.models.AccountingOperationType;
import cloud.fogbow.accs.constants.ConfigurationPropertyDefaults;
import cloud.fogbow.accs.constants.ConfigurationPropertyKeys;
import cloud.fogbow.accs.constants.SystemConstants;
import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnauthorizedRequestException;
import cloud.fogbow.common.exceptions.InternalServerErrorException;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.plugins.authorization.AuthorizationPlugin;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.common.util.PropertiesUtil;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ApplicationFacade {

    private static ApplicationFacade instance;
    private AuthorizationPlugin authorizationPlugin;
    private String buildNumber;
    private DatabaseManager dbManager;
    
    public static synchronized ApplicationFacade getInstance() {
        if (instance == null) {
            instance = new ApplicationFacade();
        }
        return instance;
    }

    public void setDatabaseManager(DatabaseManager dbManager) {
    	this.dbManager = dbManager;
    }
    
    public List<Record> getSelfRecords(String requestingMember, String resourceType, String intervalStart,
                                       String intervalEnd, String systemUserToken) throws FogbowException, ParseException {
        SystemUser requester = handleAuthIssues(systemUserToken, AccountingOperationType.OWN_BILLING);

        List<Record> records = new ArrayList<>();
        List<cloud.fogbow.accs.core.models.Record> dbRecords = dbManager.getSelfRecords(
                requestingMember, resourceType, intervalStart, intervalEnd, requester);
        
        for (cloud.fogbow.accs.core.models.Record record : dbRecords) {
            records.add(this.mountResponseRecord(record));
        }

        return records;
    }

    public List<Record> getUserRecords(String userId, String requestingMember, String providingMember, String resourceType,
                                       String intervalStart, String intervalEnd, String systemUserToken) throws Exception{
        handleAuthIssues(systemUserToken, AccountingOperationType.OTHERS_BILLING);

        List<Record> records = new ArrayList<>();
        List<cloud.fogbow.accs.core.models.Record> dbRecords = dbManager.getUserRecords(
                userId, requestingMember, providingMember, resourceType, intervalStart, intervalEnd);

        for (cloud.fogbow.accs.core.models.Record record : dbRecords) {
            records.add(this.mountResponseRecord(record));
        }

        return records;
    }

    public String getPublicKey() throws InternalServerErrorException {
        // There is no need to authenticate the user or authorize this operation
        try {
            return CryptoUtil.toBase64(ServiceAsymmetricKeysHolder.getInstance().getPublicKey());
        } catch (GeneralSecurityException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void setAuthorizationPlugin(AuthorizationPlugin authorizationPlugin) {
        this.authorizationPlugin = authorizationPlugin;
    }

    protected Record mountResponseRecord(cloud.fogbow.accs.core.models.Record dbRecord) {
        return (new Record(dbRecord.getId(), dbRecord.getOrderId(), dbRecord.getResourceType(), dbRecord.getSpec(),
                           dbRecord.getRequestingMember(), dbRecord.getStartTime(), dbRecord.getStartDate(),
                           dbRecord.getEndTime(), dbRecord.getEndDate(), dbRecord.getDuration(), dbRecord.getState()));

    }

    protected SystemUser handleAuthIssues(String systemUserToken, AccountingOperationType operationType) throws FogbowException{
        SystemUser requester = AuthenticationUtil.authenticate(
                AccountingPublicKeysHolder.getInstance().getAsPublicKey(), systemUserToken);

        AccountingOperation operation = new AccountingOperation(operationType, requester.getId());

        checkAuthorization(requester, operation);

        return requester;
    }

    private void checkAuthorization(SystemUser systemUser, AccountingOperation operation) throws UnauthorizedRequestException, InternalServerErrorException {
        if (!authorizationPlugin.isAuthorized(systemUser, operation)) {
            throw new UnauthorizedRequestException(Messages.Exception.UNAUTHORIZED_OPERATION);
        }
    }

	public String getVersionNumber() {
		// There is no need to authenticate the user or authorize this operation
        return SystemConstants.API_VERSION_NUMBER + "-" + this.buildNumber;
	}
	
	// Used for testing
    protected void setBuildNumber(String fileName) {
        Properties properties = PropertiesUtil.readProperties(fileName);
        this.buildNumber = properties.getProperty(ConfigurationPropertyKeys.BUILD_NUMBER_KEY,
                ConfigurationPropertyDefaults.BUILD_NUMBER);
    }
}
