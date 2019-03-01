package accouting.datastore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import accouting.model.Record;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "records", path = "records")
public interface RecordRepository extends JpaRepository<Record, Long>{
    List<Record> findByUserId(String userId);
}
