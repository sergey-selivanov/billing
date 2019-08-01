package sssii.billing.jobcard;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sssii.billing.common.TaskProvider;
import sssii.billing.common.entity.Customer;
import sssii.billing.common.entity.InvoiceItem;
import sssii.billing.common.entity.Project;

public class JobcardTaskProvider implements TaskProvider
{
    private Logger log = LoggerFactory.getLogger(JobcardTaskProvider.class);
    //private static EntityManagerFactory emf = null;
    private EntityManagerFactory emf = null;

    private EntityManager em;
    private Properties sqls;

    public JobcardTaskProvider() {
        sqls = new Properties();
        try {
            sqls.loadFromXML(JobcardTaskProvider.class.getResourceAsStream("/jobcard-sqls.xml"));
        } catch (IOException ex) {
            log.error("failed to load sql definitions", ex);
        }
    }

    @Override
    public void init(Properties properties) {
        emf = Persistence.createEntityManagerFactory("jobcard", properties);
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

        CriteriaBuilder cb = getEm().getCriteriaBuilder();
        CriteriaQuery<JcProject> cq = cb.createQuery(JcProject.class);
        Root<JcProject> from = cq.from(JcProject.class);
        cq.orderBy(cb.desc(from.get("createdDate")));
//cq.orderBy(cb.asc(from.get("createdDate"))); // test

        log.debug("since date: " + sinceDate);

        if(sinceDate != null) {
            // https://stackoverflow.com/questions/9449003/compare-date-entities-in-jpa-criteria-api
            OffsetDateTime odt = OffsetDateTime.from(sinceDate.toInstant().atZone(ZoneOffset.UTC));
            //OffsetDateTime odt2 = OffsetDateTime.from(sinceDate.toInstant()); // Unable to obtain ZoneOffset from TemporalAccessor: 2010-05-17T19:48:41Z of type java.time.Instant
log.debug("since odt at utc: " + odt);
//log.debug("since odt2      : " + odt2);
            odt = odt.plusSeconds(1); // by default, mysql datetime column has no fractional seconds

            cq.where(cb.greaterThan(from.<OffsetDateTime>get("createdDate"), odt));
        }


        List<JcProject> res = getEm().createQuery(cq).getResultList();
//List<JcProject> res = em.createQuery(cq).setMaxResults(5).getResultList();    // test
        for (JcProject p : res) {
            Project project = new Project();
            project.setOriginalId(p.getProjectId());
            project.setName(p.getName());
            project.setOriginalCustomerId(p.getClientId());
            project.setDefaultRate(p.getCostPerHour() == null ? BigDecimal.ZERO : p.getCostPerHour());
            project.setCreatedDate(Timestamp.from(p.getCreatedDate().toInstant()));

//log.debug(p.getName() + " odt: " + p.getCreatedDate() + " date: " + project.getCreatedDate());

            projects.add(project);
        }

        return projects;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    //@Override
    public Customer getCustomerForProject(Integer projectId) {

        // TODO error handling

        Query q = getEm().createNativeQuery(sqls.getProperty("client_by_project"));

        q.setParameter(1, projectId);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();
        Object[] row = rows.get(0);

        Customer customer = new Customer();

        customer.setOriginalId((Integer)row[0]);
        customer.setName((String)row[1]);

        String addr1 = (String)row[2];
        String addr2 = (String)row[3];
        String city = (String)row[4];
        String state = (String)row[5];
        String zip = (String)row[6];

        StringBuilder sb = new StringBuilder();
        if(!addr1.isEmpty()) {
            sb.append(addr1);
        }
        if(!addr2.isEmpty()) {
            sb.append("\n"); sb.append(addr2);
        }

        sb.append("\n"); sb.append(city);

        if(!state.isEmpty() && !zip.isEmpty()) {
            sb.append(", "); sb.append(state); sb.append(" "); sb.append(zip);
        }

        customer.setAddress(sb.toString());

        return customer;
    }

    @Override
    public List<Customer> getCustomerList() {
        ArrayList<Customer> customers = new ArrayList<>();

        Query q = getEm().createNativeQuery(sqls.getProperty("customer_list"));

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();
        for (Object[] row : rows) {
            Customer customer = new Customer();

            customer.setOriginalId((Integer)row[0]);
            customer.setName((String)row[1]);

            String addr1 = (String)row[2];
            String addr2 = (String)row[3];
            String city = (String)row[4];
            String state = (String)row[5];
            String zip = (String)row[6];

            StringBuilder sb = new StringBuilder();
            if(!addr1.isEmpty()) {
                sb.append(addr1);
            }
            if(!addr2.isEmpty()) {
                sb.append("\n"); sb.append(addr2);
            }

            sb.append("\n"); sb.append(city);

            if(!state.isEmpty() && !zip.isEmpty()) {
                sb.append(", "); sb.append(state); sb.append(" "); sb.append(zip);
            }

            customer.setAddress(sb.toString());

            customer.setDescription((String)row[7]);

            customers.add(customer);
        }

        return customers;
    }

    @Override
    public List<InvoiceItem> getWorkedTimeList(Integer projectId, Timestamp from, Timestamp to) {

        log.debug("project: " + projectId);
        log.debug("from: " + from);
        log.debug("to: " + to);

        to = new Timestamp(to.getTime() + TimeUnit.DAYS.toMillis(1));
        log.debug("actual to: " + to);

        Query q = getEm().createNativeQuery(sqls.getProperty("work_hours"));

        q.setParameter(1, projectId);
        q.setParameter(2, from);
        q.setParameter(3, to);

        // Note we do not sum/group by in SQL
        ArrayList<InvoiceItem> items = new ArrayList<InvoiceItem>();

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();
        for (Object[] row : rows) {
            InvoiceItem i = new InvoiceItem();
            i.setTaskNumber(row[4].toString());
            i.setName(row[8].toString());
            i.setInvoicedTimeHours((BigDecimal) row[1]);

            i.setOriginalTaskId(((BigInteger)row[4]).intValueExact());

            items.add(i);
        }

        return items;
    }

    @Override
    public java.sql.Date getLastWorkReported(Integer projectId) {
        // TODO NPE, em expired?
        Query q = getEm().createNativeQuery(sqls.getProperty("last_work_report"));
        q.setParameter(1, projectId);

        Object o = q.getSingleResult();

        if(o == null) {
            return null;
        }
        else {
            return (java.sql.Date)o;
        }

        //Timestamp ts = new Timestamp(date.getTime());

    }
}
