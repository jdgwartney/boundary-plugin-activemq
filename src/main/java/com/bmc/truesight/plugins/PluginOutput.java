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
package com.bmc.truesight.plugins;

public class PluginOutput {

    private PluginEventList events;
    private PluginLogList logs;
    private PluginMeasurementList measurements;

    public PluginOutput(PluginEventList events,
                        PluginLogList logs,
                        PluginMeasurementList measurements) {
        this.events = events;
        this.logs = logs;
        this.measurements = measurements;
    }

    PluginEventList getEvents() {
        PluginEventList events = new PluginEventList();
        for (PluginEvent event: this.events) {
            events.add(event);
        }
        return events;
    }

    PluginLogList getLogs() {
        PluginLogList logs = new PluginLogList();
        for (PluginLog s: this.logs) {
            logs.add(s);
        }
        return logs;
    }

    PluginMeasurementList getMeasurements() {
        PluginMeasurementList measurements = new PluginMeasurementList();
        for (PluginMeasurement measurement: this.measurements) {
            measurements.add(measurement);
        }
        return measurements;
    }
}
