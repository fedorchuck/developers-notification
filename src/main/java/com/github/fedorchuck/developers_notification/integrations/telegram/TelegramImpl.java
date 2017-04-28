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

package com.github.fedorchuck.developers_notification.integrations.telegram;

import com.github.fedorchuck.developers_notification.DevelopersNotificationLogger;
import com.github.fedorchuck.developers_notification.integrations.Integration;
import com.github.fedorchuck.developers_notification.http.HttpClient;
import com.github.fedorchuck.developers_notification.DevelopersNotificationUtil;
import com.github.fedorchuck.developers_notification.http.HttpResponse;
import com.google.common.base.Strings;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides sending messages via Telegram messenger
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.0
 */
public class TelegramImpl implements Integration {
    private HttpClient httpClient = new HttpClient();
    private String token = DevelopersNotificationUtil.getEnvironmentVariable("DN_TELEGRAM_TOKEN");
    private String channel = DevelopersNotificationUtil.getEnvironmentVariable("DN_TELEGRAM_CHANNEL");

    private static final String SERVER_ENDPOINT = "https://api.telegram.org/bot";
    private static final String SEND_MESSAGE = "/sendMessage";

    public TelegramImpl() {
        if (Strings.isNullOrEmpty(token)) {
            DevelopersNotificationLogger.errorWrongTelegramConfig(token);
            throw new IllegalArgumentException("DN_TELEGRAM_TOKEN has invalid value: " + token);
        }
        if (Strings.isNullOrEmpty(channel)) {
            DevelopersNotificationLogger.errorWrongTelegramConfig(channel);
            throw new IllegalArgumentException("DN_TELEGRAM_CHANNEL has invalid value: " + channel);
        }
    }

    /**
     * Prints environment variable value with
     * {@link DevelopersNotificationLogger#infoEnvironmentVariable(String, String)}.
     *
     * @since 0.1.0
     **/
    public static void printConfiguration() {
        DevelopersNotificationUtil.printToLogEnvironmentVariable("DN_TELEGRAM_TOKEN");
        DevelopersNotificationUtil.printToLogEnvironmentVariable("DN_TELEGRAM_CHANNEL");
    }

    /**
     * Provides sending messages to Telegram messenger
     * @param message to send
     *
     * @since 0.1.0
     **/
    @Override
    public void sendMessage(String message) {
        String url = SERVER_ENDPOINT + token + SEND_MESSAGE;
        Map<String, String> params = new HashMap<String, String>();
            params.put("chat_id", channel);
            params.put("text", message);
            params.put("parse_mode", "Markdown");

        DevelopersNotificationLogger.infoTelegramSend(url);
        try {
            HttpResponse res = httpClient.get(url, params);
            DevelopersNotificationLogger.infoTelegramResponse(res.toString());
        } catch (IOException e) {
            DevelopersNotificationLogger.errorSendTelegramMessage(e);
        }
    }

    /**
     * Generate message to send by specified params
     * @param projectName where was method called. Can be <code>null</code>
     * @param description about situation. Can be <code>null</code>
     * @param throwable which happened. Can be <code>null</code>
     *
     * @since 0.1.0
     **/
    @Override
    public String generateMessage(String projectName, String description, Throwable throwable) {
        String generatedMessage = "";
        if (!Strings.isNullOrEmpty(projectName)) {
            generatedMessage += "*Project*: " + projectName + " \n";
        }
        if (!Strings.isNullOrEmpty(projectName)) {
            generatedMessage += "*Message*: " + description + " \n";
        }
        if (throwable!=null) {
            generatedMessage += "*Throwable*:` " + String.valueOf(throwable) + " `\n*Stack trace*:``` " +
                DevelopersNotificationUtil.arrayToString(throwable.getStackTrace()) + " ```";
        }
        return generatedMessage;
    }
}
