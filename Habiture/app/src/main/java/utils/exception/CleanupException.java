package utils.exception;



public class CleanupException extends RuntimeException{

    public CleanupException() {
        super();
    }

    public CleanupException(Throwable cause) {
        super(cause);
    }

    public CleanupException(String detailMessage) {
        super(detailMessage);
    }

    public CleanupException(String detailMessage, Throwable cause) {
        super(detailMessage, cause);
    }

}
