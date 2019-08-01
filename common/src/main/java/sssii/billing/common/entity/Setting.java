package sssii.billing.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Settings")
public class Setting implements BillingEntity
{
    public static final String REST_PATH = "settings";

    // Payable by check to: Company Intl. Inc
    public static final String PAYMENT_RECIPIENT_TITLE = "PAYMENT_RECIPIENT_TITLE";
    // Terms: On receipt
    public static final String PAYMENT_TERMS = "PAYMENT_TERMS";
    // Mail check to: nnnn
    public static final String PAYMENT_RECIPIENT_ADDRESS = "PAYMENT_RECIPIENT_ADDRESS";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable=false, length=255)
    private String name;

    @Column(name = "val", columnDefinition = "TEXT")
    private String value;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
