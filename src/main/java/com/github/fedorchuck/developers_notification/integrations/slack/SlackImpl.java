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
import com.github.fedorchuck.developers_notification.configuration.Config;
import com.github.fedorchuck.developers_notification.configuration.Messenger;
import com.github.fedorchuck.developers_notification.http.HttpClient;
import com.github.fedorchuck.developers_notification.http.HttpResponse;
import com.github.fedorchuck.developers_notification.integrations.Integration;
import com.github.fedorchuck.developers_notification.model.Task;
import com.github.fedorchuck.dnjson.Json;
import org.apache.log4j.spi.LoggingEvent;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * Provides sending messages via Slack messenger
 * <p>
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 *
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.0
 */
public class SlackImpl implements Integration {
    private static final String SERVER_ENDPOINT = "https://hooks.slack.com/services/";
    private HttpClient httpClient = new HttpClient();
    private String token;
    private String channel;
    private boolean showWholeLogDetails;
    private Boolean configurationExist =
            DevelopersNotificationUtil.checkTheNecessaryConfigurationExists(DevelopersNotificationMessenger.SLACK);

    public SlackImpl() {
        Config config = DevelopersNotification.getConfiguration();
        for (Messenger m : config.getMessenger()) {
            if (m.getName() == DevelopersNotificationMessenger.SLACK) {
                token = m.getToken();
                channel = m.getChannel();
                break;
            }
        }

        showWholeLogDetails = config.getShowWholeLogDetails();

        if (!configurationExist)
            DevelopersNotificationLogger.warnSendMessageBadConfig("SLACK");
    }

    @Override
    public DevelopersNotificationMessenger name() {
        return DevelopersNotificationMessenger.SLACK;
    }

    /**
     * Provides sending messages to Slack messenger
     *
     * @param message to send
     * @since 0.1.0
     **/
    @Override
    public void sendMessage(Task message) {
        if (!configurationExist) {
            DevelopersNotificationLogger.errorSendMessageBadConfig("SLACK");
            return;
        }

        String url = SERVER_ENDPOINT + token;
        if (showWholeLogDetails)
            DevelopersNotificationLogger.infoMessageSend("Slack", url, message.getJsonGeneratedMessages());
        else
            DevelopersNotificationLogger.infoMessageSendHideDetails("Slack");

        try {
            HttpResponse res = httpClient.post(url, message.getJsonGeneratedMessages());

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
     *
     * @param projectName where was method called. Can be <code>null</code>
     * @param description about situation. Can be <code>null</code>
     * @param throwable   which happened. Can be <code>null</code>
     * @return generated message as JSON
     * @since 0.1.0
     **/
    @Override
    public Task generateMessage(String projectName, String description, Throwable throwable) {
        Task task = new Task(this, projectName, description, throwable);

        Payload payload = new Payload();
        Attachment attachment = new Attachment();
        attachment.setFallback("The message isn't supported.");
        attachment.setColor("#FF0049");
        attachment.setMrkdwn_in(new String[]{"text", "fields"});
        if (!DevelopersNotificationUtil.isNullOrEmpty(projectName)) {
            attachment.setAuthor_name(projectName);
        }
        if (throwable != null) {
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

        task.setJsonGeneratedMessages(Json.encode(payload));
        return task;
    }

    /**
     * Generate {@link Task} to send by specified params
     *
     * @param projectName where was method called. Can be <code>null</code>
     * @param event from logger
     * @return generated message as {@link Task } JSON
     * @since 0.3.0
     **/
    @Override
    public Task generateMessageFromLoggingEvent(String projectName, LoggingEvent event) {
        Task task = new Task(this, projectName, event.getMessage().toString(), null);

        Payload payload = new Payload();
        Attachment attachment = new Attachment();
            attachment.setFallback("The message isn't supported.");
            attachment.setColor("#FF0049");
            attachment.setMrkdwn_in(new String[]{"text", "fields"});
        if (!DevelopersNotificationUtil.isNullOrEmpty(projectName)) {
            attachment.setAuthor_name(projectName);
        }
        if (event.getThrowableInformation() != null) {
            if (event.getThrowableInformation().getThrowable() != null)
                attachment.setTitle(String.valueOf(event.getThrowableInformation().getThrowable()));

            attachment.setText(handleInformation(event));
        }
        payload.setChannel(channel);
        if (event.getMessage()!=null) {
            payload.setText(event.getMessage().toString());
        }
        payload.setIcon_url("https://raw.githubusercontent.com/fedorchuck/developers-notification/task/%2317_add_codecov/docs/website/resources/logo/48x48.png");
        payload.setUsername("developers notification bot");
        payload.setAttachments(Collections.singletonList(attachment));

        task.setJsonGeneratedMessages(Json.encode(payload));
        return task;
    }

    /**
     * Analyse response after sent message
     *
     * @param response response from http client
     * @since 0.2.1
     **/
    @Override
    public void analyseResponse(HttpResponse response) {
        if (response.getException() != null)
            DevelopersNotificationLogger.errorSendMessageBadConfig("Slack", response.getException());
        if (!response.getContentType().equals("text/html"))
            DevelopersNotificationLogger.error("Slack updated their rest api.");
        if (response.getResponseContent() != null && !response.getResponseContent().toLowerCase().equals("ok"))
            if (showWholeLogDetails)
                DevelopersNotificationLogger.errorSendMessageBadConfig("Slack",
                        "response code: " + response.getStatusCode() +
                                " response content: " + response.getResponseContent());
            else
                DevelopersNotificationLogger.errorSendMessageBadConfig("Slack",
                        "response code: " + response.getStatusCode() +
                                " response content: hidden by show_whole_log_details.");
    }

    /**
     * Convert input {@link LoggingEvent} to {@link String}
     *
     * @param event that will be converted
     * @return handled byte array
     * @since 0.3.0
     **/
    private String handleInformation(LoggingEvent event) {
        StringBuffer result = new StringBuffer();
        result.append("Logging level: \t ");
        result.append(event.getLevel());
        result.append("\n");
        result.append("loggerName: \t ");
        result.append(event.getLoggerName());
        result.append("\n");
        result.append("renderedMessage: \t");
        result.append(event.getRenderedMessage());
        result.append("\n");
        result.append("threadName: \t ");
        result.append(event.getThreadName());
        result.append("\n");
        result.append("millis: \t ");
        result.append(event.getTimeStamp());
        result.append("\n");
        result.append("date: \t ");
        result.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z(Z)").format(new Date(event.getTimeStamp())));
        result.append("\n");
        if (event.getThrowableInformation()!=null) {
            result.append("throwable: \t ");
            result.append(event.getThrowableInformation().getThrowable());
            result.append("\n");
            result.append("throwableStrRep: \t");
            String throwableInformation = Arrays.toString(
                    event.getThrowableInformation().getThrowableStrRep())
                    .replace(" \tat","\n \t \t \t at");
            result.append(throwableInformation);
            result.append("\n");
        }
        if (event.getNDC()!=null) {
            result.append("NDC: \t ");
            result.append(event.getNDC());
            result.append("\n");
        }

        return String.valueOf(result);
    }
}