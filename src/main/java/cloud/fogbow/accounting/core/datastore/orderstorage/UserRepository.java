package cloud.fogbow.accounting.core.datastore.orderstorage;

import cloud.fogbow.accounting.core.models.AccountingUser;
import cloud.fogbow.accounting.core.models.UserIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AccountingUser, UserIdentity> {
}
