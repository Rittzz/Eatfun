package rittz.eatfun.core;

/**
 * Created on 8/4/15.
 */
public class OrderStatus {

    private boolean success;
    private Exception exception;
    private ErrorType errorType;

    public enum ErrorType {
        NETWORK, AUTH_ERROR, UNKNOWN, NONE_AVAILABLE;
    }

    private OrderStatus() {}

    public static OrderStatus success() {
        final OrderStatus result = new OrderStatus();
        result.success = true;
        result.exception = null;
        result.errorType = null;
        return result;
    }

    public static OrderStatus failure(final Exception exception, final ErrorType errorType) {
        final OrderStatus result = new OrderStatus();
        result.success = false;
        result.exception = exception;
        result.errorType = errorType;
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }
}
