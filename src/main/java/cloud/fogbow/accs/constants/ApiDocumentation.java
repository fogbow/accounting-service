package cloud.fogbow.accs.constants;

public class ApiDocumentation {

	public static class ApiInfo {
		public static final String API_TITLE = "Fogbow Accounting Service (ACCS) API";
		public static final String API_DESCRIPTION = "This API allows clients to query "
				+ "the consumption of resources by a specific user on a specified time "
				+ "frame. Normal users can only query their own consumption, while "
				+ "admin users can query the consumption of any user.";
	}
	
	public static class Model {
		public static final String ID = "8537af26-72ee-461a-99a9-1e5d59076a98";
		public static final String VCPU = "1";
		public static final String RAM = "1024";
	}
	
	public static class Record {
		public static final String DURATION = "The duration that the resource remained active.";
		public static final String END_DATE = "The date when the resource was made inactive.";
		public static final String END_TIME = "The time when the resource was made inactive.";
		public static final String RECORD_ID = "The record identifier.";
		public static final String ORDER_ID = "The order identifier of the request.";
		public static final String REQUESTER = "The identification of the site who requested the resource.";
		public static final String RESOURCE_TYPE = "The type of the requested resource.";
		public static final String START_TIME = "The time when the order has become active.";
		public static final String START_DATE = "The date when the order has been opened.";
		public static final String STATE = "The state of the resource.";
	}

	public static class ResourceUsage {
		public static final String API = "Provides the use of resources.";
		public static final String GET_OPERATION_FROM_OTHER_USER = "Lists resource usage records for a specific user.";
		public static final String GET_OPERATION_FROM_SYSTEM_USER = "Lists resource usage records for the user who submitted the request.";
		public static final String FINAL_DATE = "The end date of the query.";
		public static final String INITIAL_DATE = "The start date of the query.";
		public static final String PROVIDER = "The identification of the site who provided the resource.";
		public static final String REQUESTER = "The identification of the site who requested the resource.";
		public static final String RESOURCE_TYPE = "The type of the requested resource.";
		public static final String USER_ID = "The user identifier.";
	}

}
