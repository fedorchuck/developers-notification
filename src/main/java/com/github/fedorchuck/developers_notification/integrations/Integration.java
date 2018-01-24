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

package com.github.fedorchuck.developers_notification.integrations;

import com.github.fedorchuck.developers_notification.DevelopersNotificationMessenger;
import com.github.fedorchuck.developers_notification.http.HttpResponse;
import com.github.fedorchuck.developers_notification.model.Task;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Decelerate which methods should contain each integration,
 * which allows to send messages to messengers.
 * <p>
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 *
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.0
 */
public interface Integration {

    /**
     * Contains name of integration
     *
     * @return name of integration
     * @since 0.3.0
     **/
    DevelopersNotificationMessenger name();

    /**
     * Provides sending messages to specified messenger
     *
     * @param message to send
     * @since 0.1.0
     **/
    void sendMessage(Task message);

    /**
     * Generate {@link Task} to send by specified params
     *
     * @param projectName where was method called. Can be <code>null</code>
     * @param description about situation. Can be <code>null</code>
     * @param throwable   which happened. Can be <code>null</code>
     * @return generated message as {@link Task } JSON
     * @since 0.1.0
     **/
    Task generateMessage(String projectName, String description, Throwable throwable);

    /**
     * Generate {@link Task} to send by specified params
     *
     * @param projectName where was method called. Can be <code>null</code>
     * @param event from logger
     * @return generated message as {@link Task } JSON
     * @since 0.3.0
     **/
    Task generateMessageFromLoggingEvent(String projectName, LoggingEvent event);

    /**
     * Analyse response after sent message
     *
     * @param response response from http client
     * @since 0.2.1
     **/
    void analyseResponse(HttpResponse response);
}
