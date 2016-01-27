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

    private String command;
    private Process plugin;
    private ArrayList<PluginEvent> events;
    private ArrayList<PluginMeasurement> measurements;

    public PluginController(String command) {
        this.command = command;
    }

    public PluginController() {
        this.command = "boundary-meter --lua init.lua";
    }

    public void start() {
        this.measurements = new ArrayList<PluginMeasurement>();
        this.events = new ArrayList<PluginEvent>();
        Thread pluginThread = new Thread(this);
        pluginThread.start();
    }

    public void run() {

        String s;

        try {

            this.plugin = Runtime.getRuntime().exec(this.command);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(this.plugin.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(this.plugin.getErrorStream()));

            // read the output from the command

            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                try {

                    if (s.matches("^_bevent.*\\$")) {
                        PluginEvent e = new PluginEvent();
                        e.parse(s);
                        this.events.add(e);
                    } else {
                        PluginMeasurement m = new PluginMeasurement();
                        m.parse(s);
                        this.measurements.add(m);
                    }

                } catch (ParseException p) {
                    p.printStackTrace();
                }
            }

            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
        }
    }

    public void stop() {
        if (this.plugin != null) {
            this.plugin.destroy();
        }

    }

    public PluginOutput getPluginOutput() {
        return new PluginOutput(this.events, this.measurements);
    }
}
