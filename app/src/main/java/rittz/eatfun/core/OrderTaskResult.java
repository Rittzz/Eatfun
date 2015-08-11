package rittz.eatfun.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 8/4/15.
 */
public class OrderTaskResult {

    private List<OrderStatus> results = new ArrayList<>();

    private Exception exception;
    private ErrorType errorType;

    public enum ErrorType {
        NETWORK, AUTH_ERROR, UNKNOWN;
    }

    public OrderTaskResult() {}

    public List<OrderStatus> getResults() {
        return results;
    }

    public void setResults(List<OrderStatus> results) {
        this.results = results;
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
