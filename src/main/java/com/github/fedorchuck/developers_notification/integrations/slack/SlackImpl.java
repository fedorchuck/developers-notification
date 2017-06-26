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

package com.github.fedorchuck.developers_notification.integrations.slack;

import com.github.fedorchuck.developers_notification.DevelopersNotification;
import com.github.fedorchuck.developers_notification.DevelopersNotificationLogger;
import com.github.fedorchuck.developers_notification.DevelopersNotificationMessenger;
import com.github.fedorchuck.developers_notification.DevelopersNotificationUtil;
import com.github.fedorchuck.developers_notification.configuration.Messenger;
import com.github.fedorchuck.developers_notification.http.HttpClient;
import com.github.fedorchuck.developers_notification.http.HttpResponse;
import com.github.fedorchuck.developers_notification.integrations.Integration;
import com.github.fedorchuck.developers_notification.json.Json;

import java.io.IOException;
import java.util.Collections;

/**
 *  Provides sending messages via Slack messenger
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.0
 */
public class SlackImpl implements Integration {
    private HttpClient httpClient = new HttpClient();
    private String token;
    private String channel;
    private Boolean showWholeLogDetails = DevelopersNotification.config.getShowWholeLogDetails();

    private static final String SERVER_ENDPOINT = "https://hooks.slack.com/services/";

    public SlackImpl() {
        for (Messenger m : DevelopersNotification.config.getMessenger()) {
            if (m.getName() == DevelopersNotificationMessenger.SLACK) {
                token = m.getToken();
                channel = m.getChannel();
                break;
            }
        }
    }

    /**
     * Provides sending messages to Slack messenger
     * @param message to send
     *
     * @since 0.1.0
     **/
    @Override
    public void sendMessage(String message) {
        String url = SERVER_ENDPOINT + token;
        if (showWholeLogDetails)
            DevelopersNotificationLogger.infoMessageSend("Slack", url, message);
        else
            DevelopersNotificationLogger.infoMessageSendHideDetails("Slack");

        try {
            HttpResponse res = httpClient.post(url, message);

            if (showWholeLogDetails)
                DevelopersNotificationLogger.infoHttpClientResponse(res);
            else
                DevelopersNotificationLogger.infoHttpClientResponseHideDetails(res);

            analyseResponse(res);
        } catch (IOException e) {
            DevelopersNotificationLogger.errorSendMessage("Slack", e);
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
        Payload payload = new Payload();
        Attachment attachment = new Attachment();
            attachment.setFallback("The message isn't supported.");
            attachment.setColor("#FF0049");
            attachment.setMrkdwn_in(new String[]{"text","fields"});
        if (!DevelopersNotificationUtil.isNullOrEmpty(projectName)) {
            attachment.setAuthor_name(projectName);
        }
        if (throwable!=null) {
            attachment.setTitle(String.valueOf(throwable));
            attachment.setText(DevelopersNotificationUtil.arrayToString(throwable.getStackTrace()));
        }
        payload.setChannel(channel);
        if (!DevelopersNotificationUtil.isNullOrEmpty(description)) {
            payload.setText(description);
        }
        payload.setIcon_url("https://raw.githubusercontent.com/fedorchuck/developers-notification/task/%2317_add_codecov/docs/website/resources/logo/48x48.png");
        payload.setUsername("developers notification bot");
        payload.setAttachments(Collections.singletonList(attachment));

        return Json.encode(payload);
    }

    /**
     * Analyse response after sent message
     * @param response response from http client
     * @since 0.2.1
     **/
    @Override
    public void analyseResponse(HttpResponse response) {
        if (response.getException()!=null)
            DevelopersNotificationLogger.errorSendMessageBadConfig("Slack", response.getException());
        if (response.getResponseContent()!= null && !response.getResponseContent().toLowerCase().equals("ok"))
            if (showWholeLogDetails)
                DevelopersNotificationLogger.errorSendMessageBadConfig("Slack",
                        "response code: " + response.getStatusCode() +
                                " response content: " + response.getResponseContent());
            else
                DevelopersNotificationLogger.errorSendMessageBadConfig("Slack",
                    "response code: " + response.getStatusCode() +
                            " response content: hidden by show_whole_log_details.");
    }
}