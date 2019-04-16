package cloud.fogbow.accouting.util;

import cloud.fogbow.accouting.constants.ConfigurationPropertyKeys;
import cloud.fogbow.accouting.constants.Messages;
import cloud.fogbow.accouting.constants.SystemConstants;
import cloud.fogbow.common.util.HomeDir;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class WhiteList {

    private static final Logger LOGGER = Logger.getLogger(WhiteList.class);

    private List<String> allowedUsers;

    public WhiteList() throws FileNotFoundException {
        this.allowedUsers = readMembersFromFile(SystemConstants.WHITE_LIST_FILE);
    }

    public List<String> getAllowedUsers() {
        return this.allowedUsers;
    }

    private List<String> readMembersFromFile(String whiteListConfPath) throws FileNotFoundException {
        Properties properties = new Properties();
        InputStream input = new FileInputStream(HomeDir.getPath() + whiteListConfPath);
        allowedUsers = new ArrayList<>();

        try {
            properties.load(input);

            String usersList = properties.getProperty(ConfigurationPropertyKeys.ALLOWED_USERS);
            for (String userId : usersList.split(",")) {
                allowedUsers.add(userId.trim());
            }
        } catch (IOException e) {
            LOGGER.warn(String.format(Messages.Warn.ERROR_READING_CONF_FILE, whiteListConfPath), e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                LOGGER.warn(String.format(Messages.Warn.ERROR_CLOSING_CONF_FILE, whiteListConfPath), e);
            }
        }

        return allowedUsers;
    }
}
