package learning.web.product;

public class InvalidLimitException extends RuntimeException {
    public InvalidLimitException() {
        super("limit must be between 1 and 100");
    }
}
