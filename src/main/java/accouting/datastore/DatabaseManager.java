package accouting.datastore;

import accouting.constants.SystemConstants;
import accouting.models.*;
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

    public void saveRecord(Record record) {
        recordRepository.save(record);
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
            idRecorder.setCurrentId((0L));
        }

        return idRecorder;
    }

    public void saveIdRecorder(AuditableOrderIdRecorder idRecorder) {
        auditableOrderIdRecorderRepository.save(idRecorder);
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
