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
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */

@SuppressWarnings("WeakerAccess")
public class FrequencyOfSending {
    private static final Lifetime<SentMessage> sentMessages = new Lifetime<SentMessage>(
            TimeUnit.MINUTES.toSeconds(10), TimeUnit.MINUTES.toSeconds(1));

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

    public static boolean canSendMessage(final MessageTypes type, final String projectName, final String description) {
        return !sentMessages.contains(new SentMessage(type, projectName, description));
    }

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

    public static boolean messageSent(final MessageTypes type,
                                      final String projectName,
                                      final String description) {
        if (sentMessages.contains(new SentMessage(type, projectName, description)))
            return false;

        sentMessages.put(new SentMessage(type, projectName, description));
        return true;
    }
}
