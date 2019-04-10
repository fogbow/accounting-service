package accouting.models;

import accouting.constants.Messages;
import accouting.constants.SystemConstants;
import accouting.util.WhiteList;
import cloud.fogbow.common.models.FogbowOperation;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AccountingOperation extends FogbowOperation {

    private static final Logger LOGGER = Logger.getLogger(AccountingOperation.class);
    private AccountingOperationType type;
    /**
     * The user who can do the operation even if the userName isn't in the white list
     */
    private String userId;

    public AccountingOperation(AccountingOperationType type, String userId) {
        this.type = type;
        this.userId = userId;
    }

    public List<String> getAllowedUsers() {
        List<String> allowedUsers = new ArrayList<String>();

        if(this.type.equals(AccountingOperationType.OTHERS_BILLING)) {
            try {
                allowedUsers = new WhiteList().getAllowedUsers();
            } catch (FileNotFoundException e) {
                LOGGER.warn(String.format(Messages.Warn.ERROR_READING_CONF_FILE, SystemConstants.WHITE_LIST_FILE), e);
            }
        } else {
            allowedUsers.add(userId);
        }

        return allowedUsers;
    }
}
