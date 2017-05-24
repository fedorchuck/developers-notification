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

import com.github.fedorchuck.developers_notification.DevelopersNotificationLogger;
import com.github.fedorchuck.developers_notification.DevelopersNotificationUtil;
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
    private String token = DevelopersNotificationUtil.getEnvironmentVariable("DN_SLACK_TOKEN");
    private String channel = DevelopersNotificationUtil.getEnvironmentVariable("DN_SLACK_CHANNEL");

    private static final String SERVER_ENDPOINT = "https://hooks.slack.com/services/";

    public SlackImpl() {
        if (DevelopersNotificationUtil.isNullOrEmpty(token)) {
            DevelopersNotificationLogger.errorWrongSlackConfig(token);
            throw new IllegalArgumentException("DN_SLACK_TOKEN has invalid value: " + token);
        }
        if (DevelopersNotificationUtil.isNullOrEmpty(channel)) {
            DevelopersNotificationLogger.errorWrongSlackConfig(channel);
            throw new IllegalArgumentException("DN_SLACK_CHANNEL has invalid value: " + channel);
        }
    }

    /**
     * Prints environment variable value with
     * {@link DevelopersNotificationLogger#infoEnvironmentVariable(String, String)}.
     * @since 0.1.0
     **/
    public static void printConfiguration() {
        DevelopersNotificationUtil.printToLogEnvironmentVariable("DN_SLACK_TOKEN");
        DevelopersNotificationUtil.printToLogEnvironmentVariable("DN_SLACK_CHANNEL");
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
        Boolean isHide = Boolean.valueOf(DevelopersNotificationUtil.getEnvironmentVariable("DN_SHOW_WHOLE_LOG_DETAILS"));
        if (isHide) {
            DevelopersNotificationLogger.infoSlackSend(url, message);
        } else {
            DevelopersNotificationLogger.infoMessageSend("Slack");
        }
        try {
            HttpResponse res = httpClient.post(url, message);
            DevelopersNotificationLogger.infoSlackResponse(res.toString());
        } catch (IOException e) {
            DevelopersNotificationLogger.errorSendSlackMessage(e);
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
        payload.setIcon_url("http://placehold.it/48x48");
        payload.setUsername("developers notification bot");
        payload.setAttachments(Collections.singletonList(attachment));

        return Json.encode(payload);
    }
}