package sssii.billing.common.entity.rs;

import sssii.billing.common.entity.BillingEntity;

public class UserCredentials implements BillingEntity
{

    private String login;
    private String password;
    private Boolean rememberMe;

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public Integer getId() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void setId(Integer id) {
        // TODO Auto-generated method stub

    }
    public Boolean getRememberMe() {
        return rememberMe;
    }
    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

}
