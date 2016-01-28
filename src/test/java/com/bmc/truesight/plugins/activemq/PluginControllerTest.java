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

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.*;

import java.util.ArrayList;

public class PluginControllerTest {

    private PluginOutput output;

    private final String COMMAND="mvn -q exec:java";

    public void generateOutput() {
        try {
            PluginController p = new PluginController(COMMAND);
            p.start();
            Thread.sleep(30000);
            p.stop();
            this.output = p.getPluginOutput();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructor() {
        PluginController p = new PluginController(COMMAND);
        assertNotNull(p);
    }

    @Test(timeout=30000)
    public void testGetOutput() {
        try {
            PluginController p = new PluginController(COMMAND);
            p.start();
            Thread.sleep(5000);
            p.stop();
            PluginOutput output = p.getPluginOutput();
            assertThat(output, is(notNullValue()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(timeout=60000)
    public void testOutput() {
        this.generateOutput();

        ArrayList<PluginEvent> events = this.output.getEvents();
        ArrayList<PluginLog> logs = this.output.getLogs();
        ArrayList<PluginMeasurement> measurements = this.output.getMeasurements();

        assertThat(events, is(notNullValue()));
        assertThat(logs, is(notNullValue()));
        assertThat(measurements, is(notNullValue()));

        assertThat(events.size(), is(equalTo(500)));
        assertThat(logs.size(), is(equalTo(1000)));
        assertThat(measurements.size(), is(equalTo(500)));
    }

    @Test(timeout=30000)
    public void testDestroy() {
        try {
            PluginController p = new PluginController(COMMAND);
            p.start();
            Thread.sleep(100);
            p.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}