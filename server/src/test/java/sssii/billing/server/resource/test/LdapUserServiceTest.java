package sssii.billing.server.resource.test;

import org.junit.Test;

import sssii.billing.common.entity.rs.User;
import sssii.billing.server.security.LdapUserService;

public class LdapUserServiceTest {

    @Test
    public void test() {
        LdapUserService us = new LdapUserService();

        User u = us.findByUsername("svs");
        for(String group: u.getAuthorities()) {
            System.out.println("-- " + group);
        }
    }

    @Test
    public void testLogin() {
        LdapUserService us = new LdapUserService();

        User u = us.validateCredentials("svs", "password");
        for(String group: u.getAuthorities()) {
            System.out.println("-- " + group);
        }
    }

}
