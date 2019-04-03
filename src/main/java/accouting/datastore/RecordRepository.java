package accouting.datastore;

import accouting.models.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;

import accouting.models.Record;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, String> {

    Record findByOrderId(String id);

    List<Record> findByUserIdAndRequestingMemberAndProvidingMemberAndResourceTypeAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            String userId, String requestingMember, String providingMember, String resourceType, Timestamp endTime, Timestamp startTime);

    List<Record> findByUserIdAndRequestingMemberAndProvidingMemberAndResourceTypeAndStartTimeLessThanEqualAndStartTimeGreaterThanEqualAndStateEquals(
            String userId, String requestingMember, String providingMember, String resourceType, Timestamp endTime, Timestamp startTime, OrderState state);

}
