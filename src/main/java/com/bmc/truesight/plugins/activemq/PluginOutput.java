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

import java.util.ArrayList;

public class PluginOutput {

    private ArrayList<PluginMeasurement> measurements;
    private ArrayList<PluginEvent> events;

    public PluginOutput(ArrayList<PluginEvent> events,
                        ArrayList<PluginMeasurement> measurements) {
        this.events = events;
        this.measurements = measurements;
    }

    ArrayList<PluginEvent> getEvents() {
        ArrayList<PluginEvent> events = new ArrayList<PluginEvent>();
        for (PluginEvent event: this.events) {
            events.add(event);
        }
        return events;
    }

    ArrayList<PluginMeasurement> getMeasurements() {
        ArrayList<PluginMeasurement> measurements = new ArrayList<PluginMeasurement>();
        for (PluginMeasurement measurement: this.measurements) {
            measurements.add(measurement);
        }
        return measurements;
    }

    ArrayList<PluginMeasurement> getMeasurementsByMetric(String metric) {
        ArrayList<PluginMeasurement> list = new ArrayList<PluginMeasurement>();

        for (PluginMeasurement measurement : this.measurements) {
            if (measurement.getMetric().equals(metric)) {
                list.add(measurement);
            }
        }
        return list;
    }

    long countMeasurementsByMetric(String metric) {
        long count = 0;

        for (PluginMeasurement measurement : this.measurements) {
            if (measurement.getMetric().equals(metric)) {
                count++;
            }
        }
        return count;
    }
}
