package accouting.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "order_table")
public class Order implements Serializable {

    protected static final int ID_FIXED_SIZE = 36; // UUID size

    private static final long serialVersionUID = 1L;

    protected static final String REQUESTER_COLUMN_NAME = "requester";
    protected static final String PROVIDER_COLUMN_NAME = "provider";
    protected static final String CLOUD_NAME_COLUMN_NAME = "cloud_name";
    protected static final String INSTANCE_ID_COLUMN_NAME = "instance_id";
    protected static final int FIELDS_MAX_SIZE = 255;

    @Column
    @Id
    @Size(max = ID_FIXED_SIZE)
    private String id;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    @Column(name = REQUESTER_COLUMN_NAME)
    @Size(max = FIELDS_MAX_SIZE)
    private String requester;

    @Column(name = PROVIDER_COLUMN_NAME)
    @Size(max = FIELDS_MAX_SIZE)
    private String provider;

    @Column(name = CLOUD_NAME_COLUMN_NAME)
    @Size(max = FIELDS_MAX_SIZE)
    private String cloudName;

    @Column(name = INSTANCE_ID_COLUMN_NAME)
    @Size(max = FIELDS_MAX_SIZE)
    private String instanceId;

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCloudName() {
        return cloudName;
    }

    public void setCloudName(String cloudName) {
        this.cloudName = cloudName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public static int getIdFixedSize() {
        return ID_FIXED_SIZE;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Order() {
    }

    public Order(String id) {
        this.id = id;
    }

}
