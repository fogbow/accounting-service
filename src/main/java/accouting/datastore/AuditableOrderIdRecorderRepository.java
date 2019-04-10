package accouting.datastore;

import accouting.models.AuditableOrderIdRecorder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditableOrderIdRecorderRepository extends JpaRepository<AuditableOrderIdRecorder, String> {

    AuditableOrderIdRecorder findById(String id);
}