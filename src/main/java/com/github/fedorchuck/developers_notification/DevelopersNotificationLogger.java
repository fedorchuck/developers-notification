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

    //region ERROR
    public static void errorTaskFailed(String val, Exception ex) {
        logger(0).error("Task {} has been failed with ", val, ex);
    }

    public static void errorWrongConfig() {
        logger(1).error("DN_MESSENGER is null or empty.");
    }

    public static void errorWrongConfig(String val) {
        logger(2).error("DEVELOPERS_NOTIFICATION_MESSENGER has invalid value: {}", val);
    }

    public static void errorPrintingConfig() {
        logger(3).error("DN_SHOW_WHOLE_LOG_DETAILS is false.");
    }

    public static void errorWrongSlackConfig(String val) {
        logger(100).error("SlackImpl config has invalid value: {}", val);
    }

    public static void errorSendSlackMessage(IOException ex) {
        logger(101).error("SlackImpl message was not send. ", ex);
    }

    public static void errorWrongTelegramConfig(String val) {
        logger(200).error("TelegramImpl config has invalid value: {}", val);
    }

    public static void errorSendTelegramMessage(IOException ex) {
        logger(201).error("TelegramImpl message was not send. ", ex);
    }
    //endregion

    //region INFO
    public static void infoTaskCompleted(String val) {
        logger(4000).info("Task {} has been completed.", val);
    }

    public static void infoEnvironmentVariable(String key, String val) {
        logger(4001).info("Environment property {} is {}. ", key, val);
    }

    public static void infoMessageSend(String integration) {
        logger(4002).info("Sending message to {}. DN_SHOW_WHOLE_LOG_DETAILS is false.", integration);
    }

    public static void infoSlackSend(String val, String message) {
        logger(4100).info("Sending message to slack by url: {} with message: {}", val, message);
    }

    public static void infoSlackResponse(String val) {
        logger(4101).info("Response from slack: {}", val);
    }

    public static void infoTelegramSend(String val, String message) {
        logger(4200).info("Sending message to telegram by url: {} with message: {}", val, message);
    }

    public static void infoTelegramResponse(String val) {
        logger(4201).info("Response from telegram: {}", val);
    }
    //endregion
}
