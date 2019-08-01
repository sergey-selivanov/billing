package sssii.billing.server.resource.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sssii.billing.server.ServletContextHelper;
import sssii.billing.server.resource.InvoiceResource;

public class InvoiceResourceTest {
    static ServletContextHelper sce;
    InvoiceResource res;

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
        res = new InvoiceResource();
    }

    @After
    public void tearDown() throws Exception {
    }
}
