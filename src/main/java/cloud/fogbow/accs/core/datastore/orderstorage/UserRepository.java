package cloud.fogbow.accs.core.datastore.orderstorage;

import cloud.fogbow.accs.core.models.AccountingUser;
import cloud.fogbow.accs.core.models.UserIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AccountingUser, UserIdentity> {
}
