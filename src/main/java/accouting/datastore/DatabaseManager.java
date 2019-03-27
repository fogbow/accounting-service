package accouting.datastore;

import accouting.constants.SystemConstants;
import accouting.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
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
        return auditableOrderStateChangeRepository.findByIdGreaterThanOrderByIdAsc(id);
    }

    public Record getRecordByOrderId(String orderId) {
        return recordRepository.findByOrderId(orderId);
    }

    public AuditableOrderIdRecorder getIdRecorder() {
        AuditableOrderIdRecorder idRecorder = auditableOrderIdRecorderRepository.findById(SystemConstants.ID_RECORDER_KEY);

        if(idRecorder == null) {
            idRecorder = new AuditableOrderIdRecorder();
            idRecorder.setId(SystemConstants.ID_RECORDER_KEY);
            idRecorder.setCurrentId((new Long(0)));
        }

        return idRecorder;
    }

    public void saveIdRecorder(AuditableOrderIdRecorder idRecorder) {
        auditableOrderIdRecorderRepository.save(idRecorder);
    }

    public List<Record> getFullFilledRecords() {
        return recordRepository.findByStateEquals(OrderState.FULFILLED);
    }

    public List<Record> getOpenedRecords(String userId, String requestingMember, String providingMember, String resourceType, Timestamp startTime, Timestamp endTime) {
        return recordRepository.findByUserIdAndRequestingMemberAndProvidingMemberAndResourceTypeAndStartTimeLessThanEqualAndStartTimeGreaterThanEqualAndStateEquals(
                userId, requestingMember, providingMember, resourceType, endTime, startTime, OrderState.FULFILLED
        );
    }

    public List<Record> getClosedRecords(String userId, String requestingMember, String providingMember, String resourceType, Timestamp beginTime, Timestamp endTime) {
        return recordRepository.findByUserIdAndRequestingMemberAndProvidingMemberAndResourceTypeAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                userId, requestingMember, providingMember, resourceType, endTime, beginTime
        );
    }
}
