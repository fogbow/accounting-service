package cloud.fogbow.accs.core.datastore.orderstorage;

import cloud.fogbow.accs.core.datastore.OrderStorageConfiguration;
import cloud.fogbow.accs.core.models.orders.Order;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(OrderStorageConfiguration.ORDER_TRANSACTION_MANAGER)
public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findById(String id);
}
