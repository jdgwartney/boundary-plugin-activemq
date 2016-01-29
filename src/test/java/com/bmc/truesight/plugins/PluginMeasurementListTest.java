package com.bmc.truesight.plugins;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import com.bmc.truesight.plugins.PluginMeasurement;
import com.bmc.truesight.plugins.PluginMeasurementList;
import org.junit.*;

import java.util.Date;

public class PluginMeasurementListTest {

    private PluginMeasurementList measurements;
    private final String METRIC = "TEST_METRIC_GET";
    private final String SOURCE = "FOOBAR";
    private final long TIMESTAMP = new Date().getTime();
    private final Number VALUE_1 = 100.0;
    private final Number VALUE_2 = 200.0;
    private final Number VALUE_3 = 300.0;


    public void createList() {
        this.measurements = new PluginMeasurementList();
        PluginMeasurement m = new PluginMeasurement(METRIC, VALUE_1, SOURCE, TIMESTAMP);
        measurements.add(m);

        m = new PluginMeasurement(METRIC, VALUE_2, SOURCE, TIMESTAMP);
        measurements.add(m);

        m = new PluginMeasurement(METRIC, VALUE_3, SOURCE, TIMESTAMP);
        measurements.add(m);

    }

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
        this.createList();

        PluginMeasurementList subMeasurements = this.measurements.getByMetric(METRIC);

        assertThat(subMeasurements.size(), is(equalTo(3)));

        PluginMeasurement fetched = measurements.get(0);
        assertThat(fetched.getMetric(), is(equalTo(METRIC)));
        assertThat(fetched.getValue(), is(equalTo(VALUE_1)));
        assertThat(fetched.getSource(), is(equalTo(SOURCE)));
        assertThat(fetched.getTimestamp(), is(equalTo(TIMESTAMP)));

    }
}
