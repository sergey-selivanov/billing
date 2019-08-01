package sssii.billing.ui.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import sssii.billing.common.Constants;
import sssii.billing.common.PermissionsException;
import sssii.billing.common.entity.Invoice;
import sssii.billing.ui.BillingUIServlet;
import sssii.billing.ui.DownloadInvoiceStreamSource;

public class InvoiceCompleteView extends VerticalLayout implements View
{
    private static final long serialVersionUID = 1L;

    private Logger log = LoggerFactory.getLogger(InvoiceCompleteView.class);

    Label summary;
    Button downloadPdf;

    Invoice invoice;

    StreamResource streamResource;
    DownloadInvoiceStreamSource dnldStreamSource;


 // filedownloader is a client-side component, try something else?
 // https://stackoverflow.com/questions/15815848/lazily-detemine-filename-when-using-filedownloader
 // setCacheTime ?

    public InvoiceCompleteView() {
        setSizeFull();

        FormLayout form = new FormLayout();
        form.setSizeFull();

        summary = new Label();
        form.addComponent(summary);

        downloadPdf = new Button("Download PDF");
        downloadPdf.addClickListener(new ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                log.debug("download clicked");
            }
        });
        form.addComponent(downloadPdf);

        dnldStreamSource = new DownloadInvoiceStreamSource();
        streamResource = new StreamResource(dnldStreamSource, "junk.pdf");
        streamResource.setCacheTime(0);

        FileDownloader fd = new FileDownloader(streamResource);
        fd.extend(downloadPdf);


        addComponent(form);
    }

    private void setData() {
        log.debug("setData");
        Integer id = (Integer)VaadinSession.getCurrent().getAttribute(Constants.SESSION_NEW_INVOICE_ID);
        if(id != null) {

            try {
                invoice = BillingUIServlet.getRestEntity(Invoice.REST_PATH + "/" + id, Invoice.class);

                summary.setValue("Invoice created: " + invoice.getName());
                downloadPdf.setEnabled(true);

                dnldStreamSource.setId(id);
                streamResource.setFilename(invoice.getName() + ".pdf");
            } catch (PermissionsException e) {
                log.error("insufficient permissions: " + e.getMessage());

                Notification.show("Insufficient permissions", Notification.Type.ERROR_MESSAGE);
            }
        }
        else {
            summary.setValue("Something wrong: no new invoice id in session");
            downloadPdf.setEnabled(false);
        }
    }

    @Override
    public void enter(ViewChangeEvent event) {
        setData();
    }

}
