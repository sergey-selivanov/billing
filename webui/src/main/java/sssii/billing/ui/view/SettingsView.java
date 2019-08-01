package sssii.billing.ui.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import sssii.billing.common.Constants;
import sssii.billing.common.PermissionsException;
import sssii.billing.common.entity.Setting;
import sssii.billing.common.entity.rs.Settings;
import sssii.billing.ui.BillingUIServlet;
import sssii.billing.ui.VaadinSessionHelper;

public class SettingsView extends VerticalLayout implements View
{
    private static final long serialVersionUID = 1L;

    private static Logger log = LoggerFactory.getLogger(SettingsView.class);

    private TextField tfRecipient;
    private TextField tfTerms;
    private TextArea taAddress;

    public SettingsView() {
        //setSizeFull();    // shifts form to bottom

        addComponent(new Label("Settings"));

        FormLayout form = new FormLayout();
        form.setSizeFull();
        //form.setSizeUndefined();
        //form.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        tfRecipient = new TextField("Payment recipient");
        tfRecipient.setWidth(Constants.DEFAULT_FIELD_WIDTH, Unit.PIXELS);
        form.addComponent(tfRecipient);

        tfTerms = new TextField("Payment terms");
        tfTerms.setWidth(Constants.DEFAULT_FIELD_WIDTH, Unit.PIXELS);
        form.addComponent(tfTerms);

        taAddress = new TextArea("Recipient address");
        taAddress.setWidth(Constants.DEFAULT_FIELD_WIDTH, Unit.PIXELS);
        form.addComponent(taAddress);

//        VerticalLayout panel1 = new VerticalLayout();
//        panel1.addComponent(form);
//        addComponent(panel1);



        Button bSave = new Button("Save");
        bSave.addStyleName(ValoTheme.BUTTON_PRIMARY);
        bSave.addClickListener(evt -> {
            saveSettings();
        });


        HorizontalLayout panel = new HorizontalLayout();
        panel.setWidth(Constants.DEFAULT_FIELD_WIDTH, Unit.PIXELS);
        panel.setDefaultComponentAlignment(Alignment.TOP_RIGHT);

        Label filler = new Label(" ");
        panel.addComponent(filler);
        panel.setExpandRatio(filler, 1.0f);

        panel.addComponent(bSave);

        form.addComponent(panel);

        addComponent(form);
    }

    private void setData() {
        Settings s = VaadinSessionHelper.getSettings();

        tfRecipient.setValue(s.getSettings().get(Setting.PAYMENT_RECIPIENT_TITLE).getValue());
        tfTerms.setValue(s.getSettings().get(Setting.PAYMENT_TERMS).getValue());
        taAddress.setValue(s.getSettings().get(Setting.PAYMENT_RECIPIENT_ADDRESS).getValue());

    }

    private void saveSettings() {
        Settings s = VaadinSessionHelper.getSettings();

        s.getSettings().get(Setting.PAYMENT_RECIPIENT_TITLE).setValue(tfRecipient.getValue());
        s.getSettings().get(Setting.PAYMENT_TERMS).setValue(tfTerms.getValue());
        s.getSettings().get(Setting.PAYMENT_RECIPIENT_ADDRESS).setValue(taAddress.getValue());

        try {
            BillingUIServlet.putRestEntity(Setting.REST_PATH + "/all", s);
        }
        catch(PermissionsException e) {
            log.error("insufficient permissions: " + e.getMessage());
            Notification.show("Insufficient permissions", Notification.Type.ERROR_MESSAGE);
        }

        try {
            s = BillingUIServlet.getRestEntity(Setting.REST_PATH + "/all", Settings.class, null);
            VaadinSessionHelper.putSettings(s);
        } catch (PermissionsException e) {
            log.error("insufficient permissions: " + e.getMessage());
            Notification.show("Insufficient permissions", Notification.Type.ERROR_MESSAGE);
        }

    }

    @Override
    public void enter(ViewChangeEvent event) {
        setData();
    }


}
