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

import com.github.fedorchuck.developers_notification.antispam.MessageTypes;
import com.github.fedorchuck.developers_notification.antispam.SpamProtection;
import com.github.fedorchuck.developers_notification.configuration.Config;
import com.github.fedorchuck.developers_notification.helpers.InternalUtil;
import com.github.fedorchuck.developers_notification.integrations.Integration;
import com.github.fedorchuck.developers_notification.model.Task;
import com.github.fedorchuck.developers_notification.monitoring.MonitorProcessor;
import com.github.fedorchuck.dnjson.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class contain methods-endpoints for this
 * <a href="https://fedorchuck.github.io/developers-notification/website/index.html">library</a>. Needed environment
 * <a href="https://fedorchuck.github.io/developers-notification/website/index.html#setup">configuration</a>.
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
@SuppressWarnings({"SameParameterValue", "UnusedReturnValue"})
public class DevelopersNotification {
    static {
        Thread.currentThread().setName("Developers notification");
    }

    private static Config config;
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
        if (!configurationExist()) {
            DevelopersNotificationLogger.fatalConfigNotFound();
            return;
        }

        if (config.getShowWholeLogDetails()) {
            DevelopersNotificationUtil.printToLogEnvironmentVariable("DN");
            DevelopersNotificationLogger.info(config.getPrivateToString());
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
        if (!configurationExist()) {
            DevelopersNotificationLogger.fatalConfigNotFound();
            return;
        }

        List<Task> tasks = new ArrayList<Task>(0);
        for (Integration integration : InternalUtil.getIntegrations()) {
            tasks.add(InternalUtil.generateTask(config.getProjectName(), description, throwable, integration));
        }

        for (Task task : tasks) {
            SpamProtection.sendIntoMessenger(config.getProtectionFromSpam(), MessageTypes.USERS_MESSAGE, task);
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
        if (!configurationExist()) {
            DevelopersNotificationLogger.fatalConfigNotFound();
            return;
        }

        List<Task> tasks = new ArrayList<Task>(0);
        for (Integration integration : InternalUtil.getIntegrations()) {
            tasks.add(InternalUtil.generateTask(projectName, description, throwable, integration));
        }

        for (Task task : tasks) {
            SpamProtection.sendIntoMessenger(config.getProtectionFromSpam(), MessageTypes.USERS_MESSAGE, task);
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
        if (!configurationExist()) {
            DevelopersNotificationLogger.fatalConfigNotFound();
            return;
        }

        List<Task> tasks = new ArrayList<Task>(0);
        for (Integration integration : InternalUtil.getIntegrations(messengerDestination)) {
            tasks.add(InternalUtil.generateTask(projectName, description, throwable, integration));
        }

        for (Task task : tasks) {
            SpamProtection.sendIntoMessenger(config.getProtectionFromSpam(), MessageTypes.USERS_MESSAGE, task);
        }
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
        if (!configurationExist()) {
            DevelopersNotificationLogger.fatalConfigNotFound();
            return;
        }

        List<Task> tasks = new ArrayList<Task>(0);
        for (Integration integration : InternalUtil.getIntegrations()) {
            tasks.add(InternalUtil.generateTask(projectName, description, throwable, integration));
        }

        for (Task task : tasks) {
            SpamProtection.sendIntoMessenger(protectionFromSpam, MessageTypes.USERS_MESSAGE, task);
        }
    }

    /**
     * Launches monitoring process for current application.
     *
     * @return  <code>true</code> if process successfully started;
     *          <code>false</code> otherwise.
     * @since 0.2.0
     **/
    public static boolean monitoringStart() {
        if (!configurationExist()) {
            DevelopersNotificationLogger.fatalConfigNotFound();
            return false;
        }

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

    /**
     * Return configuration for Developers Notification library.
     *
     * <p><b>Note: </b></p>
     * <p>If configuration is missed - configuration will be loaded from environment variable <code>DN</code>.</p>
     * <p>If configuration was loaded before - new configuration will not be uploaded.</p>
     *
     * @return configuration for Developers Notification library
     * @since 0.3.0
     */
    public static Config getConfiguration() {
        if (config != null)
            return config;

        if (!DevelopersNotificationUtil.isNullOrEmpty(DevelopersNotificationUtil.getEnvironmentVariable("DN")))
            config = Json.decodeValue(DevelopersNotificationUtil.getEnvironmentVariable("DN"), Config.class);
        else
            config = null;

        return config;
    }

    /**
     * Check is configuration for Developers Notification library exist.
     *
     * <p><b>Note: </b></p>
     * <p>If configuration is missed - configuration will be loaded from environment variable <code>DN</code>.</p>
     * <p>If configuration was loaded before - new configuration will not be uploaded.</p>
     *
     * @return  <code>true</code> if configuration exist
     *          <code>false</code> otherwise.
     * @since 0.3.0
     */
    public static boolean configurationExist() {
        if (config == null) {
            getConfiguration();
            return config != null;
        }

        return true;
    }
}
