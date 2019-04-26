package cloud.fogbow.accounting.datastore;

import cloud.fogbow.accounting.models.specs.OrderSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderSpecRepository extends JpaRepository<OrderSpec, Long> {
}
