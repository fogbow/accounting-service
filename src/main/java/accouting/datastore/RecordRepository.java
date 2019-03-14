package accouting.datastore;

import org.springframework.data.jpa.repository.JpaRepository;

import accouting.model.Record;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, String> {
    List<Record> findByUserId(String userId);

    List<Record> findByUserIdAndRequestingMemberAndProvidingMemberAndResourceType(
            String userId, String requestingMember, String providingMember, String resourceType);

    Record findByOrderId(String id);
}
