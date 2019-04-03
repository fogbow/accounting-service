package accouting.models;

import accouting.authentication.PropertiesHolder;
import accouting.constants.ConfigurationPropertyKeys;
import cloud.fogbow.common.models.FogbowOperation;

import java.util.ArrayList;
import java.util.List;

public class AccountingOperation extends FogbowOperation {
    private AccountingOperationType type;

    /**
     * The user who can do the operation even if the userName isn't in the white list
     */
    private String userName;

    public AccountingOperation(AccountingOperationType type, String userName) {
        this.type = type;
        this.userName = userName;
    }

    public List<String> getAllowedUsers() {
        List<String> allowedUsers = new ArrayList<String>();

        if(this.type.equals(AccountingOperationType.OTHERS_BILLING)) {
            String users = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.ALLOWED_USERS);
            for(String userName : users.split(" ")) {
                allowedUsers.add(userName);
            }
        } else {
            allowedUsers.add(userName);
        }

        return allowedUsers;
    }
}
