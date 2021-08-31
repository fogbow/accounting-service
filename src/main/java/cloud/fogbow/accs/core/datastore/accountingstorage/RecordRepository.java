package cloud.fogbow.accs.core.datastore.accountingstorage;

import cloud.fogbow.accs.core.datastore.AccountingStorageConfiguration;
import cloud.fogbow.accs.core.models.AccountingUser;
import cloud.fogbow.accs.core.models.Record;
import cloud.fogbow.ras.core.models.orders.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
@Transactional(AccountingStorageConfiguration.ACCOUNTING_TRANSACTION_MANAGER)
public interface RecordRepository extends JpaRepository<Record, String> {

    public Record findByOrderId(String id);

    public List<Record> findByUserAndRequestingMemberAndResourceTypeAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            AccountingUser user, String requestingMember, String resourceType, Timestamp endTime, Timestamp startTime);

    public List<Record> findByUserAndRequestingMemberAndResourceTypeAndStartDateLessThanEqualAndStartDateGreaterThanEqualAndStateEquals(
            AccountingUser user, String requestingMember, String resourceType, Timestamp endTime, Timestamp startTime, OrderState state);

}
