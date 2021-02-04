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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ApplicationFacadeTest extends BaseUnitTests{

    private ApplicationFacade applicationFacade;
    private AccountingAuthPlugin plugin;

    @Before
    public void setup() {
        super.setup();
        this.applicationFacade = Mockito.spy(new ApplicationFacade());
        plugin = Mockito.mock(AccountingAuthPlugin.class);
        applicationFacade.setAuthorizationPlugin(plugin);

        try {
            testUtils.mockAuthentication();
        } catch (FogbowException ex) {
            LOGGER.info(ex.getMessage());
        }

    }

    //test case: tests if when the user has no authorization to complete that operation the method
    //throws an UnauthorizedRequestException
    @Test(expected = UnauthorizedRequestException.class)//verify
    public void testHandleAuthIssuesWithUnauthorizedUser() throws FogbowException {
        //verify
        Mockito.when(plugin.isAuthorized(Mockito.any(SystemUser.class), Mockito.any(AccountingOperation.class))).thenReturn(false);
        //exercise
        applicationFacade.handleAuthIssues(testUtils.FAKE_USER_TOKEN, AccountingOperationType.OTHERS_BILLING);
    }

    //test case: Check if the user can complete the request when it has authorization to do so
    @Test
    public void testHandleAuthIssuesWithAuthorizedUser() throws FogbowException {
        //verify
        Mockito.when(plugin.isAuthorized(Mockito.any(SystemUser.class), Mockito.any(AccountingOperation.class))).thenReturn(true);
        //exercise
        SystemUser user = applicationFacade.handleAuthIssues(testUtils.FAKE_USER_TOKEN, AccountingOperationType.OWN_BILLING);
        //verify
        Assert.assertEquals(user, testUtils.getSysUser());
    }

    //test case: check if getUserRecords make the calls it has to in the happy path.
    @Test
    public void testGetUserRecords() throws Exception {
        //setup
        DatabaseManager dbManager = testUtils.mockDbManager();
        applicationFacade.setDatabaseManager(dbManager);
        Mockito.when(plugin.isAuthorized(Mockito.any(SystemUser.class), Mockito.any(AccountingOperation.class))).thenReturn(true);
        Mockito.when(dbManager.getUserRecords(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(getFakeRecordsCollection());
        //exercise
        applicationFacade.getUserRecords(testUtils.FAKE_USER_ID, testUtils.FAKE_REQUESTING_MEMBER, testUtils.FAKE_PROVIDER_MEMBER, testUtils.DEFAULT_RESOURCE_TYPE,
            testUtils.FAKE_INTERVAL, testUtils.FAKE_INTERVAL, testUtils.FAKE_USER_TOKEN);
        //verify
        Mockito.verify(applicationFacade, Mockito.times(1)).handleAuthIssues(Mockito.anyString(), Mockito.eq(AccountingOperationType.OTHERS_BILLING));
        Mockito.verify(dbManager, Mockito.times(1)).getUserRecords(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(applicationFacade, Mockito.times(getFakeRecordsCollection().size())).mountResponseRecord(Mockito.any(Record.class));
    }

    //test case: check if getSelfRecords make the calls it has to in the happy path.
    @Test
    public void testGetSelfRecords() throws FogbowException, ParseException {
        //setup
        DatabaseManager dbManager = testUtils.mockDbManager();
        applicationFacade.setDatabaseManager(dbManager);
        Mockito.when(plugin.isAuthorized(Mockito.any(SystemUser.class), Mockito.any(AccountingOperation.class))).thenReturn(true);
        Mockito.when(dbManager.getSelfRecords(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(SystemUser.class)))
                .thenReturn(getFakeRecordsCollection());
        //exercise
        applicationFacade.getSelfRecords(testUtils.FAKE_REQUESTING_MEMBER, testUtils.DEFAULT_RESOURCE_TYPE, testUtils.FAKE_INTERVAL,
            testUtils.FAKE_INTERVAL, testUtils.FAKE_USER_TOKEN);
        //verify
        Mockito.verify(applicationFacade, Mockito.times(1)).handleAuthIssues(Mockito.anyString(), Mockito.eq(AccountingOperationType.OWN_BILLING));
        Mockito.verify(dbManager, Mockito.times(1)).getSelfRecords(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(SystemUser.class));
        Mockito.verify(applicationFacade, Mockito.times(getFakeRecordsCollection().size())).mountResponseRecord(Mockito.any(Record.class));
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