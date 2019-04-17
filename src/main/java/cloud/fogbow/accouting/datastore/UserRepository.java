package cloud.fogbow.accouting.datastore;

import cloud.fogbow.accouting.models.AccountingUser;
import cloud.fogbow.accouting.models.UserIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AccountingUser, UserIdentity> {
}
