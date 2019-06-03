package cloud.fogbow.accounting;

import cloud.fogbow.accounting.core.ApplicationFacade;
import cloud.fogbow.accounting.core.datastore.DatabaseManager;
import cloud.fogbow.accounting.core.plugins.AccountingAuthPlugin;
import cloud.fogbow.accounting.core.processors.SyncProcessor;
import cloud.fogbow.accounting.core.PropertiesHolder;
import cloud.fogbow.common.constants.FogbowConstants;
import cloud.fogbow.common.exceptions.FatalErrorException;
import cloud.fogbow.common.plugins.authorization.AuthorizationPlugin;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Main implements ApplicationRunner   {
    private final Logger LOGGER = Logger.getLogger(Main.class);

    @Autowired
    SyncProcessor syncProcess;

    @Override
    public void run(ApplicationArguments args) {
        try {
            String publicKeyFilePath = PropertiesHolder.getInstance().getProperty(FogbowConstants.PUBLIC_KEY_FILE_PATH);
            String privateKeyFilePath = PropertiesHolder.getInstance().getProperty(FogbowConstants.PRIVATE_KEY_FILE_PATH);
            ServiceAsymmetricKeysHolder.getInstance().setPublicKeyFilePath(publicKeyFilePath);
            ServiceAsymmetricKeysHolder.getInstance().setPrivateKeyFilePath(privateKeyFilePath);

            AuthorizationPlugin authPlugin = new AccountingAuthPlugin();
            ApplicationFacade applicationFacade = ApplicationFacade.getInstance();
            applicationFacade.setAuthorizationPlugin(authPlugin);
        } catch (FatalErrorException errorException) {
            LOGGER.fatal(errorException.getMessage(), errorException);
            tryExit();
        }
    }

    @PostConstruct
    public void procStarter() {
        new Thread(syncProcess).start();
    }

    private void tryExit() {
        if (!Boolean.parseBoolean(System.getenv("SKIP_TEST_ON_TRAVIS")))
            System.exit(1);
    }
}
