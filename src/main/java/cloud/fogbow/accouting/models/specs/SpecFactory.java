package cloud.fogbow.accouting.models.specs;

import cloud.fogbow.accouting.models.*;

public class SpecFactory {
    private final String COMPUTE_TYPE = "compute";
    private final String NETWORK_TYPE = "network";
    private final String VOLUME_TYPE = "volume";

    public OrderSpec constructSpec(Order order) {
        OrderSpec spec;

        ResourceType resourceType = order.getType();
        switch (resourceType.getValue()) {
            case COMPUTE_TYPE:
                spec = new ComputeSpec(((ComputeOrder) order).getvCPU(), ((ComputeOrder) order).getMemory());
                break;
            case NETWORK_TYPE:
                spec = new NetworkSpec(((NetworkOrder) order).getCidr(), ((NetworkOrder) order).getAllocationMode());
                break;
            case VOLUME_TYPE:
                spec = new VolumeSpec(((VolumeOrder) order).getVolumeSize());
                break;
            default:
                spec = null;
        }

        return spec;
    }
}
