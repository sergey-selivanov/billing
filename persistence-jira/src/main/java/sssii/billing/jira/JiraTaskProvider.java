package sssii.billing.jira;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sssii.billing.common.TaskProvider;
import sssii.billing.common.entity.Customer;
import sssii.billing.common.entity.InvoiceItem;
import sssii.billing.common.entity.Project;

public class JiraTaskProvider implements TaskProvider
{
    private Logger log = LoggerFactory.getLogger(JiraTaskProvider.class);
    private static EntityManagerFactory emf = null;

    private EntityManager em;

    private Properties sqls;

    public JiraTaskProvider() {
        sqls = new Properties();
        try {
            sqls.loadFromXML(JiraTaskProvider.class.getResourceAsStream("/jira-sqls.xml"));
        } catch (IOException ex) {
            log.error("failed to load sql definitions", ex);
        }
    }

    @Override
    public void init(Properties properties) {
        emf = Persistence.createEntityManagerFactory("jira", properties);
        //em = emf.createEntityManager();
    }

    private EntityManager getEm() {
        try {
            if(em == null) {
                em = emf.createEntityManager();
            }
        }
        catch(Exception ex) {
            log.error("failed to create em", ex);
        }

        return em;
    }

    @Override
    public List<Project> getProjectList(Timestamp sinceDate) {
        ArrayList<Project> projects = new ArrayList<>();

        Query q = getEm().createNativeQuery(sqls.getProperty("projects"));

        if(sinceDate != null) {
            q.setParameter(1, sinceDate);
        }
        else {
            q.setParameter(1, "0000-00-00");
        }

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();
        for (Object[] row : rows) {
            Project p = new Project();
            p.setOriginalId(((BigDecimal) row[2]).intValue());
            p.setName(row[1].toString());

            // Jira stores local dates
// TODO: local to UTC?
            Timestamp localTs = (Timestamp) row[0];
//            ZonedDateTime zdt = ZonedDateTime.of(localTs.toInstant(), ZoneOffset.UTC);
//            p.setCreatedDate(new Timestamp(zdt.toInstant().toEpochMilli()));

            p.setCreatedDate(localTs);
            p.setDefaultRate(BigDecimal.ZERO);

            projects.add(p);
        }

        return projects;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    //@Override
    public Customer getCustomerForProject(Integer projectId) {
        // no customers in jira
        return null;
    }

    @Override
    public List<Customer> getCustomerList() {
        // no customers in jira
        return null;
    }

    @Override
    public List<InvoiceItem> getWorkedTimeList(Integer projectId, Timestamp from, Timestamp to) {

        log.debug("project: " + projectId);
        log.debug("from: " + from);
        log.debug("to: " + to);

        to = new Timestamp(to.getTime() + TimeUnit.DAYS.toMillis(1));
        log.debug("actual to: " + to);

        Query q = getEm().createNativeQuery(sqls.getProperty("worklog"));

        q.setParameter(1, projectId);
        q.setParameter(2, from);
        q.setParameter(3, to);

        // Note we do not sum/group by in SQL
        ArrayList<InvoiceItem> items = new ArrayList<InvoiceItem>();

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();
        for (Object[] row : rows) {
            InvoiceItem i = new InvoiceItem();
            i.setTaskNumber(row[8].toString());
            i.setName(row[5].toString());
            // jira keeps time in seconds
            i.setInvoicedTimeHours(((BigDecimal) row[1]).divide(new BigDecimal(3600), 2, RoundingMode.HALF_EVEN));

            i.setOriginalTaskId(((BigDecimal)row[4]).intValueExact());

            items.add(i);
        }

        return items;
    }

    @Override
    public java.sql.Date getLastWorkReported(Integer projectId) {
        Query q = getEm().createNativeQuery(sqls.getProperty("last_work_report"));
        q.setParameter(1, projectId);

        Object o = q.getSingleResult();
        if(o == null) {
            return null;
        }
        else {
            Timestamp ts = (Timestamp)q.getSingleResult();
            return new Date(ts.getTime());
        }

        //return new Date(ts.getTime()); // TODO verify
    }
}
