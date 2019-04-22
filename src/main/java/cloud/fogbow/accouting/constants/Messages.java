package cloud.fogbow.accouting.constants;

public class Messages {

    public static class Warn {
        public static final String ERROR_READING_CONF_FILE = "Error trying to read configuration file: %s.";
        public static final String ERROR_CLOSING_CONF_FILE = "Error trying to close configuration file: %s.";
    }

    public static class Exception {
        public static final String TYPE_NOT_ALLOWED_YET = "This operation is not supported for the type %s yet.";
    }
}
