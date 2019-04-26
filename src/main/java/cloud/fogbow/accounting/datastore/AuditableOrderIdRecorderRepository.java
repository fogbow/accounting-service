package cloud.fogbow.accounting.datastore;

import cloud.fogbow.accounting.models.AuditableOrderIdRecorder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditableOrderIdRecorderRepository extends JpaRepository<AuditableOrderIdRecorder, String> {

    AuditableOrderIdRecorder findById(String id);

}
