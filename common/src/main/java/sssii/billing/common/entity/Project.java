package sssii.billing.common.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Project implements BillingEntity {

    public static final String REST_PATH = "projects";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer customerId;

    private BigDecimal defaultRate;

    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdDateUTC", columnDefinition = "datetime")
    //private Date createdDate;
    private Timestamp createdDate;    // with Date, milliseconds lost, can't compare with ms sql datetimeoffset

    private boolean isActive;

    private Integer originalId;
    private Integer originalCustomerId;

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
    public Integer getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    public BigDecimal getDefaultRate() {
        return defaultRate;
    }
    public void setDefaultRate(BigDecimal defaultRate) {
        this.defaultRate = defaultRate;
    }

    public Integer getOriginalId() {
        return originalId;
    }
    public void setOriginalId(Integer originalId) {
        this.originalId = originalId;
    }
    public Integer getOriginalCustomerId() {
        return originalCustomerId;
    }
    public void setOriginalCustomerId(Integer originalCustomerId) {
        this.originalCustomerId = originalCustomerId;
    }
    public Timestamp getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

}
