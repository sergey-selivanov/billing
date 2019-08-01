package sssii.billing.common.security;

/**
 * Thrown if an authentication token cannot be refreshed.
 *
 * @author cassiomolin
 */
public class AuthenticationTokenRefreshmentException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AuthenticationTokenRefreshmentException(String message) {
        super(message);
    }

    public AuthenticationTokenRefreshmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
