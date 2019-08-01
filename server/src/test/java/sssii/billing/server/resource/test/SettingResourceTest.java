package sssii.billing.server.resource.test;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sssii.billing.common.entity.rs.Settings;
import sssii.billing.server.ServletContextHelper;
import sssii.billing.server.resource.SettingResource;

public class SettingResourceTest {
    static ServletContextHelper sch;
    SettingResource res;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        sch = new ServletContextHelper();
        sch.contextInitialized(null);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        res = new SettingResource();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetAll() {
        Settings set = res.getAll();
        for(String key: set.getSettings().keySet()) {
            System.out.println("-- " + key + " : " + set.getSettings().get(key).getValue());
        }
    }

}
