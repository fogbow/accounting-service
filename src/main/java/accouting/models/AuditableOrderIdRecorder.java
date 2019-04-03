package accouting.models;

import javax.persistence.*;

@Entity
@Table(name = "auditable_order_current_id")
public class AuditableOrderIdRecorder {

    @Id
    private String id;

    @Column
    private Long currentId;

    public AuditableOrderIdRecorder(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Long currentId) {
        this.currentId = currentId;
    }
}
