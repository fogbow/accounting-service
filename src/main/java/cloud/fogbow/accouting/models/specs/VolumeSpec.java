package cloud.fogbow.accouting.models.specs;

public class VolumeSpec extends OrderSpec {

    private int size;

    public VolumeSpec(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Resource: Volume, Size: " + this.size;
    }
}
