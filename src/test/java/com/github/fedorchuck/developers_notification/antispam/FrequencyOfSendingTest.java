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

package com.github.fedorchuck.developers_notification.antispam;


import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
public class FrequencyOfSendingTest {
    private String projectName = "unit tests";
    private String description = "FrequencyOfSendingTest#testCanSendMessage";

    @Test
    public void a1_testCanSendMessage() {
        Assert.assertEquals(true, FrequencyOfSending.canSendMessage(MessageTypes.RAM_LIMIT));
        Assert.assertEquals(true, FrequencyOfSending.canSendMessage(MessageTypes.DISK_LIMIT));
        Assert.assertEquals(true, FrequencyOfSending.canSendMessage(MessageTypes.DISK_CONSUMPTION_RATE));

        description = "FrequencyOfSendingTest#testCanSendMessage";
        Assert.assertEquals(true, FrequencyOfSending.canSendMessage(new SentMessage(MessageTypes.USERS_MESSAGE, null, projectName, description)));
        Assert.assertEquals(true, FrequencyOfSending.canSendMessage(new SentMessage(MessageTypes.RAM_LIMIT, null, projectName, description)));
        Assert.assertEquals(true, FrequencyOfSending.canSendMessage(new SentMessage(MessageTypes.DISK_LIMIT, null, projectName, description)));
        Assert.assertEquals(true, FrequencyOfSending.canSendMessage(new SentMessage(MessageTypes.DISK_CONSUMPTION_RATE, null, projectName, description)));
    }

    @Test(expected=IllegalArgumentException.class)
    public void b2_testCanSendMessageException() {
        FrequencyOfSending.canSendMessage(MessageTypes.USERS_MESSAGE);
    }

    @Test
    public void c3_testMessageSent() {
        Assert.assertEquals(true, FrequencyOfSending.canSendMessage(MessageTypes.RAM_LIMIT));
        Assert.assertEquals(true, FrequencyOfSending.canSendMessage(MessageTypes.DISK_LIMIT));
        Assert.assertEquals(true, FrequencyOfSending.canSendMessage(MessageTypes.DISK_CONSUMPTION_RATE));

        Assert.assertEquals(true, FrequencyOfSending.messageSent(MessageTypes.RAM_LIMIT));
        Assert.assertEquals(true, FrequencyOfSending.messageSent(MessageTypes.DISK_LIMIT));
        Assert.assertEquals(true, FrequencyOfSending.messageSent(MessageTypes.DISK_CONSUMPTION_RATE));

        Assert.assertEquals(false, FrequencyOfSending.canSendMessage(MessageTypes.RAM_LIMIT));
        Assert.assertEquals(false, FrequencyOfSending.canSendMessage(MessageTypes.DISK_LIMIT));
        Assert.assertEquals(false, FrequencyOfSending.canSendMessage(MessageTypes.DISK_CONSUMPTION_RATE));

        Assert.assertEquals(false, FrequencyOfSending.messageSent(MessageTypes.RAM_LIMIT));
        Assert.assertEquals(false, FrequencyOfSending.messageSent(MessageTypes.DISK_LIMIT));
        Assert.assertEquals(false, FrequencyOfSending.messageSent(MessageTypes.DISK_CONSUMPTION_RATE));

        description = "FrequencyOfSendingTest#testMessageSent";
        Assert.assertEquals(true, FrequencyOfSending.messageSent(new SentMessage(MessageTypes.USERS_MESSAGE, null, projectName, description)));
        Assert.assertEquals(false, FrequencyOfSending.messageSent(new SentMessage(MessageTypes.USERS_MESSAGE, null, projectName, description)));
    }

    @Test
    public void d4_testMessageSentException() {
        description = "FrequencyOfSendingTest#testMessageSentException";
        MessageTypes types = null;
        try {
            types = MessageTypes.USERS_MESSAGE;
            FrequencyOfSending.messageSent(types);
            Assert.fail("FrequencyOfSending.messageSent(MessageTypes.USERS_MESSAGE)");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Type " + types + " can not use this method.", ex.getMessage());
        }
    }
}