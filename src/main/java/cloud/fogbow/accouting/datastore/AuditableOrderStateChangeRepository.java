package cloud.fogbow.accouting.datastore;

import cloud.fogbow.accouting.models.AuditableOrderStateChange;
import cloud.fogbow.accouting.models.orders.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditableOrderStateChangeRepository extends JpaRepository<AuditableOrderStateChange, Long> {
    List<AuditableOrderStateChange> findByIdGreaterThanEqualOrderByIdAsc(Long id);
    AuditableOrderStateChange findFirstByOrderIdAndNewStateOrderByTimestampAsc(String orderId, OrderState state);
}
