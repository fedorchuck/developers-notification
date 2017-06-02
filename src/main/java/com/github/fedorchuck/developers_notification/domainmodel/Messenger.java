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

package com.github.fedorchuck.developers_notification.domainmodel;

import com.github.fedorchuck.developers_notification.DevelopersNotificationLogger;
import com.github.fedorchuck.developers_notification.DevelopersNotificationMessenger;
import com.github.fedorchuck.developers_notification.DevelopersNotificationUtil;
import lombok.*;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
@EqualsAndHashCode @ToString
@AllArgsConstructor @NoArgsConstructor
public class Messenger {
    private String name;
    private String token;
    private String channel;

    public DevelopersNotificationMessenger getName() {
        if (DevelopersNotificationMessenger.SLACK.name().equals(name.toUpperCase()))
            return DevelopersNotificationMessenger.SLACK;
        if (DevelopersNotificationMessenger.TELEGRAM.name().equals(name.toUpperCase()))
            return DevelopersNotificationMessenger.TELEGRAM;
        if (DevelopersNotificationMessenger.ALL_AVAILABLE.name().equals(name.toUpperCase()))
            return DevelopersNotificationMessenger.ALL_AVAILABLE;

        throw new IllegalArgumentException("Bad argument. Current name value: " + name + " . Name should be: SLACK or TELEGRAM or ALL_AVAILABLE. ");
    }

    public String getToken() {
        if (DevelopersNotificationUtil.isNullOrEmpty(token)) {
            DevelopersNotificationLogger.errorWrongConfig(token,"token");
            throw new IllegalArgumentException("Bad argument. TOKEN has invalid value: " + token);
        }
        return token;
    }

    public String getChannel() {
        if (DevelopersNotificationUtil.isNullOrEmpty(channel)) {
            DevelopersNotificationLogger.errorWrongConfig(channel, "channel");
            throw new IllegalArgumentException("Bad argument. CHANNEL has invalid value: " + channel);
        }
        return channel;
    }
}
