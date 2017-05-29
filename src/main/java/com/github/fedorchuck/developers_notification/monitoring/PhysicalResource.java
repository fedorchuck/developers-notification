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

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.2.0
 */
public class PhysicalResource {

    public static PhysicalResourceUsage getUsage() {
        PhysicalResourceUsage physicalResourceUsage = new PhysicalResourceUsage();
        Runtime runtime = Runtime.getRuntime();
        JVM jvm = new JVM();

        jvm.setMaxRamMemory(
                runtime.maxMemory() == Long.MAX_VALUE
                        ?
                        ((com.sun.management.OperatingSystemMXBean)
                                ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize()
                        :
                        runtime.maxMemory()
        );
        jvm.setUsedRamMemory(runtime.totalMemory() - runtime.freeMemory());
        jvm.setTotalRamMemory(runtime.totalMemory());
        jvm.setFreeRamMemory(runtime.freeMemory());
        physicalResourceUsage.setJvm(jvm);

        /* Get a list of all filesystem roots on this system */
        File[] roots = File.listRoots();
        List<Disk> disks = new ArrayList<Disk>(0);
        /*For each filesystem root, get info */
        for (File root : roots) {
            disks.add(
                    Disk.builder()
                            .totalDiskSpace(root.getTotalSpace())
                            .freeDiskSpace(root.getFreeSpace())
                            .usableDiskSpace(root.getUsableSpace())
                        .build()
            );
        }
        physicalResourceUsage.setDisks(disks);

        return physicalResourceUsage;
    }
}
