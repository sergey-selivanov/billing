package sssii.billing.ui.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.ValoTheme;

import sssii.billing.common.Constants;
import sssii.billing.common.PermissionsException;
import sssii.billing.common.entity.Invoice;
import sssii.billing.ui.BillingUIServlet;
import sssii.billing.ui.DownloadInvoiceStreamSource;


public class Invoices extends VerticalLayout implements View
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(Invoices.class);

    private Grid<Invoice> grid;
    private List<Invoice> data;
    private ListDataProvider<Invoice> dataProvider;

    private HashMap<Integer, DownloadInvoiceStreamSource> downloadStreams;

    public Invoices() {

        downloadStreams = new HashMap<>();

        setSizeFull();

        this.addComponent(new Label("Invoices"));

        grid = new Grid<>();
        grid.setSizeFull();
        grid.setCaption("Invoices");

        grid.addColumn(Invoice::getName)
        .setId("name")
        .setCaption("Name")
        .setMinimumWidthFromContent(false)
        .setExpandRatio(5)
        ;

        grid.addColumn(Invoice::getCreatedDate)
        .setId("date")
        .setCaption("Created")
        //.setRenderer(new DateRenderer(new SimpleDateFormat("MMM d, ''yy")))
        .setRenderer(new DateRenderer(SimpleDateFormat.getDateInstance()))
        //.setRenderer(new DateRenderer("%1$b %1$e")) // String.format, problems with specifiers?
        //.setMinimumWidthFromContent(true)
        .setExpandRatio(1)
        ;


        grid.addComponentColumn(new ValueProvider<Invoice, Component>(){
            private static final long serialVersionUID = 1L;

            @Override
            public Component apply(Invoice source) {
                Button b = new Button();
                //b.setCaption("X");
                // https://vaadin.com/elements/vaadin-icons/html-examples/icons-basic-demos
                b.setIcon(VaadinIcons.DOWNLOAD_ALT);
                b.addStyleName(ValoTheme.BUTTON_SMALL);
                b.addClickListener(new ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(ClickEvent evt) {
                        log.debug("clicked: " + source.getName());

                    }
                });

                if(!downloadStreams.containsKey(source.getId())) {
                    DownloadInvoiceStreamSource diss = new DownloadInvoiceStreamSource();
                    diss.setId(source.getId());
                    downloadStreams.put(source.getId(), diss);
                }

                StreamResource sr = new StreamResource(downloadStreams.get(source.getId()), "junk.pdf");
                sr.setFilename(source.getName() + ".pdf");
                sr.setCacheTime(0);
                FileDownloader fd = new FileDownloader(sr);

                fd.extend(b);

                return b;

            }})
        .setMinimumWidthFromContent(true);


        data = new ArrayList<>();
        dataProvider = new ListDataProvider<>(data);
        grid.setDataProvider(dataProvider);

        this.addComponent(grid);
        this.setExpandRatio(grid, 1);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        setData();
    }

    private void setData() {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put(Constants.QPARAM_DESC, "createdDate");
            List<Invoice> items = BillingUIServlet.getRestEntityList(Invoice.REST_PATH, Invoice.class, params);
            data.clear();
            data.addAll(items);

            dataProvider.refreshAll();

            if(items.size() == 0) {
                Notification note = new Notification("No data", "No invoices found", Notification.Type.WARNING_MESSAGE);
                note.show(Page.getCurrent());
            }
        }
        catch(PermissionsException e) {
            log.error("insufficient permissions: " + e.getMessage());
            Notification.show("Insufficient permissions", Notification.Type.ERROR_MESSAGE);
        }

    }
}
