package cloud.fogbow.accouting.models;

import cloud.fogbow.accouting.models.orders.OrderState;
import cloud.fogbow.accouting.models.specs.OrderSpec;

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
	
	@Column(nullable = false)
	private OrderSpec spec;
	
	@Column(nullable = false)
	private String requestingMember;
	
	@Column(nullable = false)
	private String providingMember;
	
	@Column(nullable = false)
	private Timestamp startTime;

	@Column
	private Timestamp endTime;
	
	@Column(nullable = false)
	private long duration = -1;

	@Column
	private OrderState state;

	@Column
	private OrderSpec orderSpec;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(
					name = "provider_id",
					referencedColumnName = "provider_id"),
			@JoinColumn(
					name = "user_key",
					referencedColumnName = "user_key")
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

	public String getSpec() {
		return spec.toString();
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

	public String getProvidingMember() {
		return providingMember;
	}

	public void setProvidingMember(String providingMember) {
		this.providingMember = providingMember;
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

	public void setState(OrderState newState) {
		this.state = newState;
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
				Objects.equals(providingMember, record.providingMember) &&
				Objects.equals(startTime, record.startTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, orderId, resourceType, spec, requestingMember, providingMember, startTime, duration);
	}

	public Record(String orderId, String resourceType, OrderSpec spec, String userName,
				  String requestingMember, String providingMember, Timestamp startTime) {
		this.orderId = orderId;
		this.resourceType = resourceType;
		this.spec = spec;
		this.requestingMember = requestingMember;
		this.providingMember = providingMember;
		this.startTime = startTime;
		this.duration = -1;
		this.state = OrderState.FULFILLED;
	}

	public Record() {}
	
}
