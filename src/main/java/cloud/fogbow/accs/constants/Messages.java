package cloud.fogbow.accs.constants;

public class Messages {

    public static class Warn {
        public static final String ERROR_READING_CONF_FILE = "Error trying to read configuration file: %s.";
        public static final String ERROR_CLOSING_CONF_FILE = "Error trying to close configuration file: %s.";
    }

    public static class Exception {
        public static final String TYPE_NOT_ALLOWED_YET = "This operation is not supported for the type %s yet.";
        public static final String UNAUTHORIZED_OPERATION = "The user is not authorized to do this operation";
        public static final String BILLING_PREDICTIONS = "Accounting predictions are not allowed";
        public static final String START_TIME_GREATER_THAN_END_TIME = "Begin time must not be greater than end time";
        public static final String GENERIC_EXCEPTION = "Operation returned error: %s";
    }

	public static class Info {
		public static final String RECEIVING_GET_VERSION_REQUEST = "Get request for version received.";
	}
}
