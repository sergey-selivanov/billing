package sssii.billing.ui.view;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Binder;
import com.vaadin.data.Binder.Binding;
import com.vaadin.data.Converter;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.EditorSaveEvent;
import com.vaadin.ui.components.grid.EditorSaveListener;
import com.vaadin.ui.renderers.NumberRenderer;

import sssii.billing.common.Constants;
import sssii.billing.common.PermissionsException;
import sssii.billing.common.entity.Customer;
import sssii.billing.common.entity.Project;
import sssii.billing.ui.BillingUIServlet;


public class Projects extends VerticalLayout implements View
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(Projects.class);

    private Grid<Project> grid;
    private List<Project> data;
    private ListDataProvider<Project> dataProvider;

    private ComboBox<Customer> filter;

    private List<Customer> activeCustomers;
    private List<Customer> allCustomers;
    private Map<Integer, Customer> mapAllCustomers;

    private ComboBox<Customer> customerEditor;

    private Integer editedProjectCustomerId;

    public Projects() {
        setSizeFull();

        this.addComponent(new Label("Projects"));


        filter = new ComboBox<>("Customer");
        filter.setEmptySelectionAllowed(false);
        filter.setPlaceholder("No customer selected");
        filter.setItemCaptionGenerator(Customer::getName);
        filter.setWidth(Constants.DEFAULT_FIELD_WIDTH, Unit.PIXELS);
        filter.addSelectionListener(new SingleSelectionListener<Customer>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectionChange(SingleSelectionEvent<Customer> event) {

                if(event.getSelectedItem().isPresent()) {
                    log.debug("selection: " + event.getSelectedItem().get().getName());
                    Integer customerId = event.getSelectedItem().get().getId();

                    dataProvider.setFilter(Project::getCustomerId, new SerializablePredicate<Integer>() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public boolean test(Integer t) {
                            if(t == null && customerId == null) {
                                return true;
                            }
                            else if(t != null && customerId != null) {
                                return t.equals(customerId);
                            }
                            else {
                                return false;
                            }
                        }
                    });
/*
                    if(customerId == NO_CUSTOMER_ID) {
                        dataProvider.setFilter(Project::getCustomerId, new SerializablePredicate<Integer>() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public boolean test(Integer t) {
                                return (t == null);    // TODO verify if this is correct
                            }
                        });
                    }
                    else {

                        dataProvider.setFilter(Project::getCustomerId, new SerializablePredicate<Integer>() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public boolean test(Integer t) {
                                if(t != null) {
                                    return t.equals(customerId);
                                }
                                else {
                                    return false;
                                }
                            }
                        });
                    }
*/
                }
                else {
                    dataProvider.clearFilters();
                }
            }
        });
        this.addComponent(filter);

        grid = new Grid<>();
        grid.setSizeFull();
        grid.setCaption("Projects (double-click to edit)");


        Binder<Project> gridBinder = grid.getEditor().getBinder();

        grid.addColumn(Project::getName)
        .setId("name")
        .setCaption("Name")
        .setMinimumWidthFromContent(false)
        .setExpandRatio(4)
        ;

        customerEditor = new ComboBox<>();
        customerEditor.setItemCaptionGenerator(Customer::getName);
        customerEditor.setEmptySelectionAllowed(false);
        Binding<Project, Integer> customerBinding = gridBinder.forField(customerEditor)
                .withConverter(new Converter<Customer, Integer>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Result<Integer> convertToModel(Customer value, ValueContext context) {
                        if(value != null) {
                            log.debug("convertToModel " + value.getName());
                            return Result.ok(value.getId());
                        }
                        else {
                            return Result.ok(null);
                        }
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public Customer convertToPresentation(Integer value, ValueContext context) {

                        log.debug("convertToPresentation " + value);
                        ComboBox<Customer> combo = (ComboBox<Customer>)context.getComponent().get();
                        combo.setSelectedItem(mapAllCustomers.get(value));
                        if(combo.getSelectedItem().isPresent()) {
                            return combo.getSelectedItem().get();
                        }
                        else {
                            return mapAllCustomers.get(value);
                        }
                    }

                })
                .bind(Project::getCustomerId, Project::setCustomerId);


        grid.addColumn(new ValueProvider<Project, String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String apply(Project source) {
                if(source.getCustomerId() == null) {
                    return "<not assigned>";
                }
                else {
                    Customer c = mapAllCustomers.get(source.getCustomerId());
                    if(c != null) {
                        return c.getName();
                    }
                    else {
                        log.debug("null customer for id " + source.getCustomerId());
                        return "no customer";
                    }
                }
            }
        })
        .setId("customer")
        .setCaption("Customer")
        .setMinimumWidthFromContent(false)
        .setExpandRatio(4)
        .setEditorBinding(customerBinding)
        .setEditable(true)
        ;


        TextField rateEditor = new TextField();
        Binding<Project, BigDecimal> rateBinding = gridBinder.forField(rateEditor)
                .withConverter(new StringToBigDecimalConverter("Must enter a number"))
                .bind(Project::getDefaultRate, Project::setDefaultRate);

        grid.addColumn(Project::getDefaultRate)
        .setId("defaultrate")
        .setCaption("Default Rate")
        .setMinimumWidthFromContent(false)
        .setExpandRatio(2)
        .setRenderer(new NumberRenderer(DecimalFormat.getCurrencyInstance()))
        .setStyleGenerator(item -> "v-align-right")
        .setEditorBinding(rateBinding)
        .setEditable(true)
        ;


        grid.addComponentColumn(new ValueProvider<Project, Component>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component apply(Project proj) {
                CheckBox cb = new CheckBox();
                cb.setValue(proj.isActive());
                cb.addValueChangeListener(new ValueChangeListener<Boolean>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void valueChange(ValueChangeEvent<Boolean> event) {
                        proj.setActive(event.getValue());
                        try {
                            BillingUIServlet.updateRestEntity(Project.REST_PATH, proj);
                            dataProvider.refreshItem(proj);
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

        grid.getEditor().addOpenListener(event ->{
            editedProjectCustomerId = event.getBean().getCustomerId();
            log.debug("editing " + editedProjectCustomerId);
        });

        grid.getEditor().addSaveListener(new EditorSaveListener<Project>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onEditorSave(EditorSaveEvent<Project> event) {
                Project p = event.getBean();
//                log.debug("== editor save: " + p.getCustomerId() + " " + p.getDefaultRate());

                if(editedProjectCustomerId == null && p.getCustomerId() != null) {
                    log.debug("customer assigned, set project active");
                    p.setActive(true);
                }

                try {
                    BillingUIServlet.updateRestEntity(Project.REST_PATH, p);
                    dataProvider.refreshItem(p);
                } catch (PermissionsException e) {
                    log.error("insufficient permissions: " + e.getMessage());
                    Notification.show("Insufficient permissions", Notification.Type.ERROR_MESSAGE);
                }
            }
        });
        grid.getEditor().setEnabled(true);


        data = new ArrayList<>();
        dataProvider = new ListDataProvider<>(data);
        grid.setDataProvider(dataProvider);

        this.addComponent(grid);
        this.setExpandRatio(grid, 1);
    }

    private void setData() throws PermissionsException {
        HashMap<String, Object> params = new HashMap<>();
        params.put(Constants.QPARAM_ISACTIVE, "true");
        //activeCustomers = BillingUIServlet.<Customer>getRestEntityList("customers", Customer.class, params);
        // TODO all customers only needed because of initial unfiltered list of all projects
        allCustomers = BillingUIServlet.<Customer>getRestEntityList(Customer.REST_PATH, Customer.class);
        activeCustomers = allCustomers.stream().filter(customer -> customer.isActive()).collect(Collectors.toList());
        mapAllCustomers = allCustomers.stream().collect(Collectors.toMap(Customer::getId, item -> item));

        List<Customer> custFilter = new ArrayList<>();

        Customer absent = new Customer();
        //absent.setId(NO_CUSTOMER_ID);
        absent.setId(null);
        absent.setName("<Projects without customer>");
        custFilter.add(absent);
        custFilter.addAll(activeCustomers);

        filter.setSelectedItem(null);
        filter.setItems(custFilter);

        params.clear();
        params.put(Constants.QPARAM_DESC, "createdDate");
        List<Project> items = BillingUIServlet.getRestEntityList(Project.REST_PATH, Project.class, params);
        data.clear();
        data.addAll(items);
        dataProvider.refreshAll();

        if(items.size() == 0) {
            Notification note = new Notification("No data", "No projects found", Notification.Type.WARNING_MESSAGE);
            note.show(Page.getCurrent());
        }

        customerEditor.setItems(activeCustomers);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        try {
            setData();
        } catch (PermissionsException e) {
            log.error("insufficient permissions: " + e.getMessage());
            Notification.show("Insufficient permissions", Notification.Type.ERROR_MESSAGE);
        }
    }
}
