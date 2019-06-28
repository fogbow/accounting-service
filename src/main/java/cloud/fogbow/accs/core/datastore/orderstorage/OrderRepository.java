package cloud.fogbow.accs.core.datastore.orderstorage;

import cloud.fogbow.accs.core.models.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Order findById(String id);
}
