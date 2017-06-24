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

package com.github.fedorchuck.developers_notification.configuration;

import com.github.fedorchuck.developers_notification.DevelopersNotificationLogger;
import com.github.fedorchuck.developers_notification.DevelopersNotificationMessenger;
import com.github.fedorchuck.developers_notification.DevelopersNotificationUtil;
import lombok.ToString;

/**
 * Part of configuration for this library.
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.2.0
 */
@SuppressWarnings("unused")
@ToString
public class Messenger {
    private String name;
    private String token;
    private String channel;

    /**
     * Return name of integration
     *
     * @return name for integration
     * @throws IllegalArgumentException if unable to map string to {@link DevelopersNotificationMessenger}
     * @since 0.2.0
     **/
    public DevelopersNotificationMessenger getName() {
        if (DevelopersNotificationMessenger.SLACK.name().equals(name.toUpperCase()))
            return DevelopersNotificationMessenger.SLACK;
        if (DevelopersNotificationMessenger.TELEGRAM.name().equals(name.toUpperCase()))
            return DevelopersNotificationMessenger.TELEGRAM;
        if (DevelopersNotificationMessenger.ALL_AVAILABLE.name().equals(name.toUpperCase()))
            return DevelopersNotificationMessenger.ALL_AVAILABLE;

        throw new IllegalArgumentException("Bad argument. Current name value: " + name + " . Name should be: SLACK or TELEGRAM or ALL_AVAILABLE. ");
    }

    /**
     * Return token of integration
     *
     * @return token for integration
     * @throws IllegalArgumentException if token is null or empty
     * @since 0.2.0
     **/
    public String getToken() {
        if (DevelopersNotificationUtil.isNullOrEmpty(token)) {
            DevelopersNotificationLogger.errorWrongConfig(token, "token");
            throw new IllegalArgumentException("Bad argument. TOKEN has invalid value: " + token);
        }
        return token;
    }

    /**
     * Return channel of integration
     *
     * @return channel for integration
     * @throws IllegalArgumentException if channel is null or empty
     * @since 0.2.0
     **/
    public String getChannel() {
        if (DevelopersNotificationUtil.isNullOrEmpty(channel)) {
            DevelopersNotificationLogger.errorWrongConfig(channel, "channel");
            throw new IllegalArgumentException("Bad argument. CHANNEL has invalid value: " + channel);
        }
        return channel;
    }
}
