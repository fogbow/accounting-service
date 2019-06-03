package cloud.fogbow.accounting.core.datastore.orderstorage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditableOrderIdRecorderRepository extends JpaRepository<AuditableOrderIdRecorder, String> {

    AuditableOrderIdRecorder findById(String id);

}
