package sssii.billing.common.security;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;


/**
 * Service which provides operations for authentication tokens.
 *
 * @author cassiomolin
 */
public class AuthenticationTokenService {


    //private Long validFor = 600l; // seconds
    // TODO hardcoded
    private Long validFor = 60 * 60 *24 * 3l; // 3 days
    //private Long validFor = 60 * 1l; // 1 min
    private Integer refreshLimit = 5;    // times

    private AuthenticationTokenIssuer tokenIssuer = new AuthenticationTokenIssuer();

    private AuthenticationTokenParser tokenParser;// = new AuthenticationTokenParser();

    AuthenticationTokenSettings settings;

    public AuthenticationTokenService(AuthenticationTokenSettings s) {
        settings = s;
        tokenParser = new AuthenticationTokenParser(s);
    }

    /**
     * Issue a token for a user with the given authorities.
     *
     * @param username
     * @param authorities
     * @return
     */
    public String issueToken(String username, Set<String> authorities) {

        String id = generateTokenIdentifier();
        ZonedDateTime issuedDate = ZonedDateTime.now();
        ZonedDateTime expirationDate = calculateExpirationDate(issuedDate);

        AuthenticationTokenDetails authenticationTokenDetails = new AuthenticationTokenDetails.Builder()
                .withId(id)
                .withUsername(username)
                .withAuthorities(authorities)
                .withIssuedDate(issuedDate)
                .withExpirationDate(expirationDate)
                .withRefreshCount(0)
                .withRefreshLimit(refreshLimit)
                .build();

//        AuthenticationTokenSettings settings = new AuthenticationTokenSettings();
//        settings.setAudience("audience");
//        settings.setAuthoritiesClaimName("authoritiesClaimName");
//        settings.setClockSkew(10l);
//        settings.setIssuer("issuer");
//        settings.setRefreshCountClaimName("refreshCountClaimName");
//        settings.setRefreshLimitClaimName("refreshLimitClaimName");
//        settings.setSecret("secret");

        return tokenIssuer.issueToken(authenticationTokenDetails, settings);
    }

    /**
     * Parse and validate the token.
     *
     * @param token
     * @return
     */
    public AuthenticationTokenDetails parseToken(String token) {
        return tokenParser.parseToken(token);
    }

    /**
     * Refresh a token.
     *
     * @param currentTokenDetails
     * @return
     */
    public String refreshToken(AuthenticationTokenDetails currentTokenDetails) {

        if (!currentTokenDetails.isEligibleForRefreshment()) {
            throw new AuthenticationTokenRefreshmentException("This token cannot be refreshed");
        }

        ZonedDateTime issuedDate = ZonedDateTime.now();
        ZonedDateTime expirationDate = calculateExpirationDate(issuedDate);

        AuthenticationTokenDetails newTokenDetails = new AuthenticationTokenDetails.Builder()
                .withId(currentTokenDetails.getId()) // Reuse the same id
                .withUsername(currentTokenDetails.getUsername())
                .withAuthorities(currentTokenDetails.getAuthorities())
                .withIssuedDate(issuedDate)
                .withExpirationDate(expirationDate)
                .withRefreshCount(currentTokenDetails.getRefreshCount() + 1)
                .withRefreshLimit(refreshLimit)
                .build();

        AuthenticationTokenSettings settings = new AuthenticationTokenSettings();

// TODO configure settings
        return tokenIssuer.issueToken(newTokenDetails, settings);
    }

    /**
     * Calculate the expiration date for a token.
     *
     * @param issuedDate
     * @return
     */
    private ZonedDateTime calculateExpirationDate(ZonedDateTime issuedDate) {
        return issuedDate.plusSeconds(validFor);
    }

    /**
     * Generate a token identifier.
     *
     * @return
     */
    private String generateTokenIdentifier() {
        return UUID.randomUUID().toString();
    }
}
