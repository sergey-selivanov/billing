package sssii.billing.ui.view;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import sssii.billing.common.Constants;
import sssii.billing.common.PermissionsException;
import sssii.billing.common.entity.Invoice;
import sssii.billing.common.entity.Setting;
import sssii.billing.common.entity.rs.NewInvoice;
import sssii.billing.common.entity.rs.Settings;
import sssii.billing.ui.BillingUIServlet;
import sssii.billing.ui.VaadinSessionHelper;
import sssii.billing.ui.entity.InvoiceDraft;

public class InvoiceDetailsEditView extends VerticalLayout implements View
{
    private static final long serialVersionUID = 1L;
    private Logger log = LoggerFactory.getLogger(InvoiceDetailsEditView.class);

    private TextField invoiceNo;
    private DateField date;
    private TextArea customerAddress;
    private TextField title;
    private TextField payableTo;
    private TextField terms;
    private TextArea mailCheckAddress;

    Binder<Invoice> binder;
    Invoice editedInvoice;

    public InvoiceDetailsEditView() {
        setSizeFull();

        FormLayout form = new FormLayout();
        form.setSizeFull();
        //form.setSizeUndefined();
        //form.setMargin(true);

        binder = new Binder<>(Invoice.class);

        invoiceNo = new TextField("Invoice No.");
        //invoiceNo.setRequiredIndicatorVisible(true);
        invoiceNo.setWidth(Constants.DEFAULT_FIELD_WIDTH, Unit.PIXELS);
        binder.forField(invoiceNo).asRequired().bind("name"); // instead of getName, setName
        form.addComponent(invoiceNo);


        date = new DateField("Invoice date");
        binder.bind(date, "invoiceDate");
        form.addComponent(date);

        customerAddress = new TextArea("To");
        //customerAddress.setReadOnly(true);
        customerAddress.setRows(4);
        customerAddress.setWidth(Constants.DEFAULT_FIELD_WIDTH, Unit.PIXELS);
        binder.bind(customerAddress, "toAddress");
        form.addComponent(customerAddress);

        title = new TextField("Title");
        title.setRequiredIndicatorVisible(true);
        title.setWidth(Constants.DEFAULT_FIELD_WIDTH, Unit.PIXELS);
        binder.bind(title, "description");
        form.addComponent(title);


        payableTo = new TextField("Payable by check to");
        payableTo.setRequiredIndicatorVisible(true);
        payableTo.setWidth(Constants.DEFAULT_FIELD_WIDTH, Unit.PIXELS);
        binder.bind(payableTo, "payableTo");
        form.addComponent(payableTo);

// TODO this is a test
//String val = ((BillingUI)UI.getCurrent()).getSetting("bbb");
//tfPayableTo.setValue(val);

        terms = new TextField("Terms");
        terms.setRequiredIndicatorVisible(true);
        terms.setWidth(Constants.DEFAULT_FIELD_WIDTH, Unit.PIXELS);
        binder.bind(terms, "terms");
        form.addComponent(terms);

        mailCheckAddress = new TextArea("Mail check to");
        mailCheckAddress.setRows(4);
        mailCheckAddress.setWidth(Constants.DEFAULT_FIELD_WIDTH, Unit.PIXELS);
        binder.bind(mailCheckAddress, "mailCheckToAddress");
        form.addComponent(mailCheckAddress);

        addComponent(form);


        Button bFinish = new Button("Complete");
        bFinish.addStyleName(ValoTheme.BUTTON_PRIMARY);
        bFinish.addClickListener(new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    if(binder.validate().isOk()) {
                        saveInvoice();
                        UI.getCurrent().getNavigator().navigateTo(BillingViewType.INVOICE_COMPLETE.getViewName());
                    }
                } catch (PermissionsException e) {
                    log.error("insufficient permissions: " + e.getMessage());
                    Notification.show("Insufficient permissions", Notification.Type.ERROR_MESSAGE);
                }
            }
        });

        addComponent(bFinish);
        setComponentAlignment(bFinish, Alignment.BOTTOM_RIGHT);


    }

    private void setData() {

        InvoiceDraft id = VaadinSessionHelper.getInvoiceDraft();

        editedInvoice = id.getInvoice();

        //binder.readBean(id.getInvoice());
        binder.setBean(editedInvoice);

        date.setValue(LocalDate.now());

        customerAddress.setValue(id.getCustomer().getName() + "\n" + id.getCustomer().getAddress());

        //SimpleDateFormat df = SimpleDateFormat.getDateInstance();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        title.setValue("Invoice from Company covering " + id.getFrom().format(formatter) + " to " + id.getTo().format(formatter) + " against ...");

        Settings s = VaadinSessionHelper.getSettings();

        payableTo.setValue(s.getSettings().get(Setting.PAYMENT_RECIPIENT_TITLE).getValue());
        terms.setValue(s.getSettings().get(Setting.PAYMENT_TERMS).getValue());
        mailCheckAddress.setValue(s.getSettings().get(Setting.PAYMENT_RECIPIENT_ADDRESS).getValue());
    }

    private void saveInvoice() throws PermissionsException {

        InvoiceDraft id = VaadinSessionHelper.getInvoiceDraft();

        Invoice invoice = binder.getBean();
        invoice.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        invoice.setCustomerId(id.getCustomer().getId()); // TODO is customer mandatory?

        // TODO invoicedate: utc dates are in database? may 28 in chooser gets may 27 in database?




//        id.getInvoice().setName(invoiceNo.getValue());
//        id.getInvoice().setDescription(title.getValue());
//
//        id.getInvoice().setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
//
//        // TODO utc dates are in database: may 28 in chooser gets may 27 in database
//        //id.getInvoice().setInvoiceDate(Timestamp.valueOf(date.getValue().atStartOfDay()));
//        id.getInvoice().setInvoiceDate(date.getValue());
//
//        id.getInvoice().setToAddress(customerAddress.getValue());
//        id.getInvoice().setPayableTo(payableTo.getValue());
//        id.getInvoice().setMailCheckToAddress(mailCheckAddress.getValue());
//        id.getInvoice().setTerms(terms.getValue());
//
//        // TODO is customer mandatory?
//        if(id.getCustomer() != null) {
//            id.getInvoice().setCustomerId(id.getCustomer().getId());
//        }
//        else {
//            //id.getInvoice().setCustomerId(555);
//            log.error("invoice without customer " + id.getInvoice().getId());
//        }


        NewInvoice newInv = new NewInvoice();

        //newInv.setInvoice(id.getInvoice());
        newInv.setInvoice(invoice);
        newInv.setItems(id.getInvoiceItems());

        Integer newId = BillingUIServlet.createRestEntity(Invoice.REST_PATH + "/withitems", newInv);
        VaadinSession.getCurrent().setAttribute(Constants.SESSION_NEW_INVOICE_ID, newId);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        setData();
    }
}
