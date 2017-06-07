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

package com.github.fedorchuck.developers_notification;

import com.github.fedorchuck.developers_notification.antispam.SpamProtection;
import com.github.fedorchuck.developers_notification.configuration.Config;
import com.github.fedorchuck.developers_notification.configuration.Messenger;
import com.github.fedorchuck.developers_notification.http.HttpClient;
import com.github.fedorchuck.developers_notification.json.Json;
import com.github.fedorchuck.developers_notification.monitoring.MonitorProcessor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class contain methods-endpoints for this
 * <a href="https://fedorchuck.github.io/developers-notification/website/index.html">library</a>. Needed environment
 * <a href="https://fedorchuck.github.io/developers-notification/website/index.html#configuration">configuration</a>.
 *
 * <p><b>See Also:</b></p>
 * <ul>
 *     <li><a href="https://fedorchuck.github.io/developers-notification/website/index.html">https://fedorchuck.github.io/developers-notification/website/index.html</a></li>
 *     <li><a href="https://github.com/fedorchuck/developers-notification">https://github.com/fedorchuck/developers-notification</a></li>
 * </ul>
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.0
 */
@SuppressWarnings({"SameParameterValue", "unused"})
public class DevelopersNotification {
    static {
        Thread.currentThread().setName("Developers notification");
    }
    public static final Config config =
            Json.decodeValue(DevelopersNotificationUtil.getEnvironmentVariable("DN"), Config.class);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static boolean monitoringStateAlive = false;

    /**
     * Printing environment configuration to log which needed for this library.
     * <b>Note:</b> if configuration value of field <code>show_whole_log_details</code> is <code>false</code> -
     * will be printed result of method {@link Config#getPublicToString()}
     *
     * @since 0.1.0
     **/
    public static void printConfiguration() {
        if (config.getShowWholeLogDetails()) {
            DevelopersNotificationUtil.printToLogEnvironmentVariable("DN");
            DevelopersNotificationLogger.info(config.toString());
        } else
            DevelopersNotificationLogger.info(config.getPublicToString());
    }

    /**
     * Sending message to chosen destination. <b>Note:</b> Messenger, spam protection and project name
     * will be reading from config.
     *
     * @param description about situation
     * @param throwable which happened. Can be <code>null</code>
     * @since 0.2.0
     **/
    public static void send(final String description, final Throwable throwable) {
        for (Messenger messenger : config.getMessenger()) {
            switch (messenger.getName()){
                case SLACK:
                    SpamProtection.sendIntoMessenger(config.getProtectionFromSpam(),
                            DevelopersNotificationMessenger.SLACK, config.getProjectName(), description, throwable);
                    break;
                case TELEGRAM:
                    SpamProtection.sendIntoMessenger(config.getProtectionFromSpam(),
                            DevelopersNotificationMessenger.TELEGRAM, config.getProjectName(), description, throwable);
                    break;
                case ALL_AVAILABLE:
                    SpamProtection.sendIntoMessenger(config.getProtectionFromSpam(),
                            DevelopersNotificationMessenger.ALL_AVAILABLE, config.getProjectName(), description, throwable);
                    break;
            }
        }
    }

    /**
     * Sending message to chosen destination. <b>Note:</b> Messenger and spam protection will be reading from config.
     *
     * @param projectName where was method called
     * @param description about situation
     * @param throwable which happened. Can be <code>null</code>
     * @since 0.1.0
     **/
    public static void send(final String projectName,
                            final String description,
                            final Throwable throwable) {
        for (Messenger messenger : config.getMessenger()) {
            switch (messenger.getName()){
                case SLACK:
                    SpamProtection.sendIntoMessenger(config.getProtectionFromSpam(),
                            DevelopersNotificationMessenger.SLACK, projectName, description, throwable);
                    break;
                case TELEGRAM:
                    SpamProtection.sendIntoMessenger(config.getProtectionFromSpam(),
                            DevelopersNotificationMessenger.TELEGRAM, projectName, description, throwable);
                    break;
                case ALL_AVAILABLE:
                    SpamProtection.sendIntoMessenger(config.getProtectionFromSpam(),
                            DevelopersNotificationMessenger.ALL_AVAILABLE, projectName, description, throwable);
                    break;
            }
        }
    }

    /**
     * Sending message to chosen destination. <b>Note:</b> Spam protection will be reading from config.
     *
     * @param messengerDestination where the message will be sent.
     * @param projectName where was method called
     * @param description about situation
     * @param throwable which happened. Can be <code>null</code>
     * @since 0.1.0
     **/
    public static void send(final DevelopersNotificationMessenger messengerDestination,
                            final String projectName,
                            final String description,
                            final Throwable throwable) {
        SpamProtection.sendIntoMessenger(config.getProtectionFromSpam(),
                messengerDestination, projectName, description, throwable);
    }

    /**
     * Sending message to chosen destination. <b>Note:</b> Messenger will be reading from config.
     *
     * @param protectionFromSpam is the message will be sent with protection from spam
     * @param projectName where was method called
     * @param description about situation
     * @param throwable which happened. Can be <code>null</code>
     * @since 0.2.0
     **/
    public static void send(final boolean protectionFromSpam,
                            final String projectName,
                            final String description,
                            final Throwable throwable) {
        for (Messenger messenger : config.getMessenger()) {
            switch (messenger.getName()){
                case SLACK:
                    SpamProtection.sendIntoMessenger(protectionFromSpam,
                            DevelopersNotificationMessenger.SLACK, projectName, description, throwable);
                    break;
                case TELEGRAM:
                    SpamProtection.sendIntoMessenger(protectionFromSpam,
                            DevelopersNotificationMessenger.TELEGRAM, projectName, description, throwable);
                    break;
                case ALL_AVAILABLE:
                    SpamProtection.sendIntoMessenger(protectionFromSpam,
                            DevelopersNotificationMessenger.ALL_AVAILABLE, projectName, description, throwable);
                    break;
            }
        }
    }

    /**
     * Launches monitoring process for current application.
     *
     * @return  <code>true</code> if process successfully started;
     *          <code>false</code> otherwise.
     * @since 0.2.0
     **/
    public static boolean monitoringStart(){
        if (monitoringStateAlive) {
            DevelopersNotificationLogger.error("Monitoring process is already running.");
            return false;
        }

        long period = config.getMonitoring().getPeriod();
        TimeUnit unit = config.getMonitoring().getUnit();

        DevelopersNotificationLogger.infoScheduler("Starting scheduler. Period: " + period + "; Unit: " + unit);
        scheduler.scheduleAtFixedRate(new MonitorProcessor(), 0, period, unit);
        monitoringStateAlive = true;

        DevelopersNotificationLogger.infoScheduler("Background process successfully started.");
        return true;
    }

    /**
     * Initiates an orderly shutdown in which previously submitted tasks are executed,
     * but no new tasks will be accepted. Invocation has no additional effect if already shut down.
     *
     * @return  <code>true</code> if process successfully stopped;
     *          <code>false</code> otherwise.
     * @since 0.2.0
     **/
    public static boolean monitoringStop() {
        scheduler.shutdownNow();
        if (scheduler.isShutdown()) {
            monitoringStateAlive = false;
            DevelopersNotificationLogger.infoScheduler("Background process successfully stopped.");
            return true;
        } else {
            DevelopersNotificationLogger.errorScheduler("Background process was not stopped.");
            return false;
        }
    }

    /**
     * Check if the monitoring thread is alive. A thread is alive if it has
     * been started and has not yet died.
     *
     * @return  <code>true</code> if monitoring thread is alive;
     *          <code>false</code> otherwise.
     * @since 0.2.0
     */
    public static boolean isMonitoringStateAlive() {
        return monitoringStateAlive;
    }

}
