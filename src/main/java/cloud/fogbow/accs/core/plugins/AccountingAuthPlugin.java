package cloud.fogbow.accs.core.plugins;

import cloud.fogbow.accs.core.models.AccountingOperation;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.plugins.authorization.AuthorizationPlugin;

public class AccountingAuthPlugin implements AuthorizationPlugin<AccountingOperation> {

    @Override
    public boolean isAuthorized(SystemUser systemUser, AccountingOperation operation) {
        return operation.getAllowedUsers().stream()
            .anyMatch(userName -> userName.equals(systemUser.getName()));
    }
}
