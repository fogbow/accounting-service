package cloud.fogbow.accs.core.datastore;

import cloud.fogbow.accs.constants.SystemConstants;
import cloud.fogbow.accs.core.datastore.orderstorage.*;
import cloud.fogbow.accs.core.datastore.services.RecordService;
import cloud.fogbow.accs.core.models.AccountingUser;
import cloud.fogbow.accs.core.datastore.orderstorage.AuditableOrderIdRecorder;
import cloud.fogbow.accs.core.datastore.orderstorage.AuditableOrderStateChange;
import cloud.fogbow.accs.core.models.Record;
import cloud.fogbow.accs.core.models.orders.Order;
import cloud.fogbow.accs.core.models.orders.OrderState;
import cloud.fogbow.common.models.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
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

    @Autowired
    private RecordService recordService;

    private static DatabaseManager instance;

    private DatabaseManager() {
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void saveRecord(Record record) {
        recordRepository.save(record);
    }

    public List<AuditableOrderStateChange> getAllAuditableOrdersFromCurrentId(Long id) {
        return auditableOrderStateChangeRepository.findByIdGreaterThanEqualOrderByIdAsc(id);
    }

    public Record getRecordByOrderId(String orderId) {
        return recordRepository.findByOrderId(orderId);
    }

    public List<Record> getSelfRecords(String requestingMember, String resourceType, String intervalStart,
                                       String intervalEnd, SystemUser systemUser) throws ParseException {
        return recordService.getSelfRecords(requestingMember, resourceType, intervalStart, intervalEnd, systemUser);
    }

    public List<Record> getUserRecords(String userId, String requestingMember, String providingMember, String resourceType,
                                       String intervalStart, String intervalEnd) throws Exception {
        return recordService.getUserRecords(userId, requestingMember, providingMember, resourceType, intervalStart, intervalEnd);
    }

    public AuditableOrderIdRecorder getIdRecorder() {
        AuditableOrderIdRecorder idRecorder = auditableOrderIdRecorderRepository.findById(SystemConstants.ID_RECORDER_KEY);

        if (idRecorder == null) {
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
