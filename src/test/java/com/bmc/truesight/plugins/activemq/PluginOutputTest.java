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
        PluginEventList events = new PluginEventList();
        PluginMeasurementList measurements = new PluginMeasurementList();
        PluginLogList logs = new PluginLogList();
        PluginEvent event = new PluginEvent();
        String rawEvent = "_bevent:TITLE|m:MESSAGE|h:SOURCE|s:SENDER|t:info|tags:red,green,blue|at:text/html|ad:APPLICATION_DATA";
        event.parse(rawEvent);
        events.add(event);

        PluginMeasurement measurement = new PluginMeasurement();
        String rawMessage = "TEST_METRIC 104501.000000 localhost 1453776205";
        measurement.parse(rawMessage);
        measurements.add(measurement);

        PluginLog log = new PluginLog();
        String rawLog = "An error occurred";
        log.parse(rawLog);
        logs.add(log);

        this.output = new PluginOutput(events, logs, measurements);
    }

    @Test
    public void testConstructor() {
        PluginMeasurementList measurements = new PluginMeasurementList();
        PluginEventList events = new PluginEventList();
        PluginLogList logs = new PluginLogList();
        PluginOutput output = new PluginOutput(events, logs, measurements);
        assertThat(output, is(notNullValue()));
    }

    @Test
    public void testGet() {
        PluginEventList events = this.output.getEvents();
        PluginLogList logs = this.output.getLogs();
        PluginMeasurementList measurements = this.output.getMeasurements();

        assertThat(events, is(notNullValue()));
        assertThat(logs, is(notNullValue()));
        assertThat(measurements, is(notNullValue()));
        assertThat(events.size(), is(equalTo(1)));
        assertThat(logs.size(), is(equalTo(1)));
        assertThat(measurements.size(), is(equalTo(1)));
    }
}
