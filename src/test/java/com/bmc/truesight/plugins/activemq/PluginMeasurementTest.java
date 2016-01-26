package com.bmc.truesight.plugins.activemq;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.Date;

public class PluginMeasurementTest {

    // assigning the values
    protected void setUp(){
    }

    @Test
    public void testDefaults(){
        PluginMeasurement m = new PluginMeasurement();
        assertNull("", m.getMetric());
        assertNull(m.getValue());
        assertNull("", m.getSource());
        assertEquals(0, m.getTimestamp());
    }

    @Test
    public void testMetric() {
        PluginMeasurement m = new PluginMeasurement();
        String expectedMetric = "CPU";

        m.setMetric(expectedMetric);
        assertEquals(expectedMetric, m.getMetric());
    }

    @Test
    public void testValue() {
        PluginMeasurement m = new PluginMeasurement();
        Number expectedValue = 100.0;

        m.setValue(expectedValue);
        assertEquals(expectedValue, m.getValue());
    }

    @Test
    public void testSource() {
        PluginMeasurement m = new PluginMeasurement();
        String expectedSource = "FOOBAR";

        m.setSource(expectedSource);
        assertEquals(expectedSource, m.getSource());
    }

    @Test
    public void testTimestamp() {
        PluginMeasurement m = new PluginMeasurement();
        Date currentDate = new Date();
        long expectedTimestamp = currentDate.getTime() / 1000;

        m.setTimestamp(expectedTimestamp);
        assertEquals(expectedTimestamp, m.getTimestamp());
    }


}