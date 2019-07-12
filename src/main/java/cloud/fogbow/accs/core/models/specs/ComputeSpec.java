package cloud.fogbow.accs.core.models.specs;

import javax.persistence.Entity;

import cloud.fogbow.accs.constants.ApiDocumentation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(parent = OrderSpec.class)
@Entity
public class ComputeSpec extends OrderSpec {

    private int vCpu;
    private int ram;

    public ComputeSpec(int vCpu, int ram) {
        this.vCpu = vCpu;
        this.ram = ram;
    }

    public ComputeSpec() {}

    @ApiModelProperty(position = 1, example = ApiDocumentation.Model.VCPU)
    public int getvCpu() {
        return vCpu;
    }

    public void setvCpu(int vCpu) {
        this.vCpu = vCpu;
    }

    @ApiModelProperty(position = 2, example = ApiDocumentation.Model.RAM)
    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    @Override
    public String toString() {
        return "Compute with " + getvCpu() + "vCPUS and " + getRam() + "MB of RAM";
    }
}
