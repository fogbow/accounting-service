package cloud.fogbow.accs.core.datastore.accountingstorage;

import cloud.fogbow.accs.core.datastore.AccountingStorageConfiguration;
import cloud.fogbow.accs.core.models.specs.OrderSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(AccountingStorageConfiguration.ACCOUNTING_TRANSACTION_MANAGER)
public interface OrderSpecRepository extends JpaRepository<OrderSpec, Long> {
}
