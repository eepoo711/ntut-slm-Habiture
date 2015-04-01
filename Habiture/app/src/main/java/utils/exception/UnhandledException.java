package utils.exception;



public class UnhandledException extends RuntimeException{

    public UnhandledException() {
        super();
    }

    public UnhandledException(Throwable cause) {
        super(cause);
    }

    public UnhandledException(String detailMessage) {
        super(detailMessage);
    }

    public UnhandledException(String detailMessage, Throwable cause) {
        super(detailMessage, cause);
    }

}
