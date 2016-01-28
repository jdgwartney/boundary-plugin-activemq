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
import java.text.ParseException;

public class PluginMeasurement implements PluginParsedItem {

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

    public void parse(String s) throws ParseException {
        ArrayList<String> fields = new ArrayList<String>();

        for (String word : s.split(" ")) {
            fields.add(word);
        }

        if (fields.size() == 4) {
            this.setMetric(fields.get(0));
            this.setValue(Double.parseDouble(fields.get(1)));
            this.setSource(fields.get(2));
            this.setTimestamp(Long.parseLong(fields.get(3)));

        } else if (fields.size() == 3) {
            this.setMetric(fields.get(0));
            this.setValue(Double.parseDouble(fields.get(1)));
            this.setSource(fields.get(2));

        } else if (fields.size() == 2) {
            this.setMetric(fields.get(0));
            this.setValue(Double.parseDouble(fields.get(1)));

        } else {
            throw new ParseException("Insufficient Fields: " + s, 0);
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append(this.getMetric());
        s.append(" ");
        s.append(this.getValue());
        String source = this.getSource();
        if (source != null) {
            s.append(" ");
            s.append(this.getSource());
        }
        Long timestamp = this.getTimestamp();
        if (timestamp != 0) {
            s.append(" ");
            s.append(this.getTimestamp());
        }
        return s.toString();
    }
}
