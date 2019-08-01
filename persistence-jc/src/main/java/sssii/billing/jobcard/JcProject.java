package sssii.billing.jobcard;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "dbo", name = "Project")
public class JcProject {

//    private static DateTimeFormatter dtf;
//
//    static {
//        dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS ZZZZZ");
//    }

    @Id
    private Integer projectId;

    @Column(columnDefinition = "nvarchar", length = 128)
    private String name;
    private Integer clientId;

    // http://memorynotfound.com/hibernate-date-time-datetime-mapping/
    //@Temporal(TemporalType.TIMESTAMP)

    // java.sql.SQLException: Value 2010-04-27 03:29:31.1630867 -07:00 cannot be converted to TIMESTAMP.
    // https://stackoverflow.com/questions/36405320/using-the-datetimeoffset-datatype-with-jtds

    @Column(columnDefinition="datetimeoffset")
    //private String createdDate;
    //private Date createdDate;
    private OffsetDateTime createdDate;

    @Column(columnDefinition="money")
    private BigDecimal costPerHour;

    public Integer getProjectId() {
        return projectId;
    }
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getClientId() {
        return clientId;
    }
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public BigDecimal getCostPerHour() {
        return costPerHour;
    }
    public void setCostPerHour(BigDecimal costPerHour) {
        this.costPerHour = costPerHour;
    }
    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

//    public Date getCreatedDate() {
//        ZonedDateTime zdt = ZonedDateTime.parse(createdDate, dtf);
//        return Date.from(zdt.toInstant());
//    }
//    public void setCreatedDate(Date createdDate) {
//
//        this.createdDate = dtf.format(createdDate.toInstant());
//    }

}
