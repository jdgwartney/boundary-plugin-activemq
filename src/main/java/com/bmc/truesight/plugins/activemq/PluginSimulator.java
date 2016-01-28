package com.bmc.truesight.plugins.activemq;

import java.util.Date;

public class PluginSimulator {

    public PluginSimulator() {

    }

    public void writeOutput() {
        Date d = new Date();
        String s = "_bevent:TITLE|m:MESSAGE|h:SOURCE|s:SENDER|t:info|tags:red,blue,green";
        System.out.println(s);
        System.out.println("FOO 1.0 BAR " + d.getTime());
        System.out.flush();
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
