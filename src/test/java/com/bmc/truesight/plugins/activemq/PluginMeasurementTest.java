//
// Copyright 2015 BMC Software, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
package com.bmc.truesight.plugins.activemq;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.*;

import java.text.ParseException;
import java.util.Date;

public class PluginMeasurementTest {

    @Test
    public void testDefaults(){
        PluginMeasurement m = new PluginMeasurement();
        assertThat(m.getMetric(), is(nullValue()));
        assertThat(m.getValue(), is(nullValue()));
        assertThat(m.getSource(), is(nullValue()));
        assertThat(m.getTimestamp(), equalTo(0L));
    }

    @Test
    public void testMetric() {
        PluginMeasurement m = new PluginMeasurement();
        String expectedMetric = "CPU";

        m.setMetric(expectedMetric);
        assertThat(m.getMetric(), equalTo(expectedMetric));
    }

    @Test
    public void testValue() {
        PluginMeasurement m = new PluginMeasurement();
        Number expectedValue = 100.0;

        m.setValue(expectedValue);
        assertThat(m.getValue(), equalTo(expectedValue));
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

    @Test
    public void testParseFourTokens() throws ParseException {
        String s = "ACTIVEMQ_BROKER_TOTALS_MESSAGES 104501.000000 localhost 1453776205";
        PluginMeasurement m = new PluginMeasurement();

        m.parse(s);

        assertEquals("ACTIVEMQ_BROKER_TOTALS_MESSAGES", m.getMetric());
        assertEquals(104501.0, m.getValue());
        assertEquals("localhost", m.getSource());
        assertEquals(1453776205, m.getTimestamp());
    }

    @Test
    public void testParseThreeTokens() throws ParseException {
        String s = "ACTIVEMQ_BROKER_TOTALS_QUEUES 10.0 localhost";
        PluginMeasurement m = new PluginMeasurement();

        m.parse(s);

        assertEquals("ACTIVEMQ_BROKER_TOTALS_QUEUES", m.getMetric());
        assertEquals(10.0, m.getValue());
        assertEquals("localhost", m.getSource());
        assertEquals(0, m.getTimestamp());
    }

    @Test
    public void testParseTwoTokens() throws ParseException {
        String s = "ACTIVEMQ_BROKER_TOTALS_CONSUMERS 0.0";
        PluginMeasurement m = new PluginMeasurement();

        m.parse(s);

        assertEquals("ACTIVEMQ_BROKER_TOTALS_CONSUMERS", m.getMetric());
        assertEquals(0.0, m.getValue());
        assertNull( m.getSource());
        assertEquals(0, m.getTimestamp());
    }

    @Test(expected=ParseException.class)
    public void testParseOneToken() throws ParseException {
        String s = "ACTIVEMQ_BROKER_TOTALS_CONSUMERS";
        PluginMeasurement m = new PluginMeasurement();

        m.parse(s);

    }
}