package sssii.billing.common.entity.rs;

import java.util.List;

import sssii.billing.common.entity.BillingEntity;
import sssii.billing.common.entity.Invoice;
import sssii.billing.common.entity.InvoiceItem;

public class NewInvoice implements BillingEntity {

    private Invoice invoice;
    private List<InvoiceItem> items;

    public Invoice getInvoice() {
        return invoice;
    }
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
    public List<InvoiceItem> getItems() {
        return items;
    }
    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    // placeholder methods, we do not need id actually. This is a wrapper class to transport invoice+items over rest

    @Override
    public Integer getId() {
        return null;
    }
    @Override
    public void setId(Integer id) {
    }
}
