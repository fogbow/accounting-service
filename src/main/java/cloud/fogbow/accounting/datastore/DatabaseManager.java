package cloud.fogbow.accounting.datastore;

import cloud.fogbow.accounting.constants.SystemConstants;
import cloud.fogbow.accounting.models.*;
import cloud.fogbow.accounting.models.orders.OrderState;
import cloud.fogbow.accounting.models.orders.Order;
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

    @Autowired
    private UserRepository userRepository;

    public void saveRecord(Record record) {
        recordRepository.save(record);
    }

    public List<AuditableOrderStateChange> getAllAuditableOrdersFromCurrentId(Long id) {
        return auditableOrderStateChangeRepository.findByIdGreaterThanEqualOrderByIdAsc(id);
    }

    public Record getRecordByOrderId(String orderId) {
        return recordRepository.findByOrderId(orderId);
    }

    public AuditableOrderIdRecorder getIdRecorder() {
        AuditableOrderIdRecorder idRecorder = auditableOrderIdRecorderRepository.findById(SystemConstants.ID_RECORDER_KEY);

        if(idRecorder == null) {
            idRecorder = new AuditableOrderIdRecorder();
            idRecorder.setId(SystemConstants.ID_RECORDER_KEY);
            idRecorder.setCurrentId((0L));
            auditableOrderIdRecorderRepository.save(idRecorder);
        }

        return idRecorder;
    }

    public void saveIdRecorder(AuditableOrderIdRecorder idRecorder) {
        auditableOrderIdRecorderRepository.save(idRecorder);
    }

    public List<Record> getOpenedRecords(AccountingUser user, String requestingMember, String resourceType, Timestamp startTime, Timestamp endTime) {
        return recordRepository.findByUserAndRequestingMemberAndResourceTypeAndStartDateLessThanEqualAndStartDateGreaterThanEqualAndStateEquals(
            user, requestingMember, resourceType, endTime, startTime, OrderState.FULFILLED
        );
    }

    public List<Record> getClosedRecords(AccountingUser user, String requestingMember, String resourceType, Timestamp beginTime, Timestamp endTime) {
        return recordRepository.findByUserAndRequestingMemberAndResourceTypeAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            user, requestingMember, resourceType, endTime, beginTime
        );
    }

    public Order getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }

    public void saveUser(AccountingUser user) {
        userRepository.save(user);
    }

    public AuditableOrderStateChange getFulfilledStateChange(String orderId) {
        return auditableOrderStateChangeRepository.findFirstByOrderIdAndNewStateOrderByTimestampAsc(orderId, OrderState.FULFILLED);
    }
}
