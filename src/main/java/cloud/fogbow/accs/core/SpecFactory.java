package cloud.fogbow.accs.core;

import cloud.fogbow.accs.constants.Messages;
import cloud.fogbow.accs.core.datastore.orderstorage.OrderSpecRepository;
import cloud.fogbow.accs.core.exceptions.TypeNotAllowedException;
import cloud.fogbow.accs.core.models.ResourceType;
import cloud.fogbow.accs.core.models.orders.ComputeOrder;
import cloud.fogbow.accs.core.models.orders.NetworkOrder;
import cloud.fogbow.accs.core.models.orders.Order;
import cloud.fogbow.accs.core.models.orders.VolumeOrder;
import cloud.fogbow.accs.core.models.specs.ComputeSpec;
import cloud.fogbow.accs.core.models.specs.NetworkSpec;
import cloud.fogbow.accs.core.models.specs.OrderSpec;
import cloud.fogbow.accs.core.models.specs.VolumeSpec;
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
