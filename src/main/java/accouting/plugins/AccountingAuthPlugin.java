package accouting.plugins;

import accouting.models.AccountingOperation;
import cloud.fogbow.common.exceptions.UnauthorizedRequestException;
import cloud.fogbow.common.exceptions.UnexpectedException;
import cloud.fogbow.common.models.FogbowOperation;
import cloud.fogbow.common.models.SystemUser;
import cloud.fogbow.common.plugins.authorization.AuthorizationPlugin;

public class AccountingAuthPlugin implements AuthorizationPlugin<AccountingOperation> {


    @Override
    public boolean isAuthorized(SystemUser systemUserToken, String cloudName, String operation, String type)
            throws UnauthorizedRequestException, UnexpectedException {
        return true;
    }

    @Override
    public boolean isAuthorized(SystemUser systemUserToken, String operation, String type)
            throws UnauthorizedRequestException, UnexpectedException {
        return true;
    }

    @Override
    public boolean isAuthorized(SystemUser systemUser, AccountingOperation operation) {
        return operation.getAllowedUsers().stream()
                .anyMatch(userName -> userName.equals(systemUser.getName()));
    }
}
