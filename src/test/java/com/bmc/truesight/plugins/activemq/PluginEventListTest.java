package com.bmc.truesight.plugins.activemq;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.*;


public class PluginEventListTest {

    @Test
    public void testConstructor() {
        PluginEventList events = new PluginEventList();
        assertThat(events, is(notNullValue()));
        assertThat(events.size(), is(equalTo(0)));
    }

    @Test
    public void testAddMeasurement() {
        PluginEventList list = new PluginEventList();
        list.add(new PluginEvent());
        assertThat(list.size(), is(equalTo(1)));
    }

}
