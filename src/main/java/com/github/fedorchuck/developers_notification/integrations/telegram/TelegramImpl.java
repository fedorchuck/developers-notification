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
import com.github.fedorchuck.developers_notification.integrations.developers_notification.DNMessage;
import com.github.fedorchuck.developers_notification.json.Json;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    /**
     * Provides sending messages to Telegram messenger
     *
     * @param message to send
     * @since 0.1.0
     **/
    @Override
    public void sendMessage(DNMessage message) {
        if (!configurationExist) {
            DevelopersNotificationLogger.errorSendMessageBadConfig("TELEGRAM");
            return;
        }

        String url = SERVER_ENDPOINT + token + SEND_MESSAGE;

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
    public DNMessage generateMessage(String projectName, String description, Throwable throwable) {
        if (DevelopersNotificationUtil.isNullOrEmpty(channel))
            return null;//it will be handled on TelegramImpl#sendMessage

        DNMessage dnMessage = new DNMessage();
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

            dnMessage.setMultipartEntityBuilder(builder);
        }
        message.setText(generatedMessage.toString());
        dnMessage.setJsonGeneratedMessages(Json.encode(message));

        return dnMessage;
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
}
