package cloud.fogbow.accounting.datastore;

import cloud.fogbow.accounting.models.AuditableOrderStateChange;
import cloud.fogbow.accounting.models.orders.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditableOrderStateChangeRepository extends JpaRepository<AuditableOrderStateChange, Long> {
    List<AuditableOrderStateChange> findByIdGreaterThanEqualOrderByIdAsc(Long id);
    AuditableOrderStateChange findFirstByOrderIdAndNewStateOrderByTimestampAsc(String orderId, OrderState state);
}
