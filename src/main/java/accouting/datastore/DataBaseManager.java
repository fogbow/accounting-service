package accouting.datastore;

import accouting.model.Order;
import accouting.model.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataBaseManager {

    private static DataBaseManager dbManager;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private OrderRepository orderRepository;

    public DataBaseManager() {}

    public static DataBaseManager getDbManager() {
        if(dbManager == null) {
            dbManager = new DataBaseManager();
        }

        return dbManager;
    }

    public List<Record> getUserRecords(String userId) {
        return recordRepository.findByUserId(userId);
    }

    public void saveRecord(Record record) {
        recordRepository.save(record);
    }

    public List<Record> getRecords(String userId, String requestingMember, String providingMember, String resourceType) {
        return recordRepository.findByUserIdAndRequestingMemberAndProvidingMemberAndResourceType(
                userId, requestingMember, providingMember, resourceType
        );
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


}
