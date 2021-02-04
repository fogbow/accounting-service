package cloud.fogbow.accs.core.plugins;

import cloud.fogbow.accs.core.models.AccountingOperation;
import cloud.fogbow.common.exceptions.ConfigurationErrorException;
import cloud.fogbow.common.exceptions.UnauthorizedRequestException;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.plugins.authorization.AuthorizationPlugin;

public class AccountingAuthPlugin implements AuthorizationPlugin<AccountingOperation> {

    @Override
    public boolean isAuthorized(SystemUser systemUser, AccountingOperation operation) throws UnauthorizedRequestException {
        return operation.getAllowedUsers().stream()
            .anyMatch(userName -> userName.equals(systemUser.getId()));
    }

	@Override
	public void setPolicy(String policy) throws ConfigurationErrorException {
		// Currently there is no implementation 
		// for this version of AuthorizationPlugin
	}

	@Override
	public void updatePolicy(String policy) throws ConfigurationErrorException {
		// Currently there is no implementation 
		// for this version of AuthorizationPlugin
	}
}
