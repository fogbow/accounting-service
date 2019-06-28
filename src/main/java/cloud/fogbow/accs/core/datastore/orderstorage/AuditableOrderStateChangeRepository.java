package cloud.fogbow.accs.core.datastore.orderstorage;

import cloud.fogbow.accs.core.models.orders.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditableOrderStateChangeRepository extends JpaRepository<AuditableOrderStateChange, Long> {
    List<AuditableOrderStateChange> findByIdGreaterThanEqualOrderByIdAsc(Long id);
    AuditableOrderStateChange findFirstByOrderIdAndNewStateOrderByTimestampAsc(String orderId, OrderState state);
}
