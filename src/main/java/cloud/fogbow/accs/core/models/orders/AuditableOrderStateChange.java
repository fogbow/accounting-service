package cloud.fogbow.accs.core.models.orders;

import javax.persistence.*;
import java.sql.Timestamp;

//TODO explain why this class is duplicated in RAS and ACCS or remove
//this version
@Entity
@Table(name = "state_change_event")
@SequenceGenerator(name="seq", initialValue=1, allocationSize = 1)
public class AuditableOrderStateChange {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
    private Long id;

    @ManyToOne
    private Order order;

    @Column
    private Timestamp timestamp;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderState newState;

    public AuditableOrderStateChange(Timestamp timestamp, Order order, OrderState newState) {
        this.order = order;
        this.timestamp = timestamp;
        this.newState = newState;
    }

    public AuditableOrderStateChange() {}

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public OrderState getNewState() {
        return newState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNewState(OrderState newState) {
        this.newState = newState;
    }
}

