package com.bmc.truesight.plugins.activemq;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

public class PluginController implements Runnable {

    private String command;
    private Process plugin;
    private ArrayList<PluginMeasurement> measurements;

    public PluginController(String command) {
        this.command = command;
    }

    public PluginController() {
        this.command = "boundary-meter --lua init.lua";
    }

    public void start() {
        this.measurements = new ArrayList<PluginMeasurement>();
        Thread pluginThread = new Thread(this);
        pluginThread.start();
    }

    public void parseMeasurement(String s) {
        ArrayList<String> fields = new ArrayList<String>();

        for (String word : s.split(" ")) {
            fields.add(word);
        }

        PluginMeasurement m = new PluginMeasurement();

        if (fields.size() == 4) {
            m.setMetric(fields.get(0));
            m.setValue(Double.parseDouble(fields.get(1)));
            m.setSource(fields.get(2));
            m.setTimestamp(Integer.parseInt(fields.get(3)));

        } else if (fields.size() == 3) {
            m.setMetric(fields.get(0));
            m.setValue(Double.parseDouble(fields.get(1)));
            m.setSource(fields.get(2));

        } else if (fields.size() == 2) {

        } else {

        }

        System.out.println(m);
        this.measurements.add(m);
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
//                System.out.println(s);
                if (s.matches("^_bevent.*$")) {
                    System.out.println("EVENT");
                } else {
                    this.parseMeasurement(s);
                }
            }

            // read any errors from the attempted command

            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            System.exit(0);
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
}
