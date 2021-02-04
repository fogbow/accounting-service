package cloud.fogbow.accs.core.datastore.orderstorage;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditableOrderIdRecorderRepository extends JpaRepository<AuditableOrderIdRecorder, String> {

    Optional<AuditableOrderIdRecorder> findById(String id);

}
