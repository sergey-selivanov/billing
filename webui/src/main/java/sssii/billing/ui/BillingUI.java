package sssii.billing.ui;

import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import sssii.billing.common.Constants;
import sssii.billing.common.PermissionsException;
import sssii.billing.common.entity.Setting;
import sssii.billing.common.entity.rs.Settings;
import sssii.billing.common.entity.rs.UserCredentials;
import sssii.billing.ui.event.BillingEvent.UserLoggedOutEvent;
import sssii.billing.ui.event.BillingEvent.UserLoginRequestedEvent;
import sssii.billing.ui.event.BillingEventBus;
import sssii.billing.ui.view.LoginView;
import sssii.billing.ui.view.MainView;

@SuppressWarnings("serial")
@Theme("billing")
public class BillingUI extends UI
{
    private final BillingEventBus billingEventbus = new BillingEventBus();

    private Logger log = LoggerFactory.getLogger(BillingUI.class);

    // https://github.com/vaadin/addressbook/blob/master/src/main/java/com/vaadin/tutorial/addressbook/AddressbookUI.java
//
//    @WebServlet(urlPatterns = "/*")
//    @VaadinServletConfiguration(ui = BillingUI.class, productionMode = false)
//    public static class BillingUIServlet extends VaadinServlet {
//    }


    @Override
    protected void init(VaadinRequest request) {

        getPage().setTitle("Billing");

        BillingEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();
    }

    private boolean loginByToken() {

        log.debug("login by token...");

        String token = "";

        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        for(Cookie c: cookies) {
//            log.debug("cookie: " + c.getName()
//            + " " + c.getPath()
//            + " " + c.getValue());

            // TODO hardcoded name
            if(c.getName().equals("vaadin-billing-cookie")) { // path is null
                log.debug("here's our cookie!");
                token = c.getValue();
            }
        }

        if(token.isEmpty()) {
            log.debug("no cookie, skip login by token");
            return false;
        }

        boolean success = BillingUIServlet.loginByToken("auth/token", token);

        if(success) {

            //VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
            VaadinSession.getCurrent().setAttribute("logged-in", true);

            log.debug("obtaining settings");
            Settings s;
            try {
                s = BillingUIServlet.getRestEntity(Setting.REST_PATH + "/all", Settings.class, null);
                VaadinSessionHelper.putSettings(s);
            } catch (PermissionsException e) {
                log.error("insufficient permissions: " + e.getMessage());
                Notification.show("Insufficient permissions", Notification.Type.ERROR_MESSAGE);
            }

            requestDataSync();
            return true;
        }
        else {
            return false;
        }


    }

    private void updateContent(){
        //User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        //if (user != null && "admin".equals(user.getRole())) {

        Boolean isLoggedIn = (Boolean) VaadinSession.getCurrent().getAttribute("logged-in");

        if(isLoggedIn != null && isLoggedIn){
            // Authenticated user
            setContent(new MainView());
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        } else {

            if(loginByToken()) {
                setContent(new MainView());
                removeStyleName("loginview");
                getNavigator().navigateTo(getNavigator().getState());
            }
            else {

                setContent(new LoginView());
                addStyleName("loginview");
            }
        }
    }

    public static BillingEventBus getBillingEventbus() {
        return ((BillingUI)getCurrent()).billingEventbus;
    }

    @Subscribe
    public void userLoginRequested(final UserLoginRequestedEvent event) {

        UserCredentials cred = new UserCredentials();
        cred.setLogin(event.getUserName());
        cred.setPassword(event.getPassword());
        cred.setRememberMe(event.getRememberMe());

        boolean success = BillingUIServlet.login("auth", cred);

        if(success) {

            // set vaadin cookie
            Cookie myCookie = new Cookie("vaadin-billing-cookie", VaadinSessionHelper.getString(Constants.SESSION_TOKEN));
            myCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
            log.debug("add cookie: " + myCookie.getName()
                + " " + myCookie.getPath()
                + " " + myCookie.getValue());

            if(cred.getRememberMe()) {
                log.debug("set cookie for 3 days");
                myCookie.setMaxAge(60 * 60 * 24 * 3);    // 3 days
            }
            else {
                log.debug("set cookie but do not persist");
                myCookie.setMaxAge(-1); // do not store and forget when browser exits, chromium seems to ignore this, cookie stays
            }
            VaadinService.getCurrentResponse().addCookie(myCookie);

            VaadinSession.getCurrent().setAttribute("logged-in", true);

            log.debug("obtaining settings");
            Settings s;
            try {
                s = BillingUIServlet.getRestEntity(Setting.REST_PATH + "/all", Settings.class, null);
                VaadinSessionHelper.putSettings(s);
            } catch (PermissionsException e) {
                log.error("insufficient permissions: " + e.getMessage());
                Notification.show("Insufficient permissions", Notification.Type.ERROR_MESSAGE);
            }


            requestDataSync();
        }
        else {
            VaadinSession.getCurrent().setAttribute(Constants.SESSION_LOGIN_FAILED, true);
        }

        updateContent();
    }

    @Subscribe
    public void userLoggedOut(final UserLoggedOutEvent event) {
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the login screen. Do notice the this doesn't
        // invalidate the current HttpSession.

        log.debug("delete cookie on logout");
        // delete cookie on logout
        Cookie myCookie = new Cookie("vaadin-billing-cookie", "invalid");
        myCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
        myCookie.setMaxAge(0); // delete cookie
        VaadinService.getCurrentResponse().addCookie(myCookie);

//        myCookie = new Cookie("marker-billing-cookie", "set-on-logout");
//        myCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
//        myCookie.setMaxAge(60 * 60 * 24 * 3);
//        VaadinService.getCurrentResponse().addCookie(myCookie);

        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    private void requestDataSync() {
        try {
            BillingUIServlet.invokeRestGet("server/sync");
        } catch (PermissionsException e) {
            log.error("insufficient permissions: " + e.getMessage());
            Notification.show("Insufficient permissions", Notification.Type.ERROR_MESSAGE);
        }

        // TODO sync manually instead
    }

}
