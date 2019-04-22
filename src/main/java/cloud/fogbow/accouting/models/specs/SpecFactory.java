package cloud.fogbow.accouting.models.specs;

import cloud.fogbow.accouting.constants.Messages;
import cloud.fogbow.accouting.exceptions.TypeNotAllowedException;
import cloud.fogbow.accouting.models.*;
import cloud.fogbow.accouting.models.orders.ComputeOrder;
import cloud.fogbow.accouting.models.orders.NetworkOrder;
import cloud.fogbow.accouting.models.orders.Order;
import cloud.fogbow.accouting.models.orders.VolumeOrder;

public class SpecFactory {

    public OrderSpec constructSpec(Order order) {
        OrderSpec spec;

        ResourceType resourceType = order.getType();
        switch (resourceType) {
            case COMPUTE:
                spec = new ComputeSpec(((ComputeOrder) order).getvCPU(), ((ComputeOrder) order).getMemory());
                break;
            case NETWORK:
                spec = new NetworkSpec(((NetworkOrder) order).getCidr(), ((NetworkOrder) order).getAllocationMode());
                break;
            case VOLUME:
                spec = new VolumeSpec(((VolumeOrder) order).getVolumeSize());
                break;
            default:
                throw new TypeNotAllowedException(String.format(Messages.Exception.TYPE_NOT_ALLOWED_YET, resourceType.getValue()));
        }

        return spec;
    }
}
