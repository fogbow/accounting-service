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

@Entity
@Table(name = "order_record")
@Getter @Setter
@NoArgsConstructor
public class Record {
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
	private Long startTime;
	
	@Column(nullable = false)
	private int duration;

	public Record(String orderId, String resourceType, String spec, String userId, String userName,
			String requestingMember, String providingMember, Long startTime) {
		this.orderId = orderId;
		this.resourceType = resourceType;
		this.spec = spec;
		this.userId = userId;
		this.userName = userName;
		this.requestingMember = requestingMember;
		this.providingMember = providingMember;
		this.startTime = startTime;
	}
	
}
