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
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
public class TelegramImplTest {

    @Test
    public void generateMessage() throws Exception {
        DevelopersNotificationUtil.setEnvironmentVariable("DN", "{\"messenger\":[{\"name\":\"TELEGRAM\",\"token\":\"TEST\",\"channel\":\"-0123456789\"}]}");

        String expected = "{\"chat_id\":\"-0123456789\",\"parse_mode\":\"Markdown\",\"text\":\"*Project*: developers-notification \\n*Message*: test with full method signature \\n\"}";
        TelegramImpl telegram = new TelegramImpl();
        String actual = telegram.generateMessage("developers_notification",
                "test with full method signature", null);
        Assert.assertEquals(expected, actual);
    }
}