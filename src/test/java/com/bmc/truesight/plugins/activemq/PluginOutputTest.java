package com.bmc.truesight.plugins.activemq;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;

import java.text.ParseException;
import java.util.ArrayList;

public class PluginOutputTest {

    private PluginOutput output;

    @Before
    public void createOutput() throws ParseException {
        ArrayList<PluginEvent> events = new ArrayList<PluginEvent>();
        ArrayList<PluginMeasurement> measurements = new ArrayList<PluginMeasurement>();
        PluginEvent event = new PluginEvent();
        String rawEvent = "_bevent:TITLE|m:MESSAGE|h:SOURCE|s:SENDER|t:info|tags:red,green,blue|at:text/html|ad:APPLICATION_DATA";
        event.parse(rawEvent);
        events.add(event);

        PluginMeasurement measurement = new PluginMeasurement();
        String rawMessage = "TEST_METRIC 104501.000000 localhost 1453776205";
        measurement.parse(rawMessage);
        measurements.add(measurement);

        this.output = new PluginOutput(events, measurements);
    }

    @Test
    public void testConstructor() {
        ArrayList<PluginEvent> events = new ArrayList<PluginEvent>();
        ArrayList<PluginMeasurement> measurements = new ArrayList<PluginMeasurement>();
        PluginOutput output = new PluginOutput(events, measurements);
        assertThat(output, is(notNullValue()));
    }

    @Test
    public void testGet() {
        ArrayList<PluginEvent> events = this.output.getEvents();
        ArrayList<PluginMeasurement> measurements = this.output.getMeasurements();

        assertThat(events, is(notNullValue()));
        assertThat(measurements, is(notNullValue()));
        assertThat(events.size(), is(equalTo(1)));
        assertThat(measurements.size(), is(equalTo(1)));
    }

    @Test
    public void testGetByMetric() {
        ArrayList<PluginMeasurement> measurements = this.output.getMeasurementsByMetric("TEST_METRIC");
        assertThat(measurements.size(), is(equalTo(1)));

        PluginMeasurement m = measurements.get(0);
        assertThat(m.getMetric(), is(equalTo("TEST_METRIC")));
        Number expectedNumber = 104501.000000;
        assertThat(m.getValue(), is(equalTo(expectedNumber)));
        assertThat(m.getSource(), is(equalTo("localhost")));
        assertThat(m.getTimestamp(), is(equalTo(1453776205L)));
    }

    @Test
    public void testMeasurementCount() {
        assertThat(this.output.countMeasurementsByMetric("TEST_METRIC"), is(equalTo(1L)));
    }
}
