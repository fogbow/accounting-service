package cloud.fogbow.accs.core.exceptions;

public class TypeNotAllowedException extends RuntimeException {

    public TypeNotAllowedException() {
        super();
    }

    public TypeNotAllowedException(String msg) {
        super(msg);
    }
}
