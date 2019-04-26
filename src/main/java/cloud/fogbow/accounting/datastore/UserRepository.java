package cloud.fogbow.accounting.datastore;

import cloud.fogbow.accounting.models.AccountingUser;
import cloud.fogbow.accounting.models.UserIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AccountingUser, UserIdentity> {
}
