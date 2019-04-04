package accouting;

import accouting.util.PropertiesHolder;
import cloud.fogbow.common.constants.FogbowConstants;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Main implements ApplicationRunner   {

    @Override
    public void run(ApplicationArguments args) {
        String publicKeyFilePath = PropertiesHolder.getInstance().getProperty(FogbowConstants.PUBLIC_KEY_FILE_PATH);
        String privateKeyFilePath = PropertiesHolder.getInstance().getProperty(FogbowConstants.PRIVATE_KEY_FILE_PATH);
        ServiceAsymmetricKeysHolder.getInstance().setPublicKeyFilePath(publicKeyFilePath);
        ServiceAsymmetricKeysHolder.getInstance().setPrivateKeyFilePath(privateKeyFilePath);
    }
}
