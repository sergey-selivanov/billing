package sssii.billing.server.resource.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sssii.billing.common.entity.Customer;
import sssii.billing.common.entity.Project;
import sssii.billing.server.ServletContextHelper;
import sssii.billing.server.resource.ProjectResource;


public class ProjectResourceTest {

    static ServletContextHelper sce;
    ProjectResource res;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        sce = new ServletContextHelper();
        sce.contextInitialized(null);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        res = new ProjectResource();
    }

    @After
    public void tearDown() throws Exception {
    }

//    @Test
//    public void testGetForeignList() {
//        List<Project> projects = res.getForeignList(null);
//        for (Project project : projects) {
//            System.out.println(project.getName());
//
//        }
//    }

    @Test
    public void testGetList() {
        List<Project> items = res.getList(false, null, null);
        for (Project item : items) {
            System.out.println(item.getName());

        }
    }

    @Test
    public void testgetLastInvoicedDate() {
        System.out.println(res.getLastInvoicedDate(1));
    }

    @Test
    public void testgetLastReportedDate() {
        System.out.println(res.getLastWorkReportedDate(234));
    }
}
