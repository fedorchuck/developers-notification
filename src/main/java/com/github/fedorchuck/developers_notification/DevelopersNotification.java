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

import com.github.fedorchuck.developers_notification.domainmodel.Config;
import com.github.fedorchuck.developers_notification.domainmodel.Messenger;
import com.github.fedorchuck.developers_notification.http.HttpClient;
import com.github.fedorchuck.developers_notification.integrations.Integration;
import com.github.fedorchuck.developers_notification.integrations.slack.SlackImpl;
import com.github.fedorchuck.developers_notification.integrations.telegram.TelegramImpl;
import com.github.fedorchuck.developers_notification.json.Json;
import com.github.fedorchuck.developers_notification.monitoring.MonitorProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class contain methods-endpoints for this library. Needed environment configuration.
 *
 * Supported environment configuration:
 * <ul>
 * <li>DN_MESSENGER - where the message will be sent;
 * possible values you can see in {@link DevelopersNotificationMessenger};
 * required if you use method <code>send</code> with signature
 * {@link DevelopersNotification#send(String, String, Throwable)} </li>
 * <li>DN_SLACK_TOKEN - access key; required if you use Slack messages</li>
 * <li>DN_SLACK_CHANNEL - destination chat; required if you use Slack messages</li>
 * <li>DN_TELEGRAM_TOKEN - access key; required if you use Telegram messages</li>
 * <li>DN_TELEGRAM_CHANNEL - destination chat; required if you use Telegram messages</li>
 * <li>DN_USER_AGENT - user agent for {@link HttpClient#USER_AGENT};
 * is not required; default value is <code>Mozilla/5.0</code></li>
 * <li>DN_CONNECT_TIMEOUT - for {@link HttpClient#CONNECT_TIMEOUT};
 * is not required; default value is <code>5000</code></li>
 * <li>DN_SHOW_WHOLE_LOG_DETAILS - receive {@link Boolean} value; if true - at log will be
 * printed Information containing passwords; default value is <code>false</code> </li>
 * </ul>
 * Required configuration which will be using for sending messages.
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.0
 */
public class DevelopersNotification {
    public static final Config config = Json.decodeValue(DevelopersNotificationUtil.getEnvironmentVariable("DN"), Config.class);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * Printing environment configuration which needed for this library.
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
     * Sending message to chosen destination.
     *
     * @param projectName where was method called. Can be <code>null</code>
     * @param description about situation. Can be <code>null</code>
     * @param throwable which happened. Can be <code>null</code>
     * @throws IllegalArgumentException if <code>MESSENGER</code> has invalid value or is null or empty.
     *
     * @since 0.1.0
     **/
    public static void send(final String projectName,
                            final String description, final Throwable throwable) {
        for (Messenger messenger : config.getMessenger()) {
            switch (messenger.getName()){
                case SLACK:
                    send(DevelopersNotificationMessenger.SLACK, projectName, description, throwable);
                    break;
                case TELEGRAM:
                    send(DevelopersNotificationMessenger.TELEGRAM, projectName, description, throwable);
                    break;
                case ALL_AVAILABLE:
                    send(DevelopersNotificationMessenger.ALL_AVAILABLE, projectName, description, throwable);
                    break;
            }
        }
    }

    /**
     * Sending message to chosen destination.
     *
     * @param messengerDestination - where the message will be sent.
     * @param projectName where was method called. Can be <code>null</code>
     * @param description about situation. Can be <code>null</code>
     * @param throwable which happened. Can be <code>null</code>
     * @throws IllegalArgumentException if <code>MESSENGER</code> has invalid value or is null or empty.
     *
     * @since 0.1.0
     **/
    public static void send(final DevelopersNotificationMessenger messengerDestination, final String projectName,
                            final String description, final Throwable throwable) {
        new Thread(new Runnable() {
            public void run() {
                Thread.currentThread().setName("Send developers notification to " + messengerDestination.name());
                List<Integration> integrations = new ArrayList<Integration>(0);
                switch (messengerDestination){
                    case SLACK:
                        integrations.add(new SlackImpl());
                        break;
                    case TELEGRAM:
                        integrations.add(new TelegramImpl());
                        break;
                    case ALL_AVAILABLE:
                        integrations.add(new SlackImpl());
                        integrations.add(new TelegramImpl());
                        break;
                }

                for (Integration integration : integrations) {
                    integration.sendMessage(integration.generateMessage(projectName, description, throwable));
                    DevelopersNotificationLogger.infoTaskCompleted(integration.getClass().getSimpleName());
                }
            }
        }).start();
    }

    /**
     * Launches monitoring process for current application.
     *
     * @since 0.2.0
     **/
    public static void monitoringStart(){
        long period = config.getMonitoring().getPeriod();
        TimeUnit unit = config.getMonitoring().getUnit();

        DevelopersNotificationLogger.infoScheduler("Starting scheduler. Period: " + period + "; Unit: " + unit);
        scheduler.scheduleAtFixedRate(new MonitorProcessor(), 0, period, unit);

        DevelopersNotificationLogger.infoScheduler("Background process successfully started.");
    }

    /**
     * Initiates an orderly shutdown in which previously submitted tasks are executed,
     * but no new tasks will be accepted. Invocation has no additional effect if already shut down.
     *
     * @since 0.2.0
     **/
    public static void monitoringStop() {
        scheduler.shutdownNow();
        if (scheduler.isShutdown()) {
            DevelopersNotificationLogger.infoScheduler("Background process successfully stopped.");
        } else {
            DevelopersNotificationLogger.errorScheduler("Background process was not stopped.");
        }
    }
}
