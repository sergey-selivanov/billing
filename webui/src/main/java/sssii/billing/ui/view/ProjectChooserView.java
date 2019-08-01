package sssii.billing.ui.view;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import sssii.billing.common.Constants;
import sssii.billing.common.PermissionsException;
import sssii.billing.common.entity.Customer;
import sssii.billing.common.entity.Project;
import sssii.billing.ui.BillingUIServlet;
import sssii.billing.ui.VaadinSessionHelper;
import sssii.billing.ui.entity.InvoiceDraft;

@SuppressWarnings("serial")
public class ProjectChooserView extends VerticalLayout implements View
{
    private static Logger log = LoggerFactory.getLogger(ProjectChooserView.class);

    private ComboBox<Customer> customer;
    private ComboBox<Project> project;

    private DateField from;
    private DateField to;

    //private ListDataProvider<Customer> dpCustomer;
    private ListDataProvider<Project> dpProject;

    public ProjectChooserView(){

        //setSizeUndefined();
        setSizeFull();

        //this.addComponent(new Label("Choose project and date range"));

        FormLayout form = new FormLayout();

        //List<String> data = IntStream.range(0, 16).mapToObj(i -> "Customer company " + i).collect(Collectors.toList());
        //NativeSelect<String> org = new NativeSelect<>("Customer", data);
        //ComboBox<String> org = new ComboBox<>("Customer", data);
        //ComboBox<Customer> customer = new ComboBox<>("Customer", getCustomers());
        //ComboBox<Customer> customer = new ComboBox<>("Customer");
        customer = new ComboBox<>("Customer");
        //dpCustomer = DataProvider.ofCollection(getCustomers());
        //customer.setDataProvider(dpCustomer);



        customer.setEmptySelectionAllowed(false);
        //org.setSelectedItem(data.get(0));
        //org.setEmptySelectionCaption("choose customer");
        //org.setRequiredIndicatorVisible(true);
        //org.setComponentError(new UserError("Oops"));
        customer.setPlaceholder("No customer selected");

        customer.setWidth(Constants.DEFAULT_FIELD_WIDTH, Unit.PIXELS);

//        org.addSelectionListener(new SingleSelectionListener<String>(){
//            @Override
//            public void selectionChange(SingleSelectionEvent<String> event) {
//                log.debug("org selection: " + event.getSelectedItem());
//            }});

        //ListDataProvider<Project> dpProject = DataProvider.ofCollection(getProjects());
        //dpProject = DataProvider.ofCollection(getProjects());
        //ComboBox<Project> project = new ComboBox<>("Project");
        project = new ComboBox<>("Project");

        customer.addSelectionListener(new SingleSelectionListener<Customer>() {

            @Override
            public void selectionChange(SingleSelectionEvent<Customer> event) {

                if(event.getSelectedItem().isPresent()) {
                    log.debug("org selection: " + event.getSelectedItem().get().getName());
                    Integer customerId = event.getSelectedItem().get().getId();

                    //dpProject.setFilter(Project::getCustomerId, customerId ->{ return true;});
                    dpProject.setFilter(Project::getCustomerId, new SerializablePredicate<Integer>() {
                        @Override
                        public boolean test(Integer t) {
                            // TODO NPE bil-45
                            if(t == null) {
                                log.debug("t is null, project without customer?");
                                return false;
                            }
                            return t.equals(customerId);
                        }
                    });
                }
                else {
                    dpProject.clearFilters();
                }

                project.setSelectedItem(null);
            }
        });

        customer.setItemCaptionGenerator(Customer::getName);

        form.addComponent(customer);


        //project.setDataProvider(dpProject);

        project.setWidth(Constants.DEFAULT_FIELD_WIDTH, Unit.PIXELS);
        project.setPlaceholder("No project selected");
        project.setItemCaptionGenerator(Project::getName);
        project.setEmptySelectionAllowed(false);

        form.addComponent(project);


        HorizontalLayout horiz = new HorizontalLayout();
        Panel panel = new Panel(horiz);

        horiz.setHeight(34, Unit.PIXELS);
        //horiz.setWidth(499, Unit.PIXELS);


        panel.setHeight(35, Unit.PIXELS);
        //panel.setWidth(500.0f, Unit.PIXELS);
//        panel.setSizeFull();
        panel.addStyleName(ValoTheme.PANEL_BORDERLESS);


        Label projectNote = new Label("");
        horiz.addComponent(projectNote);

        form.addComponent(panel);


//        project.addSelectionListener(new SingleSelectionListener<Project>() {
//
//            @Override
//            public void selectionChange(SingleSelectionEvent<Project> event) {
//                if(event.getSelectedItem().isPresent()) {
//                    //projectNote.setCaption("project: " + event.getSelectedItem().get().getName());
//                    projectNote.setCaption("Previous invoice in this project included work up to 12/12/2017. " +
//                    "Last reported work: 11/11/2017");
//                }
//                else {
//                    projectNote.setCaption("");
//                }
//
//            }
//        });


        project.addSelectionListener(event -> {
            if(event.getSelectedItem().isPresent()) {

                try {

                    String lastInvoicedDate = BillingUIServlet.getRestEntity(Project.REST_PATH + "/" + event.getSelectedItem().get().getId() + "/lastinvoiceddate", String.class);
// TODO must accept our id, not original
                    String lastReportedDate = BillingUIServlet.getRestEntity(Project.REST_PATH + "/" + event.getSelectedItem().get().getOriginalId() + "/lastworkreporteddate", String.class);

log.debug("date: " + lastInvoicedDate);
log.debug("date 2: " + lastReportedDate);	// TODO bil-46 may get error string, handle this

                    StringBuilder sb = new StringBuilder();
                    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
                    if(lastInvoicedDate != null) {
                        LocalDate ld = LocalDate.parse(lastInvoicedDate);
                        sb.append("Previous invoice included work up to " + ld.format(formatter) + ".");
                        from.setValue(ld.plusDays(1));
                    }
                    else {
                        //LocalDate ld = LocalDate.from(event.getSelectedItem().get().getCreatedDate().toInstant());
                        LocalDate ld = event.getSelectedItem().get().getCreatedDate().toLocalDateTime().toLocalDate();
                        sb.append("No invoices yet, project created " + ld.format(formatter) + ".");
                        from.setValue(ld);
                    }

                    if(lastReportedDate != null) {
                        try {
                            LocalDate ld = LocalDate.parse(lastReportedDate);
                            sb.append(" Last reported work: " + ld.format(formatter) + ".");
                        }
                        catch(DateTimeParseException ex) {
                            log.error("server returned bad date string");
                            Notification.show("Failed to get data, examine logs", Notification.Type.ERROR_MESSAGE);
                        }
                    }
                    else {
                        sb.append(" No reported work.");
                    }

                    projectNote.setCaption(sb.toString()); // NOTE we set caption, not value. Caption has smaller text.

                }
                catch(PermissionsException e) {
                    log.error("insufficient permissions: " + e.getMessage());
                    Notification.show("Insufficient permissions", Notification.Type.ERROR_MESSAGE);
                }

            }
            else {
                projectNote.setCaption("");
            }
        });


        horiz = new HorizontalLayout();
        panel = new Panel(horiz);
        //panel.setWidth(500.0f, Unit.PIXELS);
        panel.setSizeUndefined();
        panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        //horiz.setMargin(false);
        //horiz.setSizeUndefined();
        //horiz.setStyleName("");

        from = new DateField("From");
        from.setValue(LocalDate.now().minusMonths(1));

        horiz.addComponent(from);

        to = new DateField("To");
        to.setValue(LocalDate.now());

        horiz.addComponent(to);


        form.addComponent(panel);


        Button next = new Button("Next >");
        next.addStyleName(ValoTheme.BUTTON_PRIMARY);
        next.setClickShortcut(KeyCode.ENTER);
        next.addClickListener(new ClickListener(){
            @Override
            public void buttonClick(ClickEvent event) {

                if(!project.getSelectedItem().isPresent() || !customer.getSelectedItem().isPresent()) {
                    Notification.show("Choose a customer and a project", Notification.Type.WARNING_MESSAGE);
                    return;
                }

                String targetView = BillingViewType.INVOICE_ITEMS_CHOOSER.getViewName();
                log.debug("target: " + targetView);

                InvoiceDraft id = VaadinSessionHelper.getInvoiceDraft();

                id.getInvoice().setProjectId(project.getSelectedItem().get().getId());

                id.setFrom(from.getValue());    // TODO unneeded?
                id.setTo(to.getValue());
                id.getInvoice().setWorkFromDate(Timestamp.valueOf(id.getFrom().atStartOfDay()));
                id.getInvoice().setWorkToDate(Timestamp.valueOf(id.getTo().atStartOfDay()));

                id.setCustomer(customer.getSelectedItem().get());

//                if(customer.getSelectedItem().isPresent()) {
//                    id.setCustomer(customer.getSelectedItem().get());
//                }
//                else {
//                    id.setCustomer(null);
//                }

                UI.getCurrent().getNavigator().navigateTo(targetView);
            }});
        //BillingEventBus.register(next);    // is this needed?


        form.addComponent(next);
        //form.setComponentAlignment(next, Alignment.BOTTOM_RIGHT);


        form.setSizeFull();
        addComponent(form);

        //org.focus();
    }

    @Override
    public void enter(ViewChangeEvent event) {
        setData();
    }

    private void setData() {
        // re-read everything: might be enabled/disabled

        try {
            // https://github.com/vaadin/framework/issues/9047
            customer.setSelectedItem(null);
            customer.setItems(getCustomers());

            dpProject = DataProvider.ofCollection(getProjects());
            project.setDataProvider(dpProject);
        }
        catch(PermissionsException e) {
            log.error("insufficient permissions: " + e.getMessage());
            Notification.show("Insufficient permissions", Notification.Type.ERROR_MESSAGE);
        }
    }

    private List<Customer> getCustomers() throws PermissionsException{
        //Properties params = new Properties();
        HashMap<String, Object> params = new HashMap<>();
        params.put(Constants.QPARAM_ISACTIVE, "true");
        //params.put(Constants.QPARAM_ASC, "name");
        return BillingUIServlet.<Customer>getRestEntityList(Customer.REST_PATH, Customer.class, params);
    }

    private List<Project> getProjects() throws PermissionsException{
        HashMap<String, Object> params = new HashMap<>();
        params.put(Constants.QPARAM_ISACTIVE, "true");
        params.put(Constants.QPARAM_DESC, "originalId");
        return BillingUIServlet.<Project>getRestEntityList(Project.REST_PATH, Project.class, params);
    }
}

