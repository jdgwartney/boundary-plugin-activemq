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

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;

import java.text.ParseException;

public class PluginEventTest {

    PluginEvent event;

    @Before
    public void create() {
        this.event = new PluginEvent();
    }

    @Test
    public void testDefaultConstructor(){
        assertThat(this.event, is(notNullValue()));
    }

    @Test
    public void testAppData() {
        String expectedAppData = "FOOOOOOOOOOOOOOBARRRRRRRR";
        this.event.setAppData(expectedAppData);
        assertThat(this.event.getAppData(), is(equalTo(expectedAppData)));
    }

    @Test
    public void testAppDataType() {
        String expectedAppDataType = "text/html";
        this.event.setAppDataType(expectedAppDataType);
        assertThat(this.event.getAppDataType(), is(equalTo(expectedAppDataType)));
    }

    @Test
    public void testEventType() {
        String expectedEventType = "info";
        this.event.setEventType(expectedEventType);
        assertThat(event.getEventType(), is(equalTo(expectedEventType)));
    }

    @Test
    public void testEventTypes() {
        String expectedEventType = "info";
        this.event.setEventType(expectedEventType);
        assertThat(event.getEventType(), is(equalTo(expectedEventType)));

        expectedEventType = "warn";
        this.event.setEventType(expectedEventType);
        assertThat(event.getEventType(), is(equalTo(expectedEventType)));

        expectedEventType = "error";
        this.event.setEventType(expectedEventType);
        assertThat(event.getEventType(), is(equalTo(expectedEventType)));

        expectedEventType = "critical";
        this.event.setEventType(expectedEventType);
        assertThat(event.getEventType(), is(equalTo(expectedEventType)));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testBadEventType() throws IllegalArgumentException {
        String eventType = "foo";
        this.event.setEventType(eventType);
    }

    @Test
    public void testMessage() {
        String expectedMessage = "Hello World!";
        this.event.setMessage(expectedMessage);
        assertThat(this.event.getMessage(), is(equalTo(expectedMessage)));
    }

    @Test
    public void testSource() {
        String expectedSource = "FOOBAR";
        this.event.setSource(expectedSource);
        assertThat(this.event.getSource(), is(equalTo(expectedSource)));
    }

    @Test
    public void testSender() {
        String expectedSender = "BARFOO";
        this.event.setSender(expectedSender);
        assertThat(this.event.getSender(), is(equalTo(expectedSender)));
    }

    @Test
    public void testTags() {
        String [] expectedTags = new String[]{"red", "green", "blue"};
        this.event.setTags(expectedTags);
        assertThat(this.event.getTags(), is(equalTo(expectedTags)));
    }

    @Test
    public void testTagsModify() {
        String [] expectedTags = new String[]{"red", "green", "blue"};
        this.event.setTags(expectedTags);
        String [] originalTags = expectedTags.clone();
        expectedTags[0] = "FOO";
        assertThat(this.event.getTags(), is(equalTo(originalTags)));
    }

    @Test
    public void testParseEvent() throws ParseException {
        String s = "_bevent:Truesight Pulse ActiveMQ Plugin version 0.9.3-localhost Status|m:Up|h:localhost|s:localhost|t:info|tags:lua,plugin,activemq|at:text/html|ad:someBase64Htmlblob";

        PluginEvent e = new PluginEvent();
        e.parse(s);

        assertThat(e.getEventType(),is(equalTo("info")));
        assertThat(e.getMessage(),is(equalTo("Up")));
        assertThat(e.getSender(),is(equalTo("localhost")));
        assertThat(e.getSource(),is(equalTo("localhost")));
        assertThat(e.getTags(),is(equalTo(new String[]{"lua", "plugin", "activemq"})));
        assertThat(e.getTitle(),is(equalTo("Truesight Pulse ActiveMQ Plugin version 0.9.3-localhost Status")));
    }

    @Test
    public void testParseEmbeddedNewLines() throws ParseException {
        String s = "_bevent:Truesight Pulse\nActiveMQ Plugin\nversion 0.9.3-localhost Status";
        PluginEvent e = new PluginEvent();
        e.parse(s);
        assertThat(e.getTitle(),is(equalTo("Truesight Pulse\nActiveMQ Plugin\nversion 0.9.3-localhost Status")));
    }
}
