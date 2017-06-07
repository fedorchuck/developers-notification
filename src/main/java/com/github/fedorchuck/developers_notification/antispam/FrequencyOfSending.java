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

import com.github.fedorchuck.developers_notification.helpers.Lifetime;

import java.util.concurrent.TimeUnit;

/**
 * This class regulate frequency of sending messages to messengers
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.2.0
 */
@SuppressWarnings("WeakerAccess")
public class FrequencyOfSending {
    private static final Lifetime<SentMessage> sentMessages = new Lifetime<SentMessage>(
            TimeUnit.MINUTES.toSeconds(10), TimeUnit.MINUTES.toSeconds(1));

    /**
     * Checking, is able to sending message with it type.
     *
     * @param type of message for sending
     * @return  <code>true</code> if this action available;
     *          <code>false</code> otherwise.
     * @throws IllegalArgumentException if method called for {@link MessageTypes#USERS_MESSAGE}
     * @since 0.2.0
     **/
    public static boolean canSendMessage(final MessageTypes type) {
        switch (type) {
            case RAM_LIMIT:
            case DISK_LIMIT:
            case DISK_CONSUMPTION_RATE:
                return canSendMessage(type, "developers_notification", "123");
            default:
                throw new IllegalArgumentException("Type " + type + " can not use this method.");
        }
    }

    /**
     * Checking, is able to sending this message.
     *
     * @param type of message for sending
     * @param projectName name of project, where this library was invoked
     * @param description what happened
     * @return  <code>true</code> if this action available;
     *          <code>false</code> otherwise.
     * @since 0.2.0
     **/
    public static boolean canSendMessage(final MessageTypes type, final String projectName, final String description) {
        return !sentMessages.contains(new SentMessage(type, projectName, description));
    }

    /**
     * Change status for message to avoid spam.
     *
     * @param type of message for sending
     * @return  <code>true</code> if this action completed successfully;
     *          <code>false</code> otherwise.
     * @throws IllegalArgumentException if method called for {@link MessageTypes#USERS_MESSAGE}
     * @since 0.2.0
     **/
    public static boolean messageSent(final MessageTypes type) {
        switch (type) {
            case RAM_LIMIT:
            case DISK_LIMIT:
            case DISK_CONSUMPTION_RATE:
                return messageSent(type, "developers_notification", "123");
            default:
                throw new IllegalArgumentException("Type " + type + " can not use this method.");
        }
    }

    /**
     * Change status for message to avoid spam.
     *
     * @param type of message for sending
     * @param projectName name of project, where this library was invoked
     * @param description what happened
     * @return  <code>true</code> if this action completed successfully;
     *          <code>false</code> otherwise.
     * @throws IllegalArgumentException if method called for {@link MessageTypes#USERS_MESSAGE}
     * @since 0.2.0
     **/
    public static boolean messageSent(final MessageTypes type,
                                      final String projectName,
                                      final String description) {
        if (sentMessages.contains(new SentMessage(type, projectName, description)))
            return false;

        sentMessages.put(new SentMessage(type, projectName, description));
        return true;
    }
}
