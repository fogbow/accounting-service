package cloud.fogbow.accs.core.models;

import cloud.fogbow.accs.core.models.orders.OrderState;
import cloud.fogbow.accs.core.models.specs.OrderSpec;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "order_record")
public class Record {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column(nullable = false, unique = true)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String orderId;
	
	@Column(nullable = false)
	private String resourceType;
	
	@OneToOne
	private OrderSpec spec;
	
	@Column(nullable = false)
	private String requestingMember;

	@Column
	private Timestamp startTime;

	@Column
	private Timestamp startDate;

	@Column
	private Timestamp endDate;

	@Column
	private Timestamp endTime;
	
	@Column
	private long duration = 0;

	@Column
	@Enumerated(EnumType.STRING)
	private OrderState state;
	
	@OneToOne(cascade={CascadeType.ALL})
	private OrderStateHistory stateHistory;

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "provider_id", referencedColumnName = "provider_id"),
		@JoinColumn(name = "user_key", referencedColumnName = "user_key")
	})
	private AccountingUser user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public OrderSpec getSpec() {
		return spec;
	}

	public void setSpec(OrderSpec spec) {
		this.spec = spec;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public String getRequestingMember() {
		return requestingMember;
	}

	public void setRequestingMember(String requestingMember) {
		this.requestingMember = requestingMember;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public OrderState getState() {
		return state;
	}

	public void updateState(OrderState newState, Timestamp changeTime) {
		this.stateHistory.updateState(newState, changeTime);
		this.state = newState;
	}
	
	public OrderStateHistory getStateHistory() {
		return this.stateHistory;
	}
	
	public void setStateHistory(OrderStateHistory newHistory) {
		this.stateHistory = newHistory;
	}
	
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setUser(AccountingUser user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Record record = (Record) o;
		return duration == record.duration &&
				id.equals(record.id) &&
				Objects.equals(orderId, record.orderId) &&
				Objects.equals(resourceType, record.resourceType) &&
				Objects.equals(spec, record.spec) &&
				Objects.equals(requestingMember, record.requestingMember) &&
				Objects.equals(startTime, record.startTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, orderId, resourceType, spec, requestingMember, startTime, duration);
	}

	public Record(String orderId, String resourceType, OrderSpec spec, String requestingMember, Timestamp startTime, AccountingUser user) {
		this.orderId = orderId;
		this.resourceType = resourceType;
		this.spec = spec;
		this.requestingMember = requestingMember;
		this.startTime = startTime;
		this.duration = 0;
		this.state = OrderState.FULFILLED;
		this.user = user;
		this.stateHistory = new OrderStateHistory();
	}

	public Record(String orderId, String resourceType, OrderSpec spec, String requestingMember, AccountingUser user) {
		this.orderId = orderId;
		this.resourceType = resourceType;
		this.spec = spec;
		this.requestingMember = requestingMember;
		this.duration = 0;
		this.user = user;
		this.stateHistory = new OrderStateHistory();
	}

	public Record() {}
	
}
