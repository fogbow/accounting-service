package accouting.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "order_record")
@Getter @Setter
@NoArgsConstructor
public class Record {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, unique = true)
	private Long id;
	
	@Column(nullable = false)
	private String orderId;
	
	@Column(nullable = false)
	private String resourceType;
	
	@Column(nullable = false)
	private String spec;
	
	@Column(nullable = false)
	private String userId;
	
	@Column(nullable = false)
	private String userName;
	
	@Column(nullable = false)
	private String requestingMember;
	
	@Column(nullable = false)
	private String providingMember;
	
	@Column(nullable = false)
	private Timestamp startTime;
	
	@Column(nullable = false)
	private long duration;

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
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Record record = (Record) o;
		return duration == record.duration &&
				Objects.equals(id, record.id) &&
				Objects.equals(orderId, record.orderId) &&
				Objects.equals(resourceType, record.resourceType) &&
				Objects.equals(spec, record.spec) &&
				Objects.equals(userId, record.userId) &&
				Objects.equals(userName, record.userName) &&
				Objects.equals(requestingMember, record.requestingMember) &&
				Objects.equals(providingMember, record.providingMember) &&
				Objects.equals(startTime, record.startTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, orderId, resourceType, spec, userId, userName, requestingMember, providingMember, startTime, duration);
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

	public Record(String orderId, String resourceType, String spec, String userId, String userName,
				  String requestingMember, String providingMember, Timestamp startTime) {
		this.orderId = orderId;
		this.resourceType = resourceType;
		this.spec = spec;
		this.userId = userId;
		this.userName = userName;
		this.requestingMember = requestingMember;
		this.providingMember = providingMember;
		this.startTime = startTime;
		this.duration = -1;
	}
	
}
