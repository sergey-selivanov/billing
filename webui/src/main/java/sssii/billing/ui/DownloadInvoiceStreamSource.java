package sssii.billing.ui;

import java.io.InputStream;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.StreamResource.StreamSource;

import sssii.billing.common.PermissionsException;
import sssii.billing.common.entity.Invoice;

public class DownloadInvoiceStreamSource implements StreamSource
{
    private static final long serialVersionUID = 1L;
    private Logger log = LoggerFactory.getLogger(DownloadInvoiceStreamSource.class);
    private Integer id;

    //public DownloadInvoiceStreamSource() {}

    public void setId(Integer id) {
        log.debug("set id: " + id);
        this.id = id;
    }

    @Override
    public InputStream getStream() {
        log.debug("getStream " + id);

        Response resp;
        try {
            resp = BillingUIServlet.invokeRestGet(Invoice.REST_PATH + "/" + id + "/pdf");
            resp.bufferEntity(); // allow multiple readEntity

            return resp.readEntity(InputStream.class);
        } catch (PermissionsException e) {
            log.error("insufficient permissions: " + e.getMessage());
        }

        return null;
    }

}
