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

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
public class PhysicalResourceTest {

    @Test
    public void testGetUsage() {
        PhysicalResourceUsage physicalResourceUsage = PhysicalResource.getUsage();
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

    private void checkFields(Object instance) {
        for (Method method : instance.getClass().getDeclaredMethods()) {
            if (method.getName().startsWith("get")
                    && Modifier.isPublic(method.getModifiers())) {
                Double value = 0.0;
                try {
                    value = Double.valueOf((Long)  method.invoke(instance));
                } catch (Exception e) {
                    Assert.fail(method.getName() + " failed with: " + e);
                }
                if (value == 0)
                    Assert.fail(method.getName() + " is 0.");
            }
        }
    }

}