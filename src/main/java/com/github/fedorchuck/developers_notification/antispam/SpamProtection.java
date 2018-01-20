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

package com.github.fedorchuck.developers_notification.antispam;

import com.github.fedorchuck.developers_notification.DevelopersNotification;
import com.github.fedorchuck.developers_notification.DevelopersNotificationLogger;
import com.github.fedorchuck.developers_notification.DevelopersNotificationMessenger;
import com.github.fedorchuck.developers_notification.configuration.Messenger;
import com.github.fedorchuck.developers_notification.helpers.Constants;
import com.github.fedorchuck.developers_notification.integrations.Integration;
import com.github.fedorchuck.developers_notification.integrations.slack.SlackImpl;
import com.github.fedorchuck.developers_notification.integrations.telegram.TelegramImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Class contains connection between mechanism witch avoiding spam and mechanism sending messages.
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.2.0
 */
@SuppressWarnings("SameParameterValue")
public class SpamProtection {

    /**
     * It provide sending messages into messengers.
     * <p><b>Note:</b> all needed data will be getting from JSON configuration</p>
     *
     * @param protectionFromSpam is needed protection from spam
     * @param description what happened
     * @since 0.2.0
     **/
    public static void sendIntoMessenger(final boolean protectionFromSpam, final String description) {
        for (Messenger messenger : DevelopersNotification.config.getMessenger()) {
            switch (messenger.getName()) {
                case SLACK:
                    sendIntoMessenger(protectionFromSpam, DevelopersNotificationMessenger.SLACK,
                            DevelopersNotification.config.getProjectName(), description, null);
                    break;
                case TELEGRAM:
                    sendIntoMessenger(protectionFromSpam, DevelopersNotificationMessenger.TELEGRAM,
                            DevelopersNotification.config.getProjectName(), description, null);
                    break;
                case ALL_AVAILABLE:
                    sendIntoMessenger(protectionFromSpam, DevelopersNotificationMessenger.ALL_AVAILABLE,
                            DevelopersNotification.config.getProjectName(), description, null);
                    break;
            }
        }
    }

    /**
     * It provide sending messages to chosen destination.
     *
     * @param protectionFromSpam is needed protection from spam
     * @param messengerDestination where the message will be sent
     * @param projectName where was method called
     * @param description about situation
     * @param throwable which happened. Can be <code>null</code>
     * @since 0.2.0
     **/
    public static void sendIntoMessenger(final boolean protectionFromSpam,
                                         final DevelopersNotificationMessenger messengerDestination,
                                         final String projectName,
                                         final String description,
                                         final Throwable throwable) {
        Thread t = new Thread(
                Constants.THREAD_GROUP,
                new Runnable() {
                    public void run() {
                        Thread.currentThread().setName("Send developers notification to " + messengerDestination.name());
                        SentMessage sentMessage =
                                new SentMessage(MessageTypes.USERS_MESSAGE, messengerDestination, projectName, description);

                        if (protectionFromSpam) {
                            if (!FrequencyOfSending.canSendMessage(sentMessage)) {
                                DevelopersNotificationLogger.infoTryToSentDuplicateMessage(sentMessage);
                                return; //duplicate ignore
                            }

                            FrequencyOfSending.messageSent(sentMessage);
                            DevelopersNotificationLogger.infoSentMessage(sentMessage);
                            sendIntoMessenger(messengerDestination, projectName, description, throwable);
                        } else {
                            sendIntoMessenger(messengerDestination, projectName, description, throwable);
                        }
                    }
                },
                Constants.THREAD_NAME_SENDING
        );
        t.setDaemon(true);
        t.start();
    }

    /**
     * Sent messages
     *
     * @param messengerDestination where the message will be sent
     * @param projectName where was method called
     * @param description about situation
     * @param throwable which happened. Can be <code>null</code>
     * @since 0.2.0
     **/
    private static void sendIntoMessenger(final DevelopersNotificationMessenger messengerDestination,
                                          final String projectName,
                                          final String description,
                                          final Throwable throwable) {
        List<Integration> integrations = new ArrayList<Integration>(0);
        switch (messengerDestination) {
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
}
