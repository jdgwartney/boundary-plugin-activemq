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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.ListIterator;

public class PluginEvent implements PluginParsedItem {

    private String appData;
    private String appDataType;
    private String eventType;
    private String message;
    private String[] tags;
    private String title;
    private String sender;
    private String source;

    private ArrayList<String> eventTypes;

    public PluginEvent() {
        this.eventTypes = new ArrayList<String>();
        this.eventTypes.add("info");
        this.eventTypes.add("warn");
        this.eventTypes.add("error");
        this.eventTypes.add("critical");

    }

    public void parse(String s) throws ParseException {
        ArrayList<String> fields = new ArrayList<String>();

        for (String word : s.split("\\|")) {
            fields.add(word);
        }

        if (fields.get(0).matches("^_bevent.*\\$")) {
            throw new ParseException("Not a plugin event string", 0);
        }

        ListIterator<String> fieldList = fields.listIterator();
        while (fieldList.hasNext()) {
            String field = fieldList.next();
            String[] pair = field.split(":");

            String prefix = pair[0];
            String value = pair[1];

            if (prefix.equals("_bevent")) {
                this.title = pair[1];
            } else if (prefix.equals("at")) {
                this.setAppDataType(value);
            } else if (prefix.equals("ad")) {
                this.setAppData(value);
            } else if (prefix.equals("m")) {
                this.setMessage(value);

            } else if (prefix.equals("t")) {
                this.setEventType(value);
            } else if (prefix.equals("h")) {
                this.setSource(value);
            } else if (prefix.equals("s")) {
                this.setSender(value);
            } else if (prefix.equals("tags")) {
                String [] tags = value.split(",");
                this.setTags(tags);
            } else {
                throw new ParseException("Unknown prefix: \"" + prefix + "\"", 0);
            }
        }
    }

    public String getEventType() {
        return this.eventType;
    }

    public void setEventType(String eventType) throws IllegalArgumentException {
        if (eventTypes.contains(eventType)) {
            this.eventType = eventType;

        } else {
            throw new IllegalArgumentException("Illegal event type: " + eventType);
        }
    }

    public String getAppData() {
        return this.appData;
    }

    public void setAppData(String appData) {
        this.appData = appData;
    }

    public String getAppDataType() {
        return this.appDataType;
    }

    public void setAppDataType(String appDataType) {
        this.appDataType = appDataType;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String[] getTags() {
        return this.tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags.clone();
    }


}
