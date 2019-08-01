package sssii.billing.common.entity.rs;

import java.util.Set;

/**
 * Persistence model that represents a user.
 *
 * @author cassiomolin
 */
public class User
//implements Serializable
{
    private String firstName;

    private String lastName;

    private String email;

    private String username;


    private Set<String> authorities;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        User user = (User) o;
//        return username != null ? username.equals(user.username) : user.username == null;
//    }
//
//    @Override
//    public int hashCode() {
//        return username != null ? username.hashCode() : 0;
//    }
}
