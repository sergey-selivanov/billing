package sssii.billing.common.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class InvoiceItem implements BillingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer invoiceId;

    private String taskNumber;// = "";    // TODO remove initializations?
    private Integer originalTaskId;
    private String name;// = "";
    //private Integer seconds;    // JIRA uses seconds, JC uses hours decimal
    private BigDecimal invoicedTimeHours = new BigDecimal(0);
    private BigDecimal rate = new BigDecimal(0);
    private BigDecimal total = new BigDecimal(0);	// TODO remove, not needed actually
    private boolean isCustom = false;
    private boolean isIncluded = true;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getInvoiceId() {
        return invoiceId;
    }
    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }
    public String getTaskNumber() {
        return taskNumber;
    }
    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
        updateTotal();
    }
    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    public boolean isCustom() {
        return isCustom;
    }
    public void setCustom(boolean isCustom) {
        this.isCustom = isCustom;
    }
    public boolean isIncluded() {
        return isIncluded;
    }
    public void setIncluded(boolean isIncluded) {
        this.isIncluded = isIncluded;
    }


    private void updateTotal() {
        total = invoicedTimeHours.multiply(rate);
    }
    public BigDecimal getInvoicedTimeHours() {
        return invoicedTimeHours;
    }
    public void setInvoicedTimeHours(BigDecimal invoicedTimeHours) {
        this.invoicedTimeHours = invoicedTimeHours;
        updateTotal();
    }
    public Integer getOriginalTaskId() {
        return originalTaskId;
    }
    public void setOriginalTaskId(Integer originalTaskId) {
        this.originalTaskId = originalTaskId;
    }

}
