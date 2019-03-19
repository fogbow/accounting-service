package accouting.model;

import org.apache.log4j.Logger;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "state_change_event")
@SequenceGenerator(name="seq", initialValue=1)
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
}

