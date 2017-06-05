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
import com.github.fedorchuck.developers_notification.integrations.Integration;
import com.github.fedorchuck.developers_notification.integrations.slack.SlackImpl;
import com.github.fedorchuck.developers_notification.integrations.telegram.TelegramImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
@SuppressWarnings("SameParameterValue")
public class SpamProtection {

    public static void sendIntoMessenger(boolean protectionFromSpam, String description) {
        for (Messenger messenger : DevelopersNotification.config.getMessenger()) {
            switch (messenger.getName()){
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

    public static void sendIntoMessenger(final boolean protectionFromSpam,
                                         final DevelopersNotificationMessenger messengerDestination,
                                         final String projectName,
                                         final String description,
                                         final Throwable throwable) {
        new Thread(
            new ThreadGroup("Developers notification"),
            new Runnable() {
                public void run() {
                    Thread.currentThread().setName("Send developers notification to " + messengerDestination.name());
                    if (protectionFromSpam) {
                        if (!FrequencyOfSending.canSendMessage(MessageTypes.USERS_MESSAGE, projectName, description))
                            return; //duplicate ignore

                        FrequencyOfSending.messageSent(MessageTypes.USERS_MESSAGE, projectName, description);
                        sendIntoMessenger(messengerDestination, projectName, description, throwable);
                    } else {
                        sendIntoMessenger(messengerDestination, projectName, description, throwable);
                    }
                }
            },
            "Send notification"
        ).start();
    }

    private static void sendIntoMessenger(final DevelopersNotificationMessenger messengerDestination,
                                          final String projectName,
                                          final String description,
                                          final Throwable throwable){
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
