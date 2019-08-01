package sssii.billing.common.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import sssii.billing.common.LocalDateAdapter;

@Entity
public class Invoice implements BillingEntity {

    public static final String REST_PATH = "invoices";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    //private LocalDate invoiceDate; // not serialized

    @Column(columnDefinition = "date")
    //private Timestamp invoiceDate;
    @XmlJavaTypeAdapter(LocalDateAdapter.class) // http://wiki.eclipse.org/EclipseLink/Examples/MOXy/JSON_Twitter
    private LocalDate invoiceDate;

    @Column(name = "createdDateUTC", columnDefinition = "datetime")
    private Timestamp createdDate;

    private Integer customerId;
    private Integer projectId;

    @Column(columnDefinition = "date")
    private Timestamp workFromDate;	// TODO use local date or timestamp? (also in sql)

    @Column(columnDefinition = "date")
    private Timestamp workToDate;
    private BigDecimal invoicedTimeHours;
    private BigDecimal total;

    @Column(columnDefinition = "text")
    private String toAddress;
    private String payableTo;
    private String terms;

    @Column(columnDefinition = "text")
    private String mailCheckToAddress;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
//    public Timestamp getInvoiceDate() {
//        return invoiceDate;
//    }
//    public void setInvoiceDate(Timestamp invoiceDate) {
//        this.invoiceDate = invoiceDate;
//    }
    public Timestamp getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    public Integer getProjectId() {
        return projectId;
    }
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
    public Timestamp getWorkFromDate() {
        return workFromDate;
    }
    public void setWorkFromDate(Timestamp workFromDate) {
        this.workFromDate = workFromDate;
    }
    public Timestamp getWorkToDate() {
        return workToDate;
    }
    public void setWorkToDate(Timestamp workToDate) {
        this.workToDate = workToDate;
    }

    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    public BigDecimal getInvoicedTimeHours() {
        return invoicedTimeHours;
    }
    public void setInvoicedTimeHours(BigDecimal invoicedTimeHours) {
        this.invoicedTimeHours = invoicedTimeHours;
    }
    public String getToAddress() {
        return toAddress;
    }
    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }
    public String getPayableTo() {
        return payableTo;
    }
    public void setPayableTo(String payableTo) {
        this.payableTo = payableTo;
    }
    public String getTerms() {
        return terms;
    }
    public void setTerms(String terms) {
        this.terms = terms;
    }
    public String getMailCheckToAddress() {
        return mailCheckToAddress;
    }
    public void setMailCheckToAddress(String mailCheckToAddress) {
        this.mailCheckToAddress = mailCheckToAddress;
    }
    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }
    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

}
