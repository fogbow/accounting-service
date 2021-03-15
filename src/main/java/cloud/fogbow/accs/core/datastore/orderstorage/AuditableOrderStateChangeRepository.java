package cloud.fogbow.accs.core.datastore.orderstorage;

import cloud.fogbow.accs.core.datastore.OrderStorageConfiguration;
import cloud.fogbow.accs.core.models.orders.AuditableOrderStateChange;
import cloud.fogbow.accs.core.models.orders.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(OrderStorageConfiguration.ORDER_TRANSACTION_MANAGER)
public interface AuditableOrderStateChangeRepository extends JpaRepository<AuditableOrderStateChange, Long> {
    List<AuditableOrderStateChange> findByIdGreaterThanEqualOrderByIdAsc(Long id);
    AuditableOrderStateChange findFirstByOrderIdAndNewStateOrderByTimestampAsc(String orderId, OrderState state);
}
