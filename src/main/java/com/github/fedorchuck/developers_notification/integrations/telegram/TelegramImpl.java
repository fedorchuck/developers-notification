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

import com.github.fedorchuck.developers_notification.DevelopersNotification;
import com.github.fedorchuck.developers_notification.DevelopersNotificationLogger;
import com.github.fedorchuck.developers_notification.DevelopersNotificationMessenger;
import com.github.fedorchuck.developers_notification.configuration.Messenger;
import com.github.fedorchuck.developers_notification.integrations.Integration;
import com.github.fedorchuck.developers_notification.http.HttpClient;
import com.github.fedorchuck.developers_notification.DevelopersNotificationUtil;
import com.github.fedorchuck.developers_notification.http.HttpResponse;
import com.github.fedorchuck.developers_notification.json.Json;

import java.io.IOException;

/**
 * Provides sending messages via Telegram messenger
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.0
 */
public class TelegramImpl implements Integration {
    private HttpClient httpClient = new HttpClient();
    private String token;
    private String channel;
    private Boolean isHide = DevelopersNotification.config.getShowWholeLogDetails();

    private static final String SERVER_ENDPOINT = "https://api.telegram.org/bot";
    private static final String SEND_MESSAGE = "/sendMessage";

    public TelegramImpl() {
        for (Messenger m : DevelopersNotification.config.getMessenger()) {
            if (m.getName() == DevelopersNotificationMessenger.TELEGRAM) {
                token = m.getToken();
                channel = m.getChannel();
                break;
            }
        }
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

        if (isHide) {
            DevelopersNotificationLogger.infoTelegramSend(url, message);
        } else {
            DevelopersNotificationLogger.infoMessageSend("Telegram");
        }
        try {
            HttpResponse res = httpClient.post(url, message);
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
     * @return generated message as JSON
     * @since 0.1.0
     **/
    @Override
    public String generateMessage(String projectName, String description, Throwable throwable) {
        Message message = new Message();
        message.setChat_id(channel);
        message.setParse_mode("Markdown");

        String generatedMessage = "";
        if (!DevelopersNotificationUtil.isNullOrEmpty(projectName)) {
            generatedMessage += "*Project*: " + replaceLowLine(projectName) + " \n";
        }
        if (!DevelopersNotificationUtil.isNullOrEmpty(projectName)) {
            generatedMessage += "*Message*: " + replaceLowLine(description) + " \n";
        }
        if (throwable!=null) {
            generatedMessage += "*Throwable*:` " + replaceLowLine(String.valueOf(throwable)) +
                    " `\n*Stack trace*:``` " +
                DevelopersNotificationUtil.arrayToString(throwable.getStackTrace()) + " ```";
        }
        message.setText(generatedMessage);

        return Json.encode(message);
    }

    /**
     * Replace <code>_</code> to <code>-</code>
     * @param string that possible contains <code>_</code>
     * @return handled string
     * @since 1.0.2
     **/
    private String replaceLowLine(String string) {
        return string.replace('_','-');
    }
}
