package sssii.billing.ui.view;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import sssii.billing.common.Constants;
import sssii.billing.ui.BillingUIServlet;
import sssii.billing.ui.event.BillingEvent.UserLoginRequestedEvent;
import sssii.billing.ui.event.BillingEventBus;


@SuppressWarnings("serial")
public class LoginView extends VerticalLayout
{
    private CheckBox rememberMe;

    public LoginView() {
        setSizeFull();

        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);


        Boolean isFailed = (Boolean) VaadinSession.getCurrent().getAttribute(Constants.SESSION_LOGIN_FAILED);
        if(isFailed != null) {

            VaadinSession.getCurrent().setAttribute(Constants.SESSION_LOGIN_FAILED, null);

            Notification notification = new Notification("", "Login failed", Notification.Type.ERROR_MESSAGE);
            notification.setPosition(Position.BOTTOM_CENTER);
            notification.show(Page.getCurrent());

//            Notification notification = new Notification("Welcome to Dashboard Demo");
//            notification.setDescription("<span>This application is not real, it only demonstrates an application built with the <a href=\"https://vaadin.com\">Vaadin framework</a>.</span> <span>No username or password is required, just click the <b>Sign In</b> button to continue.</span>");
//            notification.setHtmlContentAllowed(true);
//            notification.setStyleName("tray dark small closable login-help");
//            notification.setPosition(Position.BOTTOM_CENTER);
//            notification.setDelayMsec(20000);
//            notification.show(Page.getCurrent());
        }
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        //loginPanel.addComponent(new CheckBox("Remember me", false));

        rememberMe = new CheckBox("Remember me", true);
        loginPanel.addComponent(rememberMe);

        return loginPanel;
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        final TextField username = new TextField("Username");
        //username.setIcon(FontAwesome.USER);
        username.setIcon(VaadinIcons.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final PasswordField password = new PasswordField("Password");
        //password.setIcon(FontAwesome.LOCK);
        password.setIcon(VaadinIcons.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button("Sign In");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        signin.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {

                // TODO use normal validation
                if(username.getValue().isEmpty() || password.getValue().isEmpty()) {
                    Notification notification = new Notification("", "Enter login and password", Notification.Type.WARNING_MESSAGE);
                    notification.setPosition(Position.BOTTOM_CENTER);
                    notification.show(Page.getCurrent());
                    return;
                }

                BillingEventBus.post(new UserLoginRequestedEvent(username.getValue(), password.getValue(), rememberMe.getValue()));
            }
        });
        return fields;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Welcome");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label("Billing " + BillingUIServlet.getOptions().getProperty("title.branding"));
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;
    }

}
