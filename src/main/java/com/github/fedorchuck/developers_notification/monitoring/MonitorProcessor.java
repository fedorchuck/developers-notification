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

import com.github.fedorchuck.developers_notification.DevelopersNotification;
import com.github.fedorchuck.developers_notification.DevelopersNotificationLogger;
import com.github.fedorchuck.developers_notification.antispam.FrequencyOfSending;
import com.github.fedorchuck.developers_notification.antispam.MessageTypes;
import com.github.fedorchuck.developers_notification.antispam.SpamProtection;
import com.github.fedorchuck.developers_notification.configuration.Monitoring;
import com.github.fedorchuck.developers_notification.helpers.Constants;
import com.github.fedorchuck.developers_notification.helpers.Lifetime;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * It collects, analyzes and produces a corresponding reaction to the results of monitoring.
 * Also, it implementing interface {@link Runnable}
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.2.0
 */
public class MonitorProcessor implements Runnable {
    private static Monitoring monitoringConfig = DevelopersNotification.config.getMonitoring();
    private final Lifetime<List<Disk>> monitoring;

    public MonitorProcessor() {
        if (monitoringConfig.getPeriod() == null) {
            DevelopersNotificationLogger.errorTaskFailed(
                    "MonitorProcessor", new IllegalArgumentException("Check config. Period can not be null.")
            );
            throw new IllegalArgumentException("Check config. Period can not be null.");
        }
        long updateTimerInterval = monitoringConfig.getUnit().toSeconds(monitoringConfig.getPeriod());
        long timeToLiveObject = updateTimerInterval * 10;
        monitoring = new Lifetime<List<Disk>>(timeToLiveObject, updateTimerInterval);
    }

    /**
     * Triggers the collection and analysis of data, and sends messages when necessary
     *
     * @since 0.2.0
     */
    @Override
    public void run() {
        Thread.currentThread().setName(Constants.THREAD_NAME_MONITORING);

        PhysicalResourceUsage currentMonitoringResult = getPhysicalResourceUsage();

        if ((monitoringConfig.getMaxRam() != null) && (FrequencyOfSending.canSendMessage(MessageTypes.RAM_LIMIT))) {
            handleRamLimitUsage(currentMonitoringResult);
        }

        List<Disk> oldestMonitoringResult = monitoring.getOldest();
        monitoring.put(currentMonitoringResult.getDisks());
        if (oldestMonitoringResult == null)
            return;

        for (Disk currentUsage : currentMonitoringResult.getDisks()) {
            if ((monitoringConfig.getMaxDisk() != null) && (FrequencyOfSending.canSendMessage(MessageTypes.DISK_LIMIT)))
                handleDiskLimitUsage(currentUsage);

            if ((monitoringConfig.getDiskConsumptionRate() != null) && (FrequencyOfSending.canSendMessage(MessageTypes.DISK_CONSUMPTION_RATE)))
                handleDiskConsumptionRate(currentUsage, oldestMonitoringResult);
        }
    }

    /**
     * Make analysis of ram usage, and sends messages when necessary
     *
     * @param currentMonitoringResult results of monitoring
     * @since 0.2.0
     */
    private void handleRamLimitUsage(PhysicalResourceUsage currentMonitoringResult) {
        JVM currentUsageJVM = currentMonitoringResult.getJvm();
        double currentUsageRamInPercent =
                getUsageInPercent(currentUsageJVM.getUsedRamMemory(), currentUsageJVM.getTotalRamMemory());

        if (currentUsageRamInPercent >= monitoringConfig.getMaxRam()) {
            SpamProtection.sendIntoMessenger(
                    false,
                    AlertMessages.getAlertRAMLimitMessage(
                            currentUsageJVM.getUsedRamMemory(),
                            currentUsageRamInPercent,
                            monitoringConfig.getMaxRam()
                    )
            );
            FrequencyOfSending.messageSent(MessageTypes.RAM_LIMIT);
        }
    }

    /**
     * Make analysis of disk consumption rate, and sends messages when necessary
     *
     * @param currentUsage results of disk monitoring
     * @param oldestMonitoringResult oldest monitoring results
     * @since 0.2.0
     */
    private void handleDiskConsumptionRate(Disk currentUsage, List<Disk> oldestMonitoringResult) {
        long rate;
        double rateDiskInPercent;
        for (Disk oldestUsage : oldestMonitoringResult) {
            if (currentUsage.getDiskName().equals(oldestUsage.getDiskName())) {

                rate = currentUsage.getUsableDiskSpace() - oldestUsage.getUsableDiskSpace();
                if (rate <= 0)
                    break;
                rateDiskInPercent =
                        getUsageInPercent(rate, currentUsage.getTotalDiskSpace());

                if (rateDiskInPercent >= monitoringConfig.getDiskConsumptionRate()) {
                    SpamProtection.sendIntoMessenger(
                            false,
                            AlertMessages.getAlertDiskRateMessage(
                                    currentUsage.getDiskName(),
                                    rate,
                                    rateDiskInPercent,
                                    monitoringConfig.getDiskConsumptionRate()
                            )
                    );
                    FrequencyOfSending.messageSent(MessageTypes.DISK_CONSUMPTION_RATE);
                }
            }
        }
    }

    /**
     * Make analysis of disk usage, and sends messages when necessary
     *
     * @param currentUsage results of disk monitoring
     * @since 0.2.0
     */
    private void handleDiskLimitUsage(Disk currentUsage) {
        double currentUsageDiskInPercent =
                getUsageInPercent(currentUsage.getUsableDiskSpace(), currentUsage.getTotalDiskSpace());

        if (currentUsageDiskInPercent >= monitoringConfig.getMaxDisk()) {
            SpamProtection.sendIntoMessenger(
                    false,
                    AlertMessages.getAlertDiskLimitMessage(
                            currentUsage.getDiskName(),
                            currentUsage.getUsableDiskSpace(),
                            currentUsageDiskInPercent,
                            monitoringConfig.getMaxDisk()
                    )
            );
            FrequencyOfSending.messageSent(MessageTypes.DISK_LIMIT);
        }
    }

    /**
     * Calculates percentages of usage
     * <pre><code>result = usage * 100 / total</code></pre>
     *
     * @param usage for which need to calculate percent
     * @param total what is 100%
     * @return usage value in percent
     * @since 0.2.0
     **/
    double getUsageInPercent(long usage, long total) {
        return usage * 100.0 / total;
    }

    PhysicalResourceUsage getPhysicalResourceUsage() {
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

        /* Get a list of all filesystem roots on this system */
        File[] roots = File.listRoots();
        List<Disk> disks = new ArrayList<Disk>(0);
        /*For each filesystem root, get info */
        for (File root : roots) {
            disks.add(
                    Disk.builder()
                            .totalDiskSpace(root.getTotalSpace())
                            .freeDiskSpace(root.getFreeSpace())
                            .usableDiskSpace(root.getTotalSpace() - root.getFreeSpace())
                            .diskName(root.getPath())
                            .build()
            );
        }

        return new PhysicalResourceUsage(jvm, disks);
    }
}
