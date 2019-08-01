package sssii.billing.common;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;
import java.util.Properties;

import sssii.billing.common.entity.Customer;
import sssii.billing.common.entity.InvoiceItem;
import sssii.billing.common.entity.Project;

public interface TaskProvider {
    void init(Properties properties);
    List<Project> getProjectList(Timestamp sinceDate);
    List<Customer> getCustomerList();
    List<InvoiceItem> getWorkedTimeList(Integer projectId, Timestamp from, Timestamp to);
    //Customer getCustomerForProject(Integer projectId);
    Date getLastWorkReported(Integer projectId);    // TODO Timestamp?
    void close();
}
