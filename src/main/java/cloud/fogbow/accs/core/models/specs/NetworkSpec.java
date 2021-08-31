package cloud.fogbow.accs.core.models.specs;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import cloud.fogbow.ras.core.models.NetworkAllocationMode;

@Entity
public class NetworkSpec extends OrderSpec {

    private String cidr;

    @Enumerated(EnumType.STRING)
    private NetworkAllocationMode allocationMode;

    public NetworkSpec(String cidr, NetworkAllocationMode allocationMode) {
        this.cidr = cidr;
        this.allocationMode = allocationMode;
    }

    public NetworkSpec() {}

    public String getCidr() {
        return cidr;
    }

    public void setCidr(String cidr) {
        this.cidr = cidr;
    }

    public NetworkAllocationMode getAllocationMode() {
        return allocationMode;
    }

    public void setAllocationMode(NetworkAllocationMode allocationMode) {
        this.allocationMode = allocationMode;
    }

    @Override
    public String toString() {
        return "Resource: Network, Cidr: " + this.cidr + "Allocation mode: " + this.allocationMode.getValue();
    }
}
