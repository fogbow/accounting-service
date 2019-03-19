package accouting.datastore;

import accouting.constants.SystemConstants;
import accouting.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseManager {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AuditableOrderStateChangeRepository auditableOrderStateChangeRepository;

    @Autowired
    private AuditableOrderIdRecorderRepository auditableOrderIdRecorderRepository;

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

    public List<AuditableOrderStateChange> getAllAuditableOrdersFromCurrentId(Long id) {
        return auditableOrderStateChangeRepository.findByIdGreaterThan(id);
    }

    public Record getRecordByOrderId(String orderId) {
        return recordRepository.findByOrderId(orderId);
    }

    public AuditableOrderIdRecorder getIdRecorder() {
        AuditableOrderIdRecorder idRecorder = auditableOrderIdRecorderRepository.findById(SystemConstants.ID_RECORDER_KEY);

        if(idRecorder == null) {
            idRecorder = new AuditableOrderIdRecorder();
            idRecorder.setId(SystemConstants.ID_RECORDER_KEY);
            idRecorder.setCurrentId((new Long(1)));
        }

        return idRecorder;
    }

    public List<Record> getFullFilledRecords() {
        return recordRepository.findByStateEquals(OrderState.FULFILLED);
    }
}
