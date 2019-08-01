package sssii.billing.ui.view;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Binder;
import com.vaadin.data.Binder.Binding;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.converter.StringToBigDecimalConverter;
import com.vaadin.data.provider.DataChangeEvent;
import com.vaadin.data.provider.DataProviderListener;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.StyleGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.EditorSaveEvent;
import com.vaadin.ui.components.grid.EditorSaveListener;
import com.vaadin.ui.components.grid.FooterCell;
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.themes.ValoTheme;

import sssii.billing.common.Constants;
import sssii.billing.common.PermissionsException;
import sssii.billing.common.entity.InvoiceItem;
import sssii.billing.ui.BillingUIServlet;
import sssii.billing.ui.VaadinSessionHelper;
import sssii.billing.ui.entity.InvoiceDraft;

@SuppressWarnings("serial")
public class InvoiceItemsChooserView extends VerticalLayout implements View
{
    private static Logger log = LoggerFactory.getLogger(InvoiceItemsChooserView.class);

    private Grid<InvoiceItem> tasks;
    private FooterRow footer;
    private List<InvoiceItem> data;
    private ListDataProvider<InvoiceItem> dataProvider;

    //private ProjectChoice projectChoice;

    public InvoiceItemsChooserView(){
        setSizeFull();

        //this.addComponent(new Label("New Invoice"));

        tasks = new Grid<>();
        tasks.setSizeFull();
        tasks.setCaption("Invoice items (double-click to edit)");
        //tasks.setSelectionMode(SelectionMode.SINGLE);
        tasks.setSelectionMode(SelectionMode.NONE);

// checkboxes to select rows
// https://demo.vaadin.com/sampler/#ui/grids-and-trees/grid/multi-select
//        tasks.setSelectionMode(SelectionMode.MULTI);


        TextField titleEditor = new TextField();
//        TextArea titleEditor = new TextArea();
//        titleEditor.setSizeFull();

//        Binder<Task> taskBinder = new Binder<>();
//        taskBinder.forField(timeEditor)
//            .withConverter(new StringToIntegerConverter("Must enter a number"))
//            .bind(Task::getMinutes, Task::setMinutes);

        Binder<InvoiceItem> binder = tasks.getEditor().getBinder();
//        binder.addValueChangeListener(new ValueChangeListener<Object>() {
//
//            @Override
//            public void valueChange(ValueChangeEvent<Object> evt) {
//                log.debug("== binder - value changed: " + evt.getValue());
//            }
//        });

//        tasks.addColumn(InvoiceItem::getId)
//            .setExpandRatio(1)
//            .setId("number")
//            .setCaption("#");

        tasks.addColumn(InvoiceItem::getTaskNumber)
        .setExpandRatio(1)
        .setId("taskid")
        .setCaption("Id")
        .setStyleGenerator(item -> "v-align-center")
        ;

        tasks.addColumn(InvoiceItem::getName)
                .setCaption("Title")
                .setId("title")
                .setExpandRatio(8)
                .setEditorComponent(titleEditor, InvoiceItem::setName)
                .setEditable(true)
                //.setStyleGenerator(item -> "v-textfield-small")
                ;

//        Binding<InvoiceItem, Integer> timeBinding = binder.forField(timeEditor)
//                .withConverter(new HoursToSecondsConverter("Must enter a number"))
//                .bind(InvoiceItem::getSeconds, InvoiceItem::setSeconds);
        TextField timeEditor = new TextField();
        Binding<InvoiceItem, BigDecimal> timeBinding = binder.forField(timeEditor)
                .withConverter(new StringToBigDecimalConverter("Must enter a number"))
                .bind(InvoiceItem::getInvoicedTimeHours, InvoiceItem::setInvoicedTimeHours);

        tasks.addColumn(InvoiceItem::getInvoicedTimeHours)
            .setCaption("Hours")
            .setId("hours")
            .setExpandRatio(1)
            .setEditorBinding(timeBinding)
            .setEditable(true)
            .setStyleGenerator(item -> "v-align-right");


//        timeEditor.addValueChangeListener(new ValueChangeListener<String>() {
//            @Override
//            public void valueChange(ValueChangeEvent<String> event) {
//                log.debug("-- time changed");
//            }
//        });

        TextField rateEditor = new TextField();
        Binding<InvoiceItem, BigDecimal> rateBinding = binder.forField(rateEditor)
                .withConverter(new StringToBigDecimalConverter("Must enter a number")) // TODO display as float
                .bind(InvoiceItem::getRate, InvoiceItem::setRate);
        tasks.addColumn(InvoiceItem::getRate)
            .setCaption("Rate")
            .setExpandRatio(1)
            .setEditorBinding(rateBinding)
            .setRenderer(new NumberRenderer(DecimalFormat.getCurrencyInstance()))
            .setEditable(true)
            .setStyleGenerator(item -> "v-align-right");

        tasks.addColumn(InvoiceItem::getTotal)
            .setExpandRatio(1)
            .setId("total")
            //.setRenderer(new NumberRenderer("$%.2f"))
            .setRenderer(new NumberRenderer(DecimalFormat.getCurrencyInstance()))
            .setCaption("Total")
            .setStyleGenerator(item -> "v-align-right");

        //tasks.addComponentColumn(new ValueProvider<InvoiceItem, Button>() {
        tasks.addComponentColumn(new ValueProvider<InvoiceItem, Component>() {

            @Override
            public Component apply(InvoiceItem item) {

                if(item.isCustom()) {
                    Button b = new Button();
                    //b.setCaption("X");
                    // https://vaadin.com/elements/vaadin-icons/html-examples/icons-basic-demos
                    b.setIcon(VaadinIcons.TRASH);
                    b.addStyleName(ValoTheme.BUTTON_SMALL);
                    b.addClickListener(new ClickListener() {

                        @Override
                        public void buttonClick(ClickEvent evt) {
                            log.debug("clicked: " + item.getName());
                            data.remove(item);
                            dataProvider.refreshAll();
                        }
                    });

                    return b;
                }
                else {
                    CheckBox cb = new CheckBox();

                    cb.setValue(item.isIncluded());
                    cb.addValueChangeListener(new ValueChangeListener<Boolean>() {

                        @Override
                        public void valueChange(ValueChangeEvent<Boolean> evt) {
                            item.setIncluded(evt.getValue());
                            //dataProvider.refreshAll();
                            dataProvider.refreshItem(item);
                        }
                    });

                    return cb;
                }

            }
        })
        .setExpandRatio(1)
        .setStyleGenerator(item -> "v-align-center"); // https://stackoverflow.com/questions/43244125/how-to-right-align-a-vaadin-8-grid-column-contents


        footer = tasks.appendFooterRow();
        //FooterCell joined = footer.join(footer.getCell("number"), footer.getCell("taskid"), footer.getCell("title"));
        FooterCell joined = footer.join(footer.getCell("taskid"), footer.getCell("title"));
        joined.setText("Total:");
        joined.setStyleName("v-align-right");

        //joined.getComponent().addStyleName("v-label-bold");
        footer.getCell("hours").setStyleName("v-align-right");
        //footer.getCell("hours").getComponent().addStyleName("v-label-bold");
        footer.getCell("total").setStyleName("v-align-right");
        //footer.getCell("total").getComponent().addStyleName("v-label-bold");


//        tasks.asMultiSelect().addSelectionListener(new MultiSelectionListener<InvoiceItem>() {
//
//            @Override
//            public void selectionChange(MultiSelectionEvent<InvoiceItem> event) {
//                log.debug("-- selection changed");
//                updateTotals();
//            }
//        });


        data = new ArrayList<InvoiceItem>();
        dataProvider = new ListDataProvider<>(data);


        dataProvider.addDataProviderListener(new DataProviderListener<InvoiceItem>() {

            @Override
            public void onDataChange(DataChangeEvent<InvoiceItem> event) {
                log.debug("== data provider - data changed");
                updateTotals();
            }
        });
        tasks.setDataProvider(dataProvider);

        tasks.getEditor().addSaveListener(new EditorSaveListener<InvoiceItem>() {

            @Override
            public void onEditorSave(EditorSaveEvent<InvoiceItem> evt) {
                log.debug("== editor save");
                dataProvider.refreshItem(evt.getBean());

            }
        });

        // Add styling
        //TODO set smaller text
        //tasks.getColumn("").

        // https://vaadin.com/docs/v8/framework/components/components-grid.html
        tasks.setStyleGenerator(new StyleGenerator<InvoiceItem>() {
            @Override
            public String apply(InvoiceItem item) {
                return item.isIncluded() ? null : "dead";
                //You could then style the rows with CSS as follows:
                //
                //.v-grid-row.dead {
                //    color: gray;
                //}
            }
        });

// -------------


        this.addComponent(tasks);
        this.setExpandRatio(tasks, 1);
        tasks.getEditor().setEnabled(true);

        Button addCustomItem = new Button("Add custom item");
        addCustomItem.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent arg0) {
                InvoiceItem i = new InvoiceItem();
                i.setCustom(true);
                i.setOriginalTaskId(0); // can't be null
                i.setTaskNumber(""); // can't be null

                if(!data.add(i)) {
                    log.debug("not added!");
                }
                //dataProvider.refreshItem(i);
                dataProvider.refreshAll();
                tasks.scrollToEnd();
            }
        });

        this.addComponent(addCustomItem);

        Button next = new Button("Next >");
        next.addStyleName(ValoTheme.BUTTON_PRIMARY);
        //next.setClickShortcut(KeyCode.ENTER);
        next.addClickListener(new ClickListener(){
            @Override
            public void buttonClick(ClickEvent event) {

                InvoiceDraft id = VaadinSessionHelper.getInvoiceDraft();

                List<InvoiceItem> selectedItems = new ArrayList<InvoiceItem>();
                for(InvoiceItem i: data){
                    if(i.isIncluded()) {
                        selectedItems.add(i);
                    }
                }
                id.setInvoiceItems(selectedItems);

                UI.getCurrent().getNavigator().navigateTo(BillingViewType.INVOICE_DETAILS_EDIT.getViewName());
            }});

        this.addComponent(next);
        this.setComponentAlignment(next, Alignment.BOTTOM_RIGHT);
    }

    private void updateTotals() {

        log.debug("== updating totals");

        BigDecimal hours = new BigDecimal(0);
        BigDecimal total = new BigDecimal(0);

        for (InvoiceItem invoiceItem : data) {
            if(invoiceItem.isIncluded()) {
                hours = hours.add(invoiceItem.getInvoicedTimeHours());
                total = total.add(invoiceItem.getTotal());
            }
        }

        footer.getCell("hours").setText(String.format("%.2f", hours));
        //footer.getCell("total").setText(String.format("$%.2f", total));
        footer.getCell("total").setText(DecimalFormat.getCurrencyInstance().format(total));


//        VaadinSession.getCurrent().setAttribute(Constants.SESSION_INVOICE_HOURS, hours);
//        VaadinSession.getCurrent().setAttribute(Constants.SESSION_INVOICE_TOTAL, total);
        VaadinSessionHelper.getInvoiceDraft().getInvoice().setInvoicedTimeHours(hours);
        VaadinSessionHelper.getInvoiceDraft().getInvoice().setTotal(total);

        //tasks.getDataProvider().refreshAll();
    }

    @Override
    public void enter(ViewChangeEvent event) {
//        log.debug("enter");

        setData();
        updateTotals();
    }

    private void setData() {
        log.debug("set data");

        //if(projectChoice != null) {

        try {
            HashMap<String, Object> params = new HashMap<>();

            InvoiceDraft id = VaadinSessionHelper.getInvoiceDraft();

            params.put(Constants.QPARAM_PROJECTID, id.getInvoice().getProjectId());
            params.put(Constants.QPARAM_FROMDATE, DateTimeFormatter.ISO_DATE.format(id.getFrom()));    // see also ISO_LOCAL_DATE
            params.put(Constants.QPARAM_TODATE, DateTimeFormatter.ISO_DATE.format(id.getTo()));

            List<InvoiceItem> items = BillingUIServlet.<InvoiceItem>getRestEntityList("invoiceItems/candidates", InvoiceItem.class, params);
            data.clear();
            for(InvoiceItem item: items) {
                data.add(item);
            }

            dataProvider.refreshAll();

            if(items.size() == 0) {
                Notification note = new Notification("No data", "No reported hours found in this date range", Notification.Type.WARNING_MESSAGE);
                note.show(Page.getCurrent());
            }
        }
        catch(PermissionsException e) {
            log.error("insufficient permissions: " + e.getMessage());
            Notification.show("Insufficient permissions", Notification.Type.ERROR_MESSAGE);
        }
        //}

    }

}
