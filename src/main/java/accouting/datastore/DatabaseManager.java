package accouting.datastore;

import accouting.model.Order;
import accouting.model.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseManager {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<Record> getUserRecords(String userId) {
        return recordRepository.findByUserId(userId);
    }

    public void saveRecord(Record record) {
        recordRepository.save(record);
    }

    public List<Record> getRecords(String userId, String requestingMember, String providingMember, String resourceType) {
        return recordRepository.findByUserId(
                userId
        );
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


    public boolean existsRecordByOrderId(String id) {
        return recordRepository.findByOrderId(id) != null;
    }
}
