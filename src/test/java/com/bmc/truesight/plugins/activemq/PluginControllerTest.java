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
import org.junit.*;

public class PluginControllerTest {

    @Test
    public void testDefaultConstructor(){
        PluginController p = new PluginController();
        assertNotNull(p);
    }

    @Test
    public void testConstructor() {
        PluginController p = new PluginController("boundary-meter --lua init.lua");
        assertNotNull(p);
    }

    @Test
    public void testEventRegEx() {
        String s = "_bevent:Boundary ActiveMQ Plugin version 0.9.3-localhost Status|m:Up|h:localhost|s:localhost|t:info|tags:lua,plugin,activemq";
        String regEx = "^_bevent.*$";
        assertTrue(s.matches(regEx));
    }

    @Test
    public void testDestroy() {
        try {
            PluginController p = new PluginController("boundary-meter --lua init.lua");
            p.start();
            Thread.sleep(5000);
            p.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}