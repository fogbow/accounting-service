package accouting.datastore;

import accouting.model.AuditableOrderStateChange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditableOrderStateChangeRepository extends JpaRepository<AuditableOrderStateChange, Long> {
    List<AuditableOrderStateChange> findByIdGreaterThan(Long id);
}
