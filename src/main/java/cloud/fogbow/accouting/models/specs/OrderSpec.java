package cloud.fogbow.accouting.models.specs;

import javax.persistence.Embeddable;

@Embeddable
public abstract class OrderSpec {

    @Override
    public abstract String toString();

}
