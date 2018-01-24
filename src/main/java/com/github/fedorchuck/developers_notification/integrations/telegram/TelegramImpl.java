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
import com.github.fedorchuck.developers_notification.DevelopersNotificationUtil;
import com.github.fedorchuck.developers_notification.configuration.Messenger;
import com.github.fedorchuck.developers_notification.http.HttpClient;
import com.github.fedorchuck.developers_notification.http.HttpResponse;
import com.github.fedorchuck.developers_notification.integrations.Integration;
import com.github.fedorchuck.developers_notification.model.Task;
import com.github.fedorchuck.dnjson.Json;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.log4j.spi.LoggingEvent;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Provides sending messages via Telegram messenger
 * <p>
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 *
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.0
 */
public class TelegramImpl implements Integration {
    private static final String SERVER_ENDPOINT = "https://api.telegram.org/bot";
    private static final String SEND_MESSAGE = "/sendMessage";
    private static final String SEND_DOCUMENT = "/sendDocument";
    private HttpClient httpClient = new HttpClient();
    private String token;
    private String channel;
    private Boolean showWholeLogDetails = DevelopersNotification.config.getShowWholeLogDetails();
    private Boolean configurationExist =
            DevelopersNotificationUtil.checkTheNecessaryConfigurationExists(DevelopersNotificationMessenger.TELEGRAM);

    public TelegramImpl() {
        for (Messenger m : DevelopersNotification.config.getMessenger()) {
            if (m.getName() == DevelopersNotificationMessenger.TELEGRAM) {
                token = m.getToken();
                channel = m.getChannel();
                break;
            }
        }

        if (!configurationExist)
            DevelopersNotificationLogger.warnSendMessageBadConfig("TELEGRAM");
    }

    @Override
    public DevelopersNotificationMessenger name() {
        return DevelopersNotificationMessenger.TELEGRAM;
    }

    /**
     * Provides sending messages to Telegram messenger
     *
     * @param message to send
     * @since 0.1.0
     **/
    @Override
    public void sendMessage(Task message) {
        if (!configurationExist) {
            DevelopersNotificationLogger.errorSendMessageBadConfig("TELEGRAM");
            return;
        }

        String url = SERVER_ENDPOINT + token + SEND_MESSAGE;

        if (!DevelopersNotificationUtil.isBlank(message.getJsonGeneratedMessages())) {
            if (showWholeLogDetails)
                DevelopersNotificationLogger.infoMessageSend("Telegram", url, message.getJsonGeneratedMessages());
            else
                DevelopersNotificationLogger.infoMessageSendHideDetails("Telegram");

            try {
                HttpResponse sendMessageResult = httpClient.post(url, message.getJsonGeneratedMessages());

                if (showWholeLogDetails)
                    DevelopersNotificationLogger.infoHttpClientResponse(sendMessageResult);
                else
                    DevelopersNotificationLogger.infoHttpClientResponseHideDetails(sendMessageResult);

                analyseResponse(sendMessageResult);
            } catch (IOException e) {
                DevelopersNotificationLogger.errorSendMessage("Telegram", e);
            }
        }

        if (message.getMultipartEntityBuilder() == null)
            return;

        url = SERVER_ENDPOINT + token + SEND_DOCUMENT;
        if (showWholeLogDetails)
            DevelopersNotificationLogger.infoMessageSend("Telegram", url, message.getMultipartEntityBuilder());
        else
            DevelopersNotificationLogger.infoMessageSendHideDetails("Telegram");

        try {
            HttpResponse sendDocumentResult = httpClient.sendMultipartFromData(url, message.getMultipartEntityBuilder());

            if (showWholeLogDetails)
                DevelopersNotificationLogger.infoHttpClientResponse(sendDocumentResult);
            else
                DevelopersNotificationLogger.infoHttpClientResponseHideDetails(sendDocumentResult);

            analyseResponse(sendDocumentResult);

        } catch (IOException e) {
            DevelopersNotificationLogger.errorSendMessage("Telegram", e);
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
        if (DevelopersNotificationUtil.isNullOrEmpty(channel))
            return null;//it will be handled on TelegramImpl#sendMessage

        Task task = new Task(this, projectName, description, throwable);
        Message message = new Message();

        message.setChat_id(channel);
        message.setParse_mode("Markdown");

        StringBuffer generatedMessage = new StringBuffer();
        if (!DevelopersNotificationUtil.isNullOrEmpty(projectName)) {
            generatedMessage.append("*Project*: ");
            generatedMessage.append(replaceLowLine(projectName));
            generatedMessage.append(" \n");
        }
        if (!DevelopersNotificationUtil.isNullOrEmpty(projectName)) {
            generatedMessage.append("*Message*: ");
            generatedMessage.append(replaceLowLine(description));
            generatedMessage.append(" \n");
        }
        if (throwable != null) {
            generatedMessage.append("*Throwable*:` ");
            generatedMessage.append(replaceLowLine(String.valueOf(throwable)));
            generatedMessage.append("`\n");

            byte[] stackTrace = DevelopersNotificationUtil.getThrowableStackTraceBytes(throwable);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addBinaryBody("document", stackTrace, ContentType.MULTIPART_FORM_DATA, "StackTrace-" + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime()));
            builder.addTextBody("chat_id", channel);

            task.setMultipartEntityBuilder(builder);
        }
        message.setText(generatedMessage.toString());
        task.setJsonGeneratedMessages(Json.encode(message));

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
        if (DevelopersNotificationUtil.isNullOrEmpty(channel))
            return null;//it will be handled on TelegramImpl#sendMessage

        String filename = event.getLevel() + "_" + projectName + "_" +
                new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss_z(Z)").format(new Date(event.getTimeStamp()));

        Task task = new Task(this, projectName, event.getMessage().toString(), null);

        byte[] stackTrace = handleInformation(event, projectName);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addBinaryBody("document", stackTrace, ContentType.MULTIPART_FORM_DATA, filename);
            builder.addTextBody("chat_id", channel);

        task.setMultipartEntityBuilder(builder);

        return task;
    }

    /**
     * Analyse response after sent message
     * <p> <b>See Also:</b>: <a href="https://core.telegram.org/method/messages.sendMessage#return-errors">https://core.telegram.org/method/messages.sendMessage#return-errors</a>  </p>
     *
     * @param response response from http client
     * @since 0.2.1
     **/
    @Override
    public void analyseResponse(HttpResponse response) {
        if (response.getException() != null)
            DevelopersNotificationLogger.errorSendMessageBadConfig("Telegram", response.getException());
        if (!response.getContentType().equals("application/json"))
            DevelopersNotificationLogger.error("Telegram updated their rest api.");
        if (response.getStatusCode() != 200 || response.getResponseContent() == null) {
            if (showWholeLogDetails)
                DevelopersNotificationLogger.errorSendMessageBadConfig("Telegram",
                        "response code: " + response.getStatusCode() +
                                " response content: " + response.getResponseContent());
            else
                DevelopersNotificationLogger.errorSendMessageBadConfig("Telegram",
                        "response code: " + response.getStatusCode() +
                                " response content: hidden by show_whole_log_details.");
        }
    }

    /**
     * Replace <code>_</code> to <code>-</code>
     *
     * @param string that possible contains <code>_</code>
     * @return handled string
     * @since 1.0.2
     **/
    private String replaceLowLine(String string) {
        return string.replace('_', '-');
    }


    /**
     * Convert input {@link LoggingEvent} to byte array
     *
     * @param event that will be converted
     * @return handled byte array
     * @since 0.3.0
     **/
    private byte[] handleInformation(LoggingEvent event, String projectName) {
        StringBuffer result = new StringBuffer();
            result.append("level: \t\t\t");
            result.append(event.getLevel());
            result.append("\n");
            result.append("projectName: \t\t");
            result.append(projectName);
            result.append("\n");
            result.append("loggerName: \t\t");
            result.append(event.getLoggerName());
            result.append("\n");
            result.append("message: \t\t");
            result.append(event.getMessage());
            result.append("\n");
            result.append("renderedMessage: \t");
            result.append(event.getRenderedMessage());
            result.append("\n");
            result.append("threadName: \t\t");
            result.append(event.getThreadName());
            result.append("\n");
            result.append("millis: \t\t");
            result.append(event.getTimeStamp());
            result.append("\n");
            result.append("date: \t\t\t");
            result.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z(Z)").format(new Date(event.getTimeStamp())));
            result.append("\n");
            if (event.getThrowableInformation()!=null) {
                result.append("thrown: \n");
                result.append("throwable: \t\t");
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
                result.append("NDC: \t");
                result.append(event.getNDC());
                result.append("\n");
            }

        return String.valueOf(result).getBytes();
    }
}
