package sssii.billing.common.security;

import sssii.billing.common.entity.rs.User;

/**
 * Service that provides operations for {@link User}s.
 *
 * @author cassiomolin
 */
public interface UserService {

    public User findByUsername(String username);
    public User validateCredentials(String username, String password);

}
