package accouting.exceptions;

public class InvalidIntervalException extends RuntimeException {

    public InvalidIntervalException() {
        super();
    }

    public  InvalidIntervalException(String msg) {
        super(msg);
    }
}
