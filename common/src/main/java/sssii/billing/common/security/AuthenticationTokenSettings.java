package sssii.billing.common.security;


/**
 * Settings for signing and verifying JWT tokens.
 *
 * @author cassiomolin
 */
public class AuthenticationTokenSettings {

    /**
     * Secret for signing and verifying the token signature.
     */
    private String secret;

    /**
     * Allowed clock skew for verifying the token signature (in seconds).
     */
    private Long clockSkew;

    /**
     * Identifies the recipients that the JWT token is intended for.
     */
    private String audience;

    /**
     * Identifies the JWT token issuer.
     */
    private String issuer;

    /**
     * JWT claim for the authorities.
     */
    private String authoritiesClaimName = "authorities";

    /**
     * JWT claim for the token refreshment count.
     */
    private String refreshCountClaimName = "refreshCount";

    /**
     * JWT claim for the maximum times that a token can be refreshed.
     */
    private String refreshLimitClaimName = "refreshLimit";

    public String getSecret() {
        return secret;
    }

    public Long getClockSkew() {
        return clockSkew;
    }

    public String getAudience() {
        return audience;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getAuthoritiesClaimName() {
        return authoritiesClaimName;
    }

    public String getRefreshCountClaimName() {
        return refreshCountClaimName;
    }

    public String getRefreshLimitClaimName() {
        return refreshLimitClaimName;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setClockSkew(Long clockSkew) {
        this.clockSkew = clockSkew;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setAuthoritiesClaimName(String authoritiesClaimName) {
        this.authoritiesClaimName = authoritiesClaimName;
    }

    public void setRefreshCountClaimName(String refreshCountClaimName) {
        this.refreshCountClaimName = refreshCountClaimName;
    }

    public void setRefreshLimitClaimName(String refreshLimitClaimName) {
        this.refreshLimitClaimName = refreshLimitClaimName;
    }
}
