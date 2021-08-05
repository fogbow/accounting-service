package cloud.fogbow.accs.core.models.orders;

import cloud.fogbow.accs.core.models.specs.NetworkAllocationMode;
import org.apache.log4j.Logger;

import javax.persistence.*;
import javax.validation.constraints.Size;

//TODO explain why this class is duplicated in RAS and ACCS or remove
//this version
@Entity
@Table(name = "network_order_table")
public class NetworkOrder extends Order {
    private static final long serialVersionUID = 1L;

    private transient static final Logger LOGGER = Logger.getLogger(NetworkOrder.class);

    private static final String NAME_COLUMN_NAME = "name";
    private static final String GATEWAY_COLUMN_NAME = "gateway";
    private static final String CIDR_COLUMN_NAME = "cidr";

    @Size(max = Order.FIELDS_MAX_SIZE)
    @Column(name = NAME_COLUMN_NAME)
    private String name;

    @Size(max = Order.FIELDS_MAX_SIZE)
    @Column(name = GATEWAY_COLUMN_NAME)
    private String gateway;

    @Size(max = Order.FIELDS_MAX_SIZE)
    @Column(name = CIDR_COLUMN_NAME)
    private String cidr;

    @Column
    @Enumerated(EnumType.STRING)
    private NetworkAllocationMode allocationMode;

    public String getName() {
        return name;
    }

    public String getGateway() {
        return gateway;
    }

    public String getCidr() {
        return cidr;
    }

    public NetworkAllocationMode getAllocationMode() {
        return allocationMode;
    }
}
