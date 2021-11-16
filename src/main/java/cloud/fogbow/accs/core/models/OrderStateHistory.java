package cloud.fogbow.accs.core.models;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cloud.fogbow.accs.core.models.orders.OrderState;

@Entity
@Table(name = "order_record_state_history")
public class OrderStateHistory {

    private static final String STATE_HISTORY_COLUMN_NAME = "state_history";

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private Long id;
	
	@Column(name = STATE_HISTORY_COLUMN_NAME)
    @ElementCollection(fetch = FetchType.EAGER)
	private Map<Timestamp, OrderState> history;
	
	public OrderStateHistory() {
		this.history = new HashMap<Timestamp, OrderState>();
	}
	
    public Long getId() {
		return id;
	}
	
	public void updateState(OrderState newState, Timestamp changeTime) {
		history.put(changeTime, newState);
	}
	
	public Map<Timestamp, OrderState> getHistory() {
		return this.history;
	}
}
