package sssii.billing.jira;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sssii.billing.common.entity.Project;

public class JiraTaskProviderTest {

    static JiraTaskProvider provider;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        provider = new JiraTaskProvider();
        Properties p = new Properties();

        p.put("hibernate.connection.driver_class", "org.mariadb.jdbc.Driver");
        p.put("hibernate.connection.url", "jdbc:mysql://192.168.101.11:3306/jira");
        p.put("hibernate.connection.username", "jira");
        p.put("hibernate.connection.password", "jira");
        p.put("hibernate.hbm2ddl.auto", "validate");
        //p.put("hibernate.jdbc.time_zone", "UTC");

        provider.init(p);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetProjectList() throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd").parse("2017-04-06");
        Timestamp ts = new Timestamp(d.getTime());
        List<Project> list = provider.getProjectList(ts);
        for (Project project : list) {
            System.out.println(project.getName() + " " + project.getCreatedDate());
        }
    }

}
