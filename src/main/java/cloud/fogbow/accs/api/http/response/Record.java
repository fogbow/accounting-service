package cloud.fogbow.accs.api.http.response;

import cloud.fogbow.accs.core.models.orders.OrderState;
import cloud.fogbow.accs.core.models.specs.OrderSpec;
import cloud.fogbow.accs.constants.ApiDocumentation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.util.Objects;

@ApiModel
public class Record {

	@ApiModelProperty(position = 0, example = ApiDocumentation.Record.RECORD_ID)
	private Long id;

	@ApiModelProperty(position = 1, example = ApiDocumentation.Record.ORDER_ID)
	private String orderId;
	
	@ApiModelProperty(position = 2, example = ApiDocumentation.Record.RESOURCE_TYPE)
	private String resourceType;
	
	@ApiModelProperty
	private OrderSpec spec;
	
	@ApiModelProperty(position = 4, example = ApiDocumentation.Record.REQUESTER)
	private String requester;

	@ApiModelProperty(position = 5, example = ApiDocumentation.Record.START_TIME)
	private Timestamp startTime;

	@ApiModelProperty(position = 6, example = ApiDocumentation.Record.START_DATE)
	private Timestamp startDate;

	@ApiModelProperty(position = 7, example = ApiDocumentation.Record.END_DATE)
	private Timestamp endDate;

	@ApiModelProperty(position = 8, example = ApiDocumentation.Record.END_TIME)
	private Timestamp endTime;
	
	@ApiModelProperty(position = 9, example = ApiDocumentation.Record.DURATION)
	private long duration = 0;

	@ApiModelProperty(position = 10, example = ApiDocumentation.Record.STATE)
	private OrderState state;

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

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
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
				Objects.equals(requester, record.requester) &&
				Objects.equals(startTime, record.startTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, orderId, resourceType, spec, requester, startTime, duration);
	}

	public Record(Long id, String orderId, String resourceType, OrderSpec spec, String requester, Timestamp startTime,
				  Timestamp startDate, Timestamp endTime, Timestamp endDate, long duration, OrderState state) {
		this.id = id;
		this.orderId = orderId;
		this.resourceType = resourceType;
		this.spec = spec;
		this.requester = requester;
		this.startTime = startTime;
		this.startDate = startDate;
		this.endDate = endDate;
		this.endTime = endTime;
		this.duration = duration;
		this.state = state;
	}

	public Record() {}
}
