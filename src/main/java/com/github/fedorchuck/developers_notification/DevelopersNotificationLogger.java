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

package com.github.fedorchuck.developers_notification;

import com.github.fedorchuck.developers_notification.antispam.SentMessage;
import com.github.fedorchuck.developers_notification.http.HttpResponse;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Library logger
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.0
 */
@SuppressWarnings("WeakerAccess")
public class DevelopersNotificationLogger {
    private static Logger logger(int code) {
        return LoggerFactory.getLogger("DEVELOPERS_NOTIFICATION_"+code);
    }

    //region FATAL
    public static void fatalConfigNotFound() {
        logger(1).error("Config not found. Please, check your configuration. ");
    }

    //region ERROR
    public static void error(String val, Exception ex) {
        logger(1001).error(val, ex);
    }

    public static void error(String val) {
        logger(1002).error(val);
    }

    public static void errorWrongConfig(String val, String val2) {
        logger(1010).error("DEVELOPERS_NOTIFICATION has invalid config value: {} for {}", val, val2);
    }

    public static void errorWrongConfig(String val, String configField, String description) {
        logger(1011).error("DEVELOPERS_NOTIFICATION has invalid config value: {} for {} . {}",
                val, configField, description);
    }

    public static void errorTaskFailed(String val, Exception ex) {
        logger(1020).error("Task {} has been failed with ", val, ex);
    }

    public static void errorSendMessage(String integration, IOException ex) {
        logger(1030).error("Message was not send to {}. ", integration, ex);
    }

    public static void errorSendMessageBadConfig(String integration, IOException ex) {
        logger(1031).error("Message was not send to {}. Please, check configuration. ", integration, ex);
    }

    public static void errorSendMessageBadConfig(String integration, String desc) {
        logger(1032).error(
                "Message was not send to {}. Please, check configuration. Description: {}", integration, desc
        );
    }

    public static void errorSendMessageBadConfig(String integration) {
        logger(1033).error(
                "Message was not send to {}. Integration has not got configuration. Please, check configuration.",
                integration
        );
    }

    public static void errorScheduler(String val) {
        logger(1040).error(val);
    }
    //endregion

    //region WARN
    public static void warnSendMessageBadConfig(String integration) {
        logger(3001).warn("Integration {} has not got configuration. Please, check configuration.", integration);
    }
    //endregion

    //region INFO
    public static void info(String val) {
        logger(4001).info(val);
    }

    public static void infoTaskCompleted(String val) {
        logger(4002).info("Task {} has been completed.", val);
    }

    public static void infoEnvironmentVariable(String key, String val) {
        logger(4003).info("Environment property {} is {}. ", key, val);
    }

    public static void infoMessageSendHideDetails(String integration) {
        logger(4011).info("Sending message to {}. show_whole_log_details is false.", integration);
    }

    public static void infoMessageSend(String integration, String val, String message) {
        logger(4012).info("Sending message to {} by url: {} with message: {}", integration, val, message);
    }

    public static void infoMessageSend(String integration, String val, MultipartEntityBuilder message) {
        logger(4013).info("Sending message to {} by url: {} with message: {}", integration, val, message.toString());
    }

    public static void infoSentMessage(SentMessage sentMessage) {
        logger(4021).info("[SPAM PROTECTION] Sent message: {}.", sentMessage.getAboutMessage());
    }

    public static void infoTryToSentDuplicateMessage(SentMessage sentMessage) {
        logger(4022).info("[SPAM PROTECTION] Try to sent duplicated message: {}.", sentMessage.getAboutMessage());
    }

    public static void infoHttpClientResponseHideDetails(HttpResponse val) {
        logger(4051).info("Response: {}", val.printResponseHideDetails());
    }

    public static void infoHttpClientResponse(HttpResponse val) {
        logger(4052).info("Response: {}", val.printResponse());
    }

    public static void infoScheduler(String val) {
        logger(4071).info(val);
    }

    public static void infoLoggerLevel(String val1, Level val2) {
        logger(4100).info("Your input logging level: {}. Will be using logger level: {}", val1, val2);
    }
    //endregion
}
