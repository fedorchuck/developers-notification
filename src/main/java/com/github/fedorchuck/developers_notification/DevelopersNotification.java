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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fedorchuck.developers_notification.http.HttpClient;
import com.github.fedorchuck.developers_notification.integrations.Integration;
import com.github.fedorchuck.developers_notification.integrations.slack.SlackImpl;
import com.github.fedorchuck.developers_notification.integrations.telegram.TelegramImpl;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

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
 * </ul>
 * Required configuration which will be using for sending messages.
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.0
 */
public class DevelopersNotification {

    /**
     * Printing environment configuration which needed for this library.
     *
     * @since 0.1.0
     **/
    public static void printConfiguration() {
        DevelopersNotificationUtil.printToLogEnvironmentVariable("DN_MESSENGER");
        HttpClient.printConfiguration();
        TelegramImpl.printConfiguration();
        SlackImpl.printConfiguration();
    }

    /**
     * Sending message to chosen destination.
     *
     * @param projectName where was method called. Can be <code>null</code>
     * @param description about situation. Can be <code>null</code>
     * @param throwable which happened. Can be <code>null</code>
     * @throws IllegalArgumentException if <code>DN_MESSENGER</code> has invalid value or is null or empty.
     *
     * @since 0.1.0
     **/
    public static void send(final String projectName,
                            final String description, final Throwable throwable) {
        String stringMessengerDestination =
                DevelopersNotificationUtil.getEnvironmentVariable("DN_MESSENGER");

        if (Strings.isNullOrEmpty(stringMessengerDestination)){
            DevelopersNotificationLogger.errorWrongConfig();
            throw new IllegalArgumentException("DN_MESSENGER is null or empty.");
        }

        if (DevelopersNotificationMessenger.ALL_AVAILABLE.name().equals(stringMessengerDestination)){
            send(DevelopersNotificationMessenger.ALL_AVAILABLE, projectName, description, throwable);
        } else if (DevelopersNotificationMessenger.SLACK.name().equals(stringMessengerDestination)){
            send(DevelopersNotificationMessenger.SLACK, projectName, description, throwable);
        } else if (DevelopersNotificationMessenger.TELEGRAM.name().equals(stringMessengerDestination)){
            send(DevelopersNotificationMessenger.TELEGRAM, projectName, description, throwable);
        } else {
            DevelopersNotificationLogger.errorWrongConfig(stringMessengerDestination);
            throw new IllegalArgumentException("DN_MESSENGER has invalid value: "
                    + stringMessengerDestination);
        }
    }

    /**
     * Sending message to chosen destination.
     *
     * @param messengerDestination - where the message will be sent.
     * @param projectName where was method called. Can be <code>null</code>
     * @param description about situation. Can be <code>null</code>
     * @param throwable which happened. Can be <code>null</code>
     * @throws IllegalArgumentException if <code>DN_MESSENGER</code> has invalid value or is null or empty.
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
                    try {
                        integration.sendMessage(integration.generateMessage(projectName, description, throwable));
                    } catch (JsonProcessingException ex) {
                        DevelopersNotificationLogger.errorTaskFailed(integration.getClass().getName(), ex);
                    }
                    DevelopersNotificationLogger.infoTaskCompleted(integration.getClass().getSimpleName());
                }
            }
        }).start();
    }
}
