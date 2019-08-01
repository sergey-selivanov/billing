package sssii.billing.ui.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import sssii.billing.common.Constants;
import sssii.billing.common.entity.Customer;

public class CustomerForm extends FormLayout {

    private static final long serialVersionUID = 1L;

    private static Logger log = LoggerFactory.getLogger(CustomerForm.class);

    Binder<Customer> binder;
    Customer customer;

    public CustomerForm(ClickListener buttonListener) {
        TextField tfName = new TextField("Name");
        tfName.setRequiredIndicatorVisible(true);
        tfName.setWidth(Constants.DEFAULT_FIELD_WIDTH, Unit.PIXELS);    // TODO why pixels not percent?
        this.addComponent(tfName);

        TextArea taAddress = new TextArea("Address");
        taAddress.setRows(4);
        taAddress.setWidth(Constants.DEFAULT_FIELD_WIDTH, Unit.PIXELS);
        taAddress.setRequiredIndicatorVisible(true);

        this.addComponent(taAddress);

        TextArea taDescription = new TextArea("Description");
        taDescription.setWidth(Constants.DEFAULT_FIELD_WIDTH, Unit.PIXELS);
        this.addComponent(taDescription);
        taDescription.setRows(2);

        HorizontalLayout panel = new HorizontalLayout();
        //panel.setWidth("500px");
        panel.setWidth(Constants.DEFAULT_FIELD_WIDTH, Unit.PIXELS);

        panel.setDefaultComponentAlignment(Alignment.TOP_RIGHT);

        Label filler = new Label(" ");
        panel.addComponent(filler);
        panel.setExpandRatio(filler, 1.0f);

        Button bSave = new Button("Save");
        bSave.addClickListener(buttonListener);

        panel.addComponent(bSave);

        Button bCancel = new Button("Cancel");
        bCancel.addClickListener(buttonListener);
        panel.addComponent(bCancel);

        this.addComponent(panel);

        //this.setMargin(new MarginInfo(true));
        this.setMargin(true);

        binder = new Binder<>();
        binder.bind(tfName, Customer::getName, Customer::setName);
        binder.bind(taAddress, Customer::getAddress, Customer::setAddress);
        binder.bind(taDescription, Customer::getDescription, Customer::setDescription);

    }

    public void setCustomer(Customer c) {

        //binder.setBean(c);

        customer = c;
        binder.readBean(customer);
    }

    public Customer getCustomer() {

        // TODO binder.writeBean(customer); here instead of click?
        log.debug("getCustomer");
        //return binder.getBean();
        return customer;
    }

    public void updateCustomer() {

        try {
            log.debug("updating customer");
            binder.writeBean(customer);
        } catch (ValidationException ex) {
            log.error("validation failed", ex);
        }
    }
}
