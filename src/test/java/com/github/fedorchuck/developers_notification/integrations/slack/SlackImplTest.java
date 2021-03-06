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

import com.github.fedorchuck.developers_notification.DevelopersNotificationUtil;
import com.github.fedorchuck.developers_notification.Utils;
import com.github.fedorchuck.developers_notification.model.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
public class SlackImplTest {

    @Before
    public void setUp() {
        String slackToken = DevelopersNotificationUtil.getEnvironmentVariable("TRAVIS_TEST_SLACK_TOKEN");
        String slackChannel = DevelopersNotificationUtil.getEnvironmentVariable("TRAVIS_TEST_SLACK_CHANNEL");

        String stringFullConfig = "{\"messenger\":[{\"name\":\"SLACK\",\"token\":\""+slackToken+"\",\"channel\":\""+slackChannel+"\"}],\"show_whole_log_details\":false,\"protection_from_spam\": \"true\",\"project_name\": \"Where this library will be invoked\",\"connect_timeout\":5000,\"user_agent\":\"Mozilla/5.0\",\"monitoring\":{\"period\":5,\"unit\":\"seconds\",\"max_ram\":90,\"max_disk\": 90,\"disk_consumption_rate\":2}}";
        Utils.setConfig(stringFullConfig);
    }

    @Test
    public void testGenerateMessage() {
        String expected = "{\"username\":\"developers notification bot\",\"icon_url\":\"https://raw.githubusercontent.com/fedorchuck/developers-notification/task/%2317_add_codecov/docs/website/resources/logo/48x48.png\",\"text\":\"test with full method signature\",\"channel\":\"general\",\"attachments\":[{\"fallback\":\"The message isn't supported.\",\"color\":\"#FF0049\",\"author_name\":\"developers_notification\",\"mrkdwn_in\":[\"text\",\"fields\"]}]}";
        SlackImpl slack = new SlackImpl();

        Field field;
        try {
            field = slack.getClass().getDeclaredField("channel");
            field.setAccessible(true);
            field.set(slack, "general");
        } catch (NoSuchFieldException e) {
            Assert.fail(e.getMessage());
        } catch (IllegalAccessException e) {
            Assert.fail(e.getMessage());
        }

        Task actual = slack.generateMessage("developers_notification",
                "test with full method signature", null);
        Assert.assertEquals(expected, actual.getJsonGeneratedMessages());
    }
}