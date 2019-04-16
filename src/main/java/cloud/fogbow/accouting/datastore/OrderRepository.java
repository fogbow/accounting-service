package cloud.fogbow.accouting.datastore;

import cloud.fogbow.accouting.models.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Order findById(String id);
}
