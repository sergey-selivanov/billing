package sssii.billing.ui.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.ItemClick;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.ItemClickListener;
import com.vaadin.ui.themes.ValoTheme;

import sssii.billing.common.Constants;
import sssii.billing.common.PermissionsException;
import sssii.billing.common.entity.Customer;
import sssii.billing.common.entity.rs.CustomerProjectCount;
import sssii.billing.ui.BillingUIServlet;

@SuppressWarnings("serial")
public class Customers extends VerticalLayout implements View, ClickListener {

    private static Logger log = LoggerFactory.getLogger(Customers.class);

    private Grid<Customer> grid;
    private List<Customer> data;
    private ListDataProvider<Customer> dataProvider;

    private Map<Integer, CustomerProjectCount> projectCounts;

    private CustomerForm form;
    private Window win;

    public Customers() {

        setSizeFull();

        this.addComponent(new Label("Customers"));

        grid = new Grid<>();
        grid.setSizeFull();
        grid.setCaption("Customers (double-click to edit)");

        //grid.setSelectionMode(SelectionMode.NONE);

        grid.addColumn(Customer::getName)
            .setId("name")
            .setCaption("Name")
            .setMinimumWidthFromContent(false)
            .setExpandRatio(4);

        grid.addColumn(Customer::getDescription)
        .setId("description")
        .setCaption("Description")
        .setMinimumWidthFromContent(false)
        .setExpandRatio(4);


        grid.addComponentColumn(new ValueProvider<Customer, Component>() {

            @Override
            public Component apply(Customer cust) {
                CheckBox cb = new CheckBox();
                cb.setValue(cust.isActive());
                cb.addValueChangeListener(new ValueChangeListener<Boolean>() {

                    @Override
                    public void valueChange(ValueChangeEvent<Boolean> event) {
                        cust.setActive(event.getValue());
                        try {
                            BillingUIServlet.updateRestEntity(Customer.REST_PATH, cust);
                            dataProvider.refreshItem(cust);
                        } catch (PermissionsException e) {
                            log.error("insufficient permissions: " + e.getMessage());
                            Notification.show("Insufficient permissions", Notification.Type.ERROR_MESSAGE);
                        }

                    }
                });
                return cb;
            }
        })
        .setCaption("Active")
        .setExpandRatio(1)
        .setStyleGenerator(item -> "v-align-center");

        grid.addComponentColumn(new ValueProvider<Customer, Component>() {

            @Override
            public Component apply(Customer cust) {

                //if(projectCounts.get(cust.getId()).getProjectCount() == 0 && cust.getOriginalId() == null) {
                if(!projectCounts.containsKey(cust.getId()) && cust.getOriginalId() == null) {

                    Button b = new Button();
                    b.setIcon(VaadinIcons.TRASH);
                    b.addStyleName(ValoTheme.BUTTON_SMALL);

                    b.addClickListener(event -> {

                        ConfirmDialog.show(UI.getCurrent(), "Delete " + cust.getName() + "?", dialog -> {
                            if(dialog.isConfirmed()) {
                                try {
                                    BillingUIServlet.deleteRestEntity(Customer.REST_PATH, cust);

                                    data.remove(cust);
                                    dataProvider.refreshAll();
                                } catch (PermissionsException e) {
                                    log.error("insufficient permissions: " + e.getMessage());
                                    Notification.show("Insufficient permissions", Notification.Type.ERROR_MESSAGE);
                                }

                            }
                        });

                    });

                    return b;
                }
                else {
                    return null;
                }
            }})
        .setCaption("Delete")
        .setExpandRatio(1)
        .setStyleGenerator(item -> "v-align-center");

        grid.addItemClickListener(new ItemClickListener<Customer>() {

            @Override
            public void itemClick(ItemClick<Customer> event) {
                if(event.getMouseEventDetails().isDoubleClick()) {
                    //CustomerForm form = new CustomerForm(CustomersView.this);
                    form = new CustomerForm(Customers.this);
                    form.setCustomer(event.getItem());
                    form.setSizeUndefined();

                    //Window win = new Window();
                    win = new Window();
                    win.setSizeUndefined();
                    win.setContent(form);
                    win.setModal(true);
                    win.center();
                    win.setCaption("Edit Customer");
                    //getUI().addWindow(win);
                    UI.getCurrent().addWindow(win);
                }

            }
        });

        data = new ArrayList<>();
        dataProvider = new ListDataProvider<>(data);
        grid.setDataProvider(dataProvider);

        this.addComponent(grid);
        this.setExpandRatio(grid, 1);

        Button addCustomer = new Button("Add customer");
        addCustomer.addClickListener(clickEvent -> {
            Customer newCustomer = new Customer();
            newCustomer.setName("[edit me]");
            newCustomer.setActive(true);

            form = new CustomerForm(Customers.this);
            form.setCustomer(newCustomer);
            form.setSizeUndefined();

            win = new Window();
            win.setSizeUndefined();
            win.setContent(form);
            win.setModal(true);
            win.center();
            win.setCaption("Add Customer");

            UI.getCurrent().addWindow(win);
        });

        this.addComponent(addCustomer);
    }

    private void setData() {


        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put(Constants.QPARAM_DESC, "originalId");
            List<Customer> items = BillingUIServlet.getRestEntityList(Customer.REST_PATH, Customer.class, params);

            data.clear();
            data.addAll(items);

            dataProvider.refreshAll();

            List<CustomerProjectCount> counts = BillingUIServlet.getRestEntityList("customers/projectcounts", CustomerProjectCount.class);
            projectCounts = counts.stream().collect(Collectors.toMap(CustomerProjectCount::getCustomerId, c -> c));

            if(items.size() == 0) {
                Notification note = new Notification("No data", "No customers found", Notification.Type.WARNING_MESSAGE);
                note.show(Page.getCurrent());
            }

        } catch (PermissionsException e) {
            log.error("insufficient permissions: " + e.getMessage());
            Notification.show("Insufficient permissions", Notification.Type.ERROR_MESSAGE);
        }
    }

    @Override
    public void enter(ViewChangeEvent event) {
        log.debug("enter");
        setData();
    }

    @Override
    public void buttonClick(ClickEvent event) {
        //log.debug("button click: " + event.getButton().getCaption());

        UI.getCurrent().removeWindow(win);

        if(event.getButton().getCaption() == "Save") {

            form.updateCustomer();

            Customer c = form.getCustomer();
            if(c.getId() != null) {
                try {
                    BillingUIServlet.updateRestEntity(Customer.REST_PATH, c);
                    dataProvider.refreshItem(c);
                } catch (PermissionsException e) {
                    log.error("insufficient permissions: " + e.getMessage());
                    Notification.show("Insufficient permissions", Notification.Type.ERROR_MESSAGE);
                }

            }
            else {
                Customer newCust;
                try {
                    newCust = BillingUIServlet.createRestEntity(Customer.REST_PATH, Customer.class, c);
                    data.add(newCust);
                    dataProvider.refreshAll();
                } catch (PermissionsException e) {
                    log.error("insufficient permissions: " + e.getMessage());
                    Notification.show("Insufficient permissions", Notification.Type.ERROR_MESSAGE);

                }
            }
        }
    }
}
