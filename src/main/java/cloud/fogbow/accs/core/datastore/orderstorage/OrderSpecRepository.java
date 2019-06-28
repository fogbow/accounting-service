package cloud.fogbow.accs.core.datastore.orderstorage;

import cloud.fogbow.accs.core.models.specs.OrderSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderSpecRepository extends JpaRepository<OrderSpec, Long> {
}
