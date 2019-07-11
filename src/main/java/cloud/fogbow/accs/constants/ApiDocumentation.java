package cloud.fogbow.accs.constants;

public class ApiDocumentation {

	public static class ApiInfo {
		public static final String API_TITLE = "Fogbow Accounting Service (ACCS) API";
		public static final String API_DESCRIPTION = "The API provides four main operations, as follows: \ngetVersion: Shows the service's current version.\n" +
				"getPublicKey: Returns the service's public key.\n" +
				"getResourceUsageFromOtherUser: This operation provides the accounting information about an user whose id is received as parameter. Administrators only.\n" +
				"getResourceUsageFromSystemUser: This operation provides the accounting information about the user who is making the request. Allowed to any user.";
	}
	
	public static class Model {
		public static final String ID = "8537af26-72ee-461a-99a9-1e5d59076a98";
		public static final String VCPU = "1";
		public static final String RAM = "1024";
	}
	
	public static class Record {
		public static final String DURATION = "The duration that the resource remained as active.";
		public static final String END_DATE = "The date when the resource got inactive.";
		public static final String END_TIME = "The time when the resource got inactive.";
		public static final String RECORD_ID = "The record identifier.";
		public static final String ORDER_ID = "The order identifier of the request.";
		public static final String REQUESTING_MEMBER = "Registration of the member who requested the resource.";
		public static final String RESOURSE_TYPE = "The requested resource type.";
		public static final String START_TIME = "The time when the order became active.";
		public static final String START_DATE = "The date when the order has been opened.";
		public static final String STATE = "The state of resource.";
	}

	public static class ResourceUsage {
		public static final String API = "Provides the use of resources.";
		public static final String GET_OPERATION_FROM_OTHER_USER = "Lists resource records used by another user.";
		public static final String GET_OPERATION_FROM_SYSTEM_USER = "Lists the records of the resources used by the system user.";
		public static final String FINAL_DATE = "The end date of the query.";
		public static final String INITIAL_DATE = "The start date of the query.";
		public static final String PROVIDING_MEMBER = "The providing member of resources.";
		public static final String REQUESTING_MEMBER = "The requesting member of resources.";
		public static final String RESOURCE_TYPE = "The type of resource in use that you want to consult.";
		public static final String USER_ID = "The user identifier.";
	}

}
