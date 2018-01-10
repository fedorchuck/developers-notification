/*
 * Copyright 2017 Volodymyr Fedorchuk <vl.fedorchuck@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.fedorchuck.developers_notification.monitoring;

import com.github.fedorchuck.developers_notification.Utils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
public class MonitorProcessorTest {
    @Before
    public void setUp() {
        String stringHttpConfig = "{\"monitoring\":{\"period\":5,\"unit\":\"seconds\",\"max_ram\":90,\"max_disk\": 90,\"disk_consumption_rate\":2}}";
        Utils.setConfig(stringHttpConfig);
    }

    @Test
    public void testGetPhysicalResourceUsage() {
        PhysicalResourceUsage physicalResourceUsage = new MonitorProcessor().getPhysicalResourceUsage();
        JVM jvm = physicalResourceUsage.getJvm();
        List<Disk> disks = physicalResourceUsage.getDisks();
        if (jvm==null)
            Assert.fail("JVM result is null");
        if (disks.size()==0)
            Assert.fail("Disk result is 0");
        if (disks.isEmpty())
            Assert.fail("Disk result is empty");

        checkFields(jvm);
        for (Disk disk : disks)
            checkFields(disk);
    }

    @Test
    public void testGetUsageInPercent() {
        Assert.assertEquals(5, new MonitorProcessor().getUsageInPercent(5,100),0);
    }

    private void checkFields(Object instance) {
        for (Method method : instance.getClass().getDeclaredMethods()) {
            if (method.getName().startsWith("get")
                    && Modifier.isPublic(method.getModifiers())) {
                Object value = null;
                try {
                    value = method.invoke(instance);
                } catch (Exception e) {
                    Assert.fail(method.getName() + " failed with: " + e);
                }
                if (value == null)
                    Assert.fail(method.getName() + " is 0.");
            }
        }
    }

    @After
    public void tearDown() {
        Utils.resetConfig();
    }
}