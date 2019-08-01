package sssii.billing.server.resource.test;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sssii.billing.common.entity.Customer;
import sssii.billing.server.ServletContextHelper;
import sssii.billing.server.resource.CustomerResource;

public class CustomerResourceTest {
    static ServletContextHelper sce;
    CustomerResource res;

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
        res = new CustomerResource();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetForeignCustomerList() {
        List<Customer> customers = ServletContextHelper.getTaskProvider().getCustomerList();
        for (Customer c : customers) {
            System.out.println("-- " + c.getName());
            System.out.println(c.getAddress());
        }
    }

    @Test
    public void testGetList() {
        List<Customer> items = res.getList(false, null, null);
        for (Customer item : items) {
            System.out.println(item.getName());

        }
    }

    @Test
    public void testInsert() {
        Customer c = new Customer();
        c.setName("unit test");
        c.setDescription("test description");
        c.setAddress("test address");

        res.insert(c);
    }

}
