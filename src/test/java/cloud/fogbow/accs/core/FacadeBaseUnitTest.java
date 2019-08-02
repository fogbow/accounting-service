package cloud.fogbow.accs.core;

import cloud.fogbow.accs.core.datastore.DatabaseManager;
import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.models.SystemUser;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import java.security.interfaces.RSAPublicKey;

public class FacadeBaseUnitTest {
    protected final String FAKE_USER_TOKEN = "FAKE_TOKEN";
    protected final String FAKE_USER_ID = "FAKE_USER_ID";
    protected final String FAKE_USER_NAME = "FAKE_USER_NAME";
    protected final String FAKE_IDP = "FAKE_IDENTITY_PROVIDER";
    protected final String FAKE_REQUESTING_MEMBER = "REQUESTING_MEMBER";
    protected final String FAKE_PROVIDER_MEMBER = "PROVIDER";
    protected final String FAKE_INTERVAL = "2000-01-01";
    protected final String DEFAULT_RESOURCE_TYPE = "compute";


    protected DatabaseManager dbManager;

    protected SystemUser getSysUser() {
        return new SystemUser(FAKE_USER_ID, FAKE_USER_NAME, FAKE_IDP);
    }

    protected void mockAuthentication() throws FogbowException {
        RSAPublicKey key = Mockito.mock(RSAPublicKey.class);
        AccountingPublicKeysHolder publicKeysHolder = Mockito.mock(AccountingPublicKeysHolder.class);
        PowerMockito.mockStatic(AccountingPublicKeysHolder.class);
        BDDMockito.given(AccountingPublicKeysHolder.getInstance()).willReturn(publicKeysHolder);
        BDDMockito.given(publicKeysHolder.getAsPublicKey()).willReturn(key);
        PowerMockito.mockStatic(AuthenticationUtil.class);
        BDDMockito.given(AuthenticationUtil.authenticate(Mockito.any(RSAPublicKey.class), Mockito.anyString())).willReturn(getSysUser());
    }

    protected void mockDbManager() {
        dbManager = Mockito.mock(DatabaseManager.class);
        PowerMockito.mockStatic(DatabaseManager.class);
        BDDMockito.given(DatabaseManager.getInstance()).willReturn(dbManager);
    }
}
