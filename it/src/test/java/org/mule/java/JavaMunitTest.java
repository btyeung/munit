package org.mule.java;

import org.junit.Test;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

import static junit.framework.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class JavaMunitTest extends FunctionalMunitSuite{

    @Override
    protected String getConfigResources() {
        return "mule-config.xml";
    }

    @Test
    public void test() throws Exception {
        whenMessageProcessor("create-group")
                .ofNamespace("jira")
                .thenReturn(muleMessageWithPayload("expected"));

        Object payload = runFlow("callingJira", testEvent("something")).getMessage().getPayload();

        assertEquals("expected", payload);
    }

    @Test
    public void testSetMuleAppHome() throws Exception {

        Object payload = runFlow("setMuleAppHomeFlow", testEvent("something")).getMessage().getPayload();

        assertEquals(new File(getClass().getResource("/mule-config.xml").getPath()).getParentFile().getAbsolutePath(), payload);
    }


}
