package cloud.fogbow.accouting.models.specs;

public class NetworkSpec extends OrderSpec {

    private String cidr;
    private NetworkAllocationMode allocationMode;

    public NetworkSpec(String cidr, NetworkAllocationMode allocationMode) {
        this.cidr = cidr;
        this.allocationMode = allocationMode;
    }

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
