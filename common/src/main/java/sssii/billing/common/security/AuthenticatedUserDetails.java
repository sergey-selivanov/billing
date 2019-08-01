package sssii.billing.common.security;


import java.security.Principal;
import java.util.Collections;
import java.util.Set;

/**
 * {@link Principal} implementation with a set of {@link Authority}.
 *
 * @author cassiomolin
 */
public final class AuthenticatedUserDetails implements Principal {

    private final String username;
    private final Set<String> authorities;

    public AuthenticatedUserDetails(String username, Set<String> authorities) {
        this.username = username;
        this.authorities = Collections.unmodifiableSet(authorities);
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return username;
    }
}
