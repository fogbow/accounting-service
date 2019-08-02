package cloud.fogbow.accs.core;

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

@RunWith(PowerMockRunner.class)
@PrepareForTest({AuthenticationUtil.class, AccountingPublicKeysHolder.class})
public class ApplicationFacadeTest extends FacadeBaseUnitTest{

    ApplicationFacade applicationFacade;
    AccountingAuthPlugin plugin;

    @Before
    public void setup() throws FogbowException {
        this.applicationFacade = new ApplicationFacade();
        plugin = Mockito.mock(AccountingAuthPlugin.class);
        applicationFacade.setAuthorizationPlugin(plugin);
        mockAuthentication();
    }

    @Test(expected = UnauthorizedRequestException.class)
    public void testHandleAuthIssuesWithUnauthorizedUser() throws FogbowException{
        Mockito.when(plugin.isAuthorized(Mockito.any(SystemUser.class), Mockito.any(AccountingOperation.class))).thenReturn(false);
        applicationFacade.handleAuthIssues(FAKE_USER_TOKEN, AccountingOperationType.OTHERS_BILLING);
    }

    @Test
    public void testHandleAuthIssuesWithAuthorizedUser() throws FogbowException{
        Mockito.when(plugin.isAuthorized(Mockito.any(SystemUser.class), Mockito.any(AccountingOperation.class))).thenReturn(true);
        SystemUser user = applicationFacade.handleAuthIssues(FAKE_USER_TOKEN, AccountingOperationType.OWN_BILLING);
        Assert.assertEquals(user, getSysUser());
    }

    @Test
    public void testGetUserRecords() throws Exception{
        mockDbManager();
        Mockito.when(plugin.isAuthorized(Mockito.any(SystemUser.class), Mockito.any(AccountingOperation.class))).thenReturn(true);
        Mockito.when(dbManager.getUserRecords(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(getFakeRecordsCollection());

        applicationFacade.getUserRecords(FAKE_USER_ID, FAKE_REQUESTING_MEMBER, FAKE_PROVIDER_MEMBER, DEFAULT_RESOURCE_TYPE, FAKE_INTERVAL, FAKE_INTERVAL, FAKE_USER_TOKEN);
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