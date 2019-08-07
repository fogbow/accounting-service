package cloud.fogbow.accs.core;

import cloud.fogbow.accs.core.datastore.DatabaseManager;
import cloud.fogbow.accs.core.models.AccountingOperation;
import cloud.fogbow.accs.core.models.AccountingOperationType;
import cloud.fogbow.accs.core.models.Record;
import cloud.fogbow.accs.core.plugins.AccountingAuthPlugin;
import cloud.fogbow.as.core.util.AuthenticationUtil;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnauthorizedRequestException;
import cloud.fogbow.common.models.SystemUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ApplicationFacadeTest extends BaseUnitTests{

    ApplicationFacade applicationFacade;
    AccountingAuthPlugin plugin;

    @Before
    public void setup() {
        super.setup();
        this.applicationFacade = new ApplicationFacade();
        plugin = Mockito.mock(AccountingAuthPlugin.class);
        applicationFacade.setAuthorizationPlugin(plugin);

        try {
            testUtils.mockAuthentication();
        } catch (FogbowException ex) {
            LOGGER.info(ex.getMessage());
        }

    }

    @Test(expected = UnauthorizedRequestException.class)
    public void testHandleAuthIssuesWithUnauthorizedUser() throws FogbowException{
        Mockito.when(plugin.isAuthorized(Mockito.any(SystemUser.class), Mockito.any(AccountingOperation.class))).thenReturn(false);
        applicationFacade.handleAuthIssues(testUtils.FAKE_USER_TOKEN, AccountingOperationType.OTHERS_BILLING);
    }

    @Test
    public void testHandleAuthIssuesWithAuthorizedUser() throws FogbowException{
        Mockito.when(plugin.isAuthorized(Mockito.any(SystemUser.class), Mockito.any(AccountingOperation.class))).thenReturn(true);
        SystemUser user = applicationFacade.handleAuthIssues(testUtils.FAKE_USER_TOKEN, AccountingOperationType.OWN_BILLING);
        Assert.assertEquals(user, testUtils.getSysUser());
    }

    @Test
    public void testGetUserRecords() throws Exception{
        DatabaseManager dbManager = testUtils.mockDbManager();
        Mockito.when(plugin.isAuthorized(Mockito.any(SystemUser.class), Mockito.any(AccountingOperation.class))).thenReturn(true);
        Mockito.when(dbManager.getUserRecords(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(getFakeRecordsCollection());

        applicationFacade.getUserRecords(testUtils.FAKE_USER_ID, testUtils.FAKE_REQUESTING_MEMBER, testUtils.FAKE_PROVIDER_MEMBER, testUtils.DEFAULT_RESOURCE_TYPE,
            testUtils.FAKE_INTERVAL, testUtils.FAKE_INTERVAL, testUtils.FAKE_USER_TOKEN);
    }

    private List<Record> getFakeRecordsCollection() {
        List<Record> records = new ArrayList<>();
        for(int i = 0; i < 5; i ++) {
            records.add(
                new Record("fake_order_id", "compute", null, null, null)
            );
        }
        return records;
    }

}