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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class PluginController implements Runnable {

    private final long DEFAULT_COUNT = 10L;

    private String command;
    private Process plugin;
    private ArrayList<PluginEvent> events;
    private ArrayList<PluginLog> logs;
    private ArrayList<PluginMeasurement> measurements;
    private BufferedReader stdInput;
    private BufferedReader stdError;
    private long stdOutCount;
    private long stdErrCount;

    public PluginController(String command) {
        this.command = command;
        this.stdOutCount = DEFAULT_COUNT;
        this.stdErrCount = DEFAULT_COUNT;
    }

    public PluginController(String command,
                            long stdOutCount,
                            long stdErrCount) {
        this.command = command;
        this.stdOutCount = stdOutCount;
        this.stdErrCount = stdErrCount;
    }

    public void start() {
        this.events = new ArrayList<PluginEvent>();
        this.logs = new ArrayList<PluginLog>();
        this.measurements = new ArrayList<PluginMeasurement>();
        Thread pluginThread = new Thread(this);
        pluginThread.start();
    }

    public void run() {
        try {
            this.plugin = Runtime.getRuntime().exec(this.command);
            this.stdInput = new BufferedReader(new InputStreamReader(this.plugin.getInputStream()));
            this.stdError = new BufferedReader(new InputStreamReader(this.plugin.getErrorStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            String s;
            Long lineCount = 1L;
            while ((s = this.stdInput.readLine()) != null && lineCount <= this.stdOutCount) {
                if (s.matches(PluginEvent.EVENT_REG_EX)) {
                    PluginEvent e = new PluginEvent();
                    e.parse(s);
                    this.events.add(e);
                } else {
                    PluginMeasurement m = new PluginMeasurement();
                    m.parse(s);
                    this.measurements.add(m);
                }
                lineCount++;
            }

            lineCount = 1L;
            while ((s = stdError.readLine()) != null && lineCount <= this.stdErrCount) {
                System.out.flush();
                PluginLog log = new PluginLog();
                log.parse(s);
                this.logs.add(log);
                lineCount++;
            }
            if (this.plugin != null) {
                this.plugin.destroy();
            }
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ParseException p) {
            p.printStackTrace();
        }
    }

    public PluginOutput getPluginOutput() {
        return new PluginOutput(this.events, this.logs, this.measurements);
    }
}
