package cloud.fogbow.accs.core.models.specs;

import javax.persistence.*;

import cloud.fogbow.accs.constants.ApiDocumentation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(subTypes = { ComputeSpec.class })
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class OrderSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private Long id;

    @ApiModelProperty(position = 0, example = ApiDocumentation.Model.ID)
    public Long getId() {
		return id;
	}

	@Override
    public abstract String toString();

}
