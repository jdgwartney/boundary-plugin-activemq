package com.bmc.truesight.plugins.activemq;

import static org.junit.Assert.*;
import org.junit.*;

public class PluginControllerTest {

    // assigning the values
    protected void setUp(){
    }

    @Test
    public void testDefaultConstructor(){
        PluginController p = new PluginController();
        assertNotNull(p);
    }

    @Test
    public void testConstructor() {
        PluginController p = new PluginController("boundary-meter --lua init.lua");
        assertNotNull(p);
    }

    @Test
    public void testEventRegEx() {
        String s = "_bevent:Boundary ActiveMQ Plugin version 0.9.3-localhost Status|m:Up|h:localhost|s:localhost|t:info|tags:lua,plugin,activemq";
        String regEx = "^_bevent.*$";
        assertTrue(s.matches(regEx));
    }

    @Test
    public void testDestroy() {
        try {
            PluginController p = new PluginController("boundary-meter --lua init.lua");
            p.start();
            Thread.sleep(5000);
            p.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}