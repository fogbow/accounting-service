package accouting.datastore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import accouting.model.Record;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long>{
    List<Record> findByUserId(String userId);

    List<Record> findByUserIdAndRequestingMemberAndProvidingMemberAndResouceType(
            String userId, String requestingMember, String providingMember, String resourceType);
}
