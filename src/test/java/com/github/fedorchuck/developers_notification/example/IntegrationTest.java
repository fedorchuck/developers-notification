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

package com.github.fedorchuck.developers_notification.example;

import com.github.fedorchuck.developers_notification.DevelopersNotificationUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 **/
public class IntegrationTest {
    @Test
    public void test001() throws IOException, IllegalAccessException, InterruptedException {

        DevelopersNotificationUtil.setEnvironmentVariable("TRAVIS_TEST_SLACK_TOKEN", "T0XG2L132/B51RF69GQ/jxoAWjkBvfiS4hvALuDb6lez");
        DevelopersNotificationUtil.setEnvironmentVariable("TRAVIS_TEST_SLACK_CHANNEL", "general");
        DevelopersNotificationUtil.setEnvironmentVariable("TRAVIS_TEST_TELEGRAM_TOKEN", "317208208:AAEZU_J17y5-UnMTjFRrUwtRbBCvuBu2-9U");
        DevelopersNotificationUtil.setEnvironmentVariable("TRAVIS_TEST_TELEGRAM_CHANNEL", "-197235129");
//        DevelopersNotificationUtil.setEnvironmentVariable("DN_MESSENGER", "ALL_AVAILABLE");

        String slackToken = DevelopersNotificationUtil.getEnvironmentVariable("TRAVIS_TEST_SLACK_TOKEN");
        String slackChannel = DevelopersNotificationUtil.getEnvironmentVariable("TRAVIS_TEST_SLACK_CHANNEL");
        String telegramToken = DevelopersNotificationUtil.getEnvironmentVariable("TRAVIS_TEST_TELEGRAM_TOKEN");
        String telegramChannel = DevelopersNotificationUtil.getEnvironmentVariable("TRAVIS_TEST_TELEGRAM_CHANNEL");

        DevelopersNotificationUtil.setEnvironmentVariable("DN", "{\"messenger\":[{\"name\":\"SLACK\",\"token\":\""+slackToken+"\",\"channel\":\""+slackChannel+"\"},{\"name\":\"TELEGRAM\",\"token\":\""+telegramToken+"\",\"channel\":\""+telegramChannel+"\"}],\"show_whole_log_details\":false,\"protection_from_spam\": \"true\",\"project_name\": \"Where this library will be invoked\",\"connect_timeout\":5000,\"user_agent\":\"Mozilla/5.0\",\"monitoring\":{\"period\":5,\"unit\":\"seconds\",\"max_ram\":90,\"max_disk\": 90,\"disk_consumption_rate\":2}}");

        A a = new A();
        a.a();
        Thread.sleep(10000);
    }


}
