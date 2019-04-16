package cloud.fogbow.accouting.models;

import cloud.fogbow.common.models.SystemUser;
import org.apache.log4j.Logger;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "compute_order_table")
public class ComputeOrder extends Order {
    private static final long serialVersionUID = 1L;

    private static final String NAME_COLUMN_NAME = "name";
    private static final String IMAGE_ID_COLUMN_NAME = "image_id";
    private static final String PUBLIC_KEY_COLUMN_NAME = "public_key";

    public static final int PUBLIC_KEY_MAX_SIZE = 1024;

    @Transient
    private transient final Logger LOGGER = Logger.getLogger(ComputeOrder.class);

    @Column
    private int vCPU;

    // Memory attribute, must be set in MB.
    @Column
    private int memory;

    // Disk attribute, must be set in GB.
    @Column
    private int disk;

    @Size(max = Order.FIELDS_MAX_SIZE)
    @Column(name = NAME_COLUMN_NAME)
    private String name;

    @Size(max = Order.FIELDS_MAX_SIZE)
    @Column(name = IMAGE_ID_COLUMN_NAME)
    private String imageId;

    @Size(max = PUBLIC_KEY_MAX_SIZE)
    @Column(name = PUBLIC_KEY_COLUMN_NAME)
    private String publicKey;

    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> networkIds;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getvCPU() {
        return vCPU;
    }

    public int getMemory() {
        return memory;
    }

    public int getDisk() {
        return disk;
    }

    public String getImageId() {
        return imageId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public List<String> getNetworkIds() {
        if (networkIds == null) {
            return Collections.unmodifiableList(new ArrayList<>());
        }
        return Collections.unmodifiableList(this.networkIds);
    }

    public void setNetworkIds(List<String> networkIds) {
        this.networkIds = networkIds;
    }

}
