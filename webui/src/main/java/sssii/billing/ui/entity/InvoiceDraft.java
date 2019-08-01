package sssii.billing.ui.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import sssii.billing.common.entity.Customer;
import sssii.billing.common.entity.Invoice;
import sssii.billing.common.entity.InvoiceItem;

public class InvoiceDraft {

    //private Integer projectId;
    //private Integer originalProjectId;
    private LocalDate from;
    private LocalDate to;

    private Invoice invoice = new Invoice();
    private List<InvoiceItem> invoiceItems = new ArrayList<InvoiceItem>();

    private Customer customer;

    public LocalDate getFrom() {
        return from;
    }
    public void setFrom(LocalDate from) {
        this.from = from;
    }
    public LocalDate getTo() {
        return to;
    }
    public void setTo(LocalDate to) {
        this.to = to;
    }
    public Invoice getInvoice() {
        return invoice;
    }
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
    public List<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }
    public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
