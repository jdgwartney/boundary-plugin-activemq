package com.bmc.truesight.plugins.activemq;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.*;


public class PluginLogListTest {

    @Test
    public void testConstructor() {
        PluginLogList events = new PluginLogList();
        assertThat(events, is(notNullValue()));
        assertThat(events.size(), is(equalTo(0)));
    }

    @Test
    public void testAddLog() {
        PluginLogList list = new PluginLogList();
        list.add(new PluginLog());
        assertThat(list.size(), is(equalTo(1)));
    }

}
