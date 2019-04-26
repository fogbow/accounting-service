package cloud.fogbow.accounting.exceptions;

public class InvalidIntervalException extends RuntimeException {

    public InvalidIntervalException() {
        super();
    }

    public  InvalidIntervalException(String msg) {
        super(msg);
    }
}
