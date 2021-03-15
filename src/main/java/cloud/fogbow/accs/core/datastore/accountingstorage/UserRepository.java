package cloud.fogbow.accs.core.datastore.accountingstorage;

import cloud.fogbow.accs.core.datastore.AccountingStorageConfiguration;
import cloud.fogbow.accs.core.models.AccountingUser;
import cloud.fogbow.accs.core.models.UserIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(AccountingStorageConfiguration.ACCOUNTING_TRANSACTION_MANAGER)
public interface UserRepository extends JpaRepository<AccountingUser, UserIdentity> {
}
