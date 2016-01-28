package com.bmc.truesight.plugins.activemq;

import java.util.Date;

public class PluginSimulator {

    private long eventCount = Long.MAX_VALUE;
    private long logCount = Long.MAX_VALUE;
    private long measurementCount = Long.MAX_VALUE;

    public PluginSimulator() {
        this.getProperties();
    }

    public PluginSimulator(long eventCount, long logCount, long measurementCount) {
        this.eventCount = eventCount;
        this.logCount = logCount;
        this.measurementCount = measurementCount;

        // System properties override
        this.getProperties();
    }

    public void getProperties() {
        this.eventCount = Long.parseLong(System.getProperty("plugin.simulator.event_count",
                Long.toString(this.eventCount)));
        this.logCount = Long.parseLong(System.getProperty("plugin.simulator.log_count",
                Long.toString(this.logCount)));
        this.measurementCount = Long.parseLong(System.getProperty("plugin.simulator.measurement_count",
                Long.toString(this.measurementCount)));
    }

    public void writeEvent() {
        if (this.eventCount > 0) {
            String s = "_bevent:TITLE|m:MESSAGE|h:SOURCE|s:SENDER|t:info|tags:red,blue,green";
            System.out.println(s);
            System.out.flush();
            eventCount--;
        }
    }

    public void writeMeasurement() {
        if (this.measurementCount > 0) {
            Date d = new Date();
            System.out.println("FOO 1.0 BAR " + d.getTime());
            System.out.flush();
            measurementCount--;
        }
    }

    public void writeLog() {
        if (this.measurementCount > 0) {
            System.err.println("An Error Occurred");
            System.err.flush();
            logCount--;
        }
    }

    public void writeOutput() {
        if (eventCount > 0 || logCount > 0 || measurementCount > 0) {
            this.writeEvent();
            this.writeLog();
            this.writeMeasurement();
        } else {
            System.exit(0);
        }
    }

    public void run() {
        while(true) {
            try {
                this.writeOutput();
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) {
        PluginSimulator sim = new PluginSimulator();
        sim.run();
    }
}
