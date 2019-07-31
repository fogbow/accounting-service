package cloud.fogbow.accs.core.processors;

import cloud.fogbow.accs.core.datastore.orderstorage.AuditableOrderStateChange;
import cloud.fogbow.accs.core.models.Record;
import cloud.fogbow.accs.core.models.orders.Order;

import java.sql.Timestamp;

public class ProcessorsBaseUnitTest {

    protected final Timestamp NOW = new Timestamp(System.currentTimeMillis());

    protected Order createOrder(String id) {
        Order order = new Order();
        order.setId(id);
        return order;
    }

    protected AuditableOrderStateChange createAuditableOrderStateChange(Order order) {
        AuditableOrderStateChange auditableOrderStateChange = new AuditableOrderStateChange();
        auditableOrderStateChange.setOrder(order);
        return auditableOrderStateChange;
    }

    protected Record createRecord(String orderId) {
        Record record = new Record();
        record.setOrderId(orderId);
        return record;
    }
}
