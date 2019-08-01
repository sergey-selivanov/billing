package sssii.billing.ui;

import com.vaadin.server.VaadinSession;

import sssii.billing.common.Constants;
import sssii.billing.common.entity.rs.Settings;
import sssii.billing.common.entity.rs.User;
import sssii.billing.common.security.AuthenticationTokenDetails;
import sssii.billing.ui.entity.InvoiceDraft;

public abstract class VaadinSessionHelper {

    public static void newInvoiceDraft() {
        VaadinSession.getCurrent().setAttribute(InvoiceDraft.class, new InvoiceDraft());
    }

    public static InvoiceDraft getInvoiceDraft() {
        InvoiceDraft i = VaadinSession.getCurrent().getAttribute(InvoiceDraft.class);
        if(i == null) {
            i = new InvoiceDraft();
            VaadinSession.getCurrent().setAttribute(InvoiceDraft.class, i);
        }

        return i;
    }

    public static void putSettings(Settings s) {
        VaadinSession.getCurrent().setAttribute(Settings.class, s);
    }

    public static Settings getSettings() {
        return VaadinSession.getCurrent().getAttribute(Settings.class);
    }

    public static User getUser() {
        return VaadinSession.getCurrent().getAttribute(User.class);
    }

    public static void putUser(User u) {
        VaadinSession.getCurrent().setAttribute(User.class, u);
    }

    public static void setAttr(String name, Object value) {
        VaadinSession.getCurrent().setAttribute(name, value);
    }

    public static Object getAttr(String name) {
        return VaadinSession.getCurrent().getAttribute(name);
    }

    public static void setString(String name, String value) {
        VaadinSession.getCurrent().setAttribute(name, value);
    }

    public static String getString(String name) {
        return (String)VaadinSession.getCurrent().getAttribute(name);
    }

    public static String getTokenHeader() {
        return "Bearer " + (String)VaadinSession.getCurrent().getAttribute(Constants.SESSION_TOKEN);
    }

    public static void putTokenDetails(AuthenticationTokenDetails details) {
        VaadinSession.getCurrent().setAttribute(AuthenticationTokenDetails.class, details);
    }

    public static AuthenticationTokenDetails getTokenDetails() {
        return VaadinSession.getCurrent().getAttribute(AuthenticationTokenDetails.class);
    }
}
