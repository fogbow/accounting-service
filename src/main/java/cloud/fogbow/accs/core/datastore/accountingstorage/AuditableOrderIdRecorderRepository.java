package cloud.fogbow.accs.core.datastore.accountingstorage;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import cloud.fogbow.accs.core.datastore.AccountingStorageConfiguration;
import cloud.fogbow.accs.core.models.AuditableOrderIdRecorder;

@Transactional(AccountingStorageConfiguration.ACCOUNTING_TRANSACTION_MANAGER)
public interface AuditableOrderIdRecorderRepository extends JpaRepository<AuditableOrderIdRecorder, String> {

    Optional<AuditableOrderIdRecorder> findById(String id);

}
