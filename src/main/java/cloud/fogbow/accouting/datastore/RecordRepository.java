package cloud.fogbow.accouting.datastore;

import cloud.fogbow.accouting.models.AccountingUser;
import cloud.fogbow.accouting.models.orders.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;

import cloud.fogbow.accouting.models.Record;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, String> {

    Record findByOrderId(String id);

    List<Record> findByUserAndRequestingMemberAndResourceTypeAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            AccountingUser user, String requestingMember, String resourceType, Timestamp endTime, Timestamp startTime);

    List<Record> findByUserAndRequestingMemberAndResourceTypeAndStartDateLessThanEqualAndStartDateGreaterThanEqualAndStateEquals(
            AccountingUser user, String requestingMember, String resourceType, Timestamp endTime, Timestamp startTime, OrderState state);

}
