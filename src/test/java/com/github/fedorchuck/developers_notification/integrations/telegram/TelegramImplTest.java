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

import com.github.fedorchuck.developers_notification.DevelopersNotificationUtil;
import com.github.fedorchuck.developers_notification.Utils;
import com.github.fedorchuck.developers_notification.model.Task;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
public class TelegramImplTest {

    @Before
    public void setUp() {
        String telegramToken = DevelopersNotificationUtil.getEnvironmentVariable("TRAVIS_TEST_TELEGRAM_TOKEN");
        String telegramChannel = DevelopersNotificationUtil.getEnvironmentVariable("TRAVIS_TEST_TELEGRAM_CHANNEL");

        String stringTelegramConfig = "{\"messenger\":[{\"name\":\"TELEGRAM\",\"token\":\""+telegramToken+"\",\"channel\":"+telegramChannel+"}],\"show_whole_log_details\":false,\"protection_from_spam\": \"true\",\"project_name\": \"Where this library will be invoked\",\"connect_timeout\":5000,\"user_agent\":\"Mozilla/5.0\"}";
        Utils.setConfig(stringTelegramConfig);
    }

    @Test
    public void testGenerateMessage() {
        String telegramChannel = DevelopersNotificationUtil.getEnvironmentVariable("TRAVIS_TEST_TELEGRAM_CHANNEL");
        String expected = "{\"chat_id\":\""+telegramChannel+"\",\"parse_mode\":\"Markdown\",\"text\":\"*Project*: Where this library will be invoked \\n*Message*: test generate telegram message \\n\"}";
        TelegramImpl telegram = new TelegramImpl();

        Task actual = telegram.generateMessage("Where this library will be invoked",
                "test generate telegram message", null);

        Assert.assertEquals(expected, actual.getJsonGeneratedMessages());
    }

    @Test
    public void testSendMessage() {
        TelegramImpl telegram = new TelegramImpl();

        Task messageToSend;

        messageToSend = telegram.generateMessage(
                "test " + this.getClass().getCanonicalName() + "#testSendMessage",
                "test for short message" +
                "java: " + Runtime.class.getPackage().getImplementationVersion(),
                null);

        telegram.sendMessage(messageToSend);

        messageToSend = telegram.generateMessage(
                "test " + this.getClass().getCanonicalName() + "#testSendMessage",
                "test for long message" +
                        "java: " + Runtime.class.getPackage().getImplementationVersion(),
                new Throwable(new Throwable(new Throwable("test for long message"))));

        telegram.sendMessage(messageToSend);

        //TODO: looking for errors in logs
    }

    @After
    public void tearDown() {
        Utils.resetConfig();
    }
}