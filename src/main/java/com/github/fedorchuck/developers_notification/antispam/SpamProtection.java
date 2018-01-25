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
import com.github.fedorchuck.developers_notification.helpers.Constants;
import com.github.fedorchuck.developers_notification.helpers.InternalUtil;
import com.github.fedorchuck.developers_notification.integrations.Integration;
import com.github.fedorchuck.developers_notification.model.Task;
import org.apache.log4j.spi.LoggingEvent;

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
    public static void sendMonitoringResultsIntoMessenger(final boolean protectionFromSpam,
                                                          final MessageTypes types,
                                                          final String description) {
        List<Task> tasks = new ArrayList<Task>(0);
        String projectName = DevelopersNotification.getConfiguration().getProjectName();
        for (Integration integration : InternalUtil.getIntegrations()) {
            tasks.add(InternalUtil.generateTask(projectName, description, null, integration));
        }

        for (Task task : tasks) {
            sendIntoMessenger(protectionFromSpam, types, task);
        }
    }

    /**
     * It provide sending messages into messengers.
     * <p><b>Note:</b> all needed data will be getting from JSON configuration</p>
     *
     * @param protectionFromSpam is needed protection from spam
     * @param event what happened
     * @since 0.3.0
     **/
    public void sendLogEventIntoMessenger(final boolean protectionFromSpam,
                                          final LoggingEvent event) {
        List<Task> tasks = new ArrayList<Task>(0);
        String projectName = DevelopersNotification.getConfiguration().getProjectName();
        for (Integration integration : InternalUtil.getIntegrations()) {
            tasks.add(InternalUtil.generateTaskFromLoggingEvent(projectName, event, integration));
        }

        for (Task task : tasks) {
            sendIntoMessenger(protectionFromSpam, MessageTypes.LOGGING_EVENT, task);
        }
    }

    /**
     * It provide sending messages to chosen destination.
     *
     * @param protectionFromSpam is needed protection from spam
     * @param types of message
     * @param task to compete
     * @since 0.3.0
     **/
    public static void sendIntoMessenger(final boolean protectionFromSpam,
                                         final MessageTypes types,
                                         final Task task) {
        Thread t = new Thread(
            Constants.THREAD_GROUP,
            new Runnable() {
                public void run() {
                    Thread.currentThread().setName("Send developers notification to " + task.getIntegration().name());
                    SentMessage sentMessage =
                        new SentMessage(types, task.getIntegration().name(), task.getProjectName(), task.getDescription());

                    if (protectionFromSpam) {
                        if (!FrequencyOfSending.canSendMessage(sentMessage)) {
                            DevelopersNotificationLogger.infoTryToSentDuplicateMessage(sentMessage);
                            return; //duplicate ignore
                        }

                        FrequencyOfSending.messageSent(sentMessage);
                        DevelopersNotificationLogger.infoSentMessage(sentMessage);
                        complete(task);
                    } else {
                        complete(task);
                    }
                }
            },
            Constants.THREAD_NAME_SENDING
        );
        t.setDaemon(true);
        t.start();
    }

    /**
     * This method complete the {@link Task} by sending messages to destination.
     *
     * @param task to compete
     * @since 0.3.0
     **/
    private static void complete(Task task) {
        task.getIntegration().sendMessage(task);
        DevelopersNotificationLogger.infoTaskCompleted(task.getIntegration().getClass().getSimpleName());
    }
}
