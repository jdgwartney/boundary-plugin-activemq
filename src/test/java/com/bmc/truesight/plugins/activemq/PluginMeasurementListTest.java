package com.bmc.truesight.plugins.activemq;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.*;

import java.util.ArrayList;

public class PluginMeasurementListTest {

    @Test
    public void testConstructor() {
        PluginMeasurementList measurements = new PluginMeasurementList();
        assertThat(measurements, is(notNullValue()));
        assertThat(measurements.size(), is(equalTo(0)));
    }

    @Test
    public void testAddMeasurement() {
        PluginMeasurementList list = new PluginMeasurementList();

        list.add(new PluginMeasurement());

        assertThat(list.size(), is(equalTo(1)));
    }


    @Test
    public void testGetByMetric() {
        PluginMeasurementList measurements = new PluginMeasurementList();
//
//        PluginMeasurement m = measurements.get(0);
//        assertThat(m.getMetric(), is(equalTo("TEST_METRIC")));
//        Number expectedNumber = 104501.000000;
//        assertThat(m.getValue(), is(equalTo(expectedNumber)));
//        assertThat(m.getSource(), is(equalTo("localhost")));
//        assertThat(m.getTimestamp(), is(equalTo(1453776205L)));
    }
}
