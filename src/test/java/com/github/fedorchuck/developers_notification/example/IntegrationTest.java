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
        String slackToken = DevelopersNotificationUtil.getEnvironmentVariable("TRAVIS_TEST_SLACK_TOKEN");
        String slackChannel = DevelopersNotificationUtil.getEnvironmentVariable("TRAVIS_TEST_SLACK_CHANNEL");
        String telegramToken = DevelopersNotificationUtil.getEnvironmentVariable("TRAVIS_TEST_TELEGRAM_TOKEN");
        String telegramChannel = DevelopersNotificationUtil.getEnvironmentVariable("TRAVIS_TEST_TELEGRAM_CHANNEL");

        DevelopersNotificationUtil.setEnvironmentVariable("DN", "{\"messenger\":[{\"name\":\"SLACK\",\"token\":\""+slackToken+"\",\"channel\":\""+slackChannel+"\"},{\"name\":\"TELEGRAM\",\"token\":\""+telegramToken+"\",\"channel\":\""+telegramChannel+"\"}],\"show_whole_log_details\":false,\"connect_timeout\":5000,\"user_agent\":\"Mozilla/5.0\",\"monitoring\":{\"period\":10,\"unit\":\"seconds\",\"max_ram\":90,\"max_write\":2}}");

        A a = new A();
        a.a();
        Thread.sleep(10000);
    }

}
