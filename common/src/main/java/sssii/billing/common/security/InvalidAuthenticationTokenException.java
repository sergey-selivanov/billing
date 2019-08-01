package sssii.billing.common.security;


/**
 * Thrown if an authentication token is invalid.
 *
 * @author cassiomolin
 */
public class InvalidAuthenticationTokenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidAuthenticationTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
