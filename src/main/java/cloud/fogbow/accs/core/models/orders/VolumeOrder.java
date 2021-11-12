package cloud.fogbow.accs.core.models.orders;

import org.apache.log4j.Logger;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "volume_order_table")
public class VolumeOrder extends Order {
    private static final long serialVersionUID = 1L;

    private static final String NAME_COLUMN_NAME = "name";

    @Transient
    private transient final Logger LOGGER = Logger.getLogger(VolumeOrder.class);

    @Column
    private int volumeSize;

    @Size(max = FIELDS_MAX_SIZE)
    @Column(name = NAME_COLUMN_NAME)
    private String name;

    public int getVolumeSize() {
        return volumeSize;
    }

    public String getName() {
        return name;
    }
}
