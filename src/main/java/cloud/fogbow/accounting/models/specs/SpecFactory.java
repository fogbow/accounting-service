package cloud.fogbow.accounting.models.specs;

import cloud.fogbow.accounting.constants.Messages;
import cloud.fogbow.accounting.datastore.OrderSpecRepository;
import cloud.fogbow.accounting.exceptions.TypeNotAllowedException;
import cloud.fogbow.accounting.models.*;
import cloud.fogbow.accounting.models.orders.ComputeOrder;
import cloud.fogbow.accounting.models.orders.NetworkOrder;
import cloud.fogbow.accounting.models.orders.Order;
import cloud.fogbow.accounting.models.orders.VolumeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpecFactory {

    @Autowired
    private OrderSpecRepository orderSpecRepository;

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

        orderSpecRepository.save(spec);
        return spec;
    }
}
