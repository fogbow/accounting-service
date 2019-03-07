package accouting.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "order_table")
public class Order implements Serializable {

    protected static final int ID_FIXED_SIZE = 36; // UUID size

    private static final long serialVersionUID = 1L;

    @Column
    @Id
    @Size(max = ID_FIXED_SIZE)
    private String id;

    public Order() {
    }

    public Order(String id) {
        this.id = id;
    }

}
