package com.bmc.truesight.plugins.activemq;

/**
 * Created by dgwartne on 1/25/16.
 */
public class PluginMeasurement {

    private String metric;
    private Number value;
    private String source;
    private long timestamp;

    public PluginMeasurement() {
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append("PluginMeasurement");
        s.append("(\"");
        s.append(this.getMetric());
        s.append("\", ");
        s.append(this.getValue());
        s.append(", ");
        s.append("\"");
        s.append(this.getSource());
        s.append("\"");
        s.append(", ");
        s.append(this.getTimestamp());
        s.append(")");
        return s.toString();
    }
}
