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

package com.github.fedorchuck.developers_notification.helpers;

import com.github.fedorchuck.developers_notification.DevelopersNotificationMessenger;
import com.github.fedorchuck.developers_notification.antispam.MessageTypes;
import com.github.fedorchuck.developers_notification.antispam.SentMessage;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
public class LifetimeTest {

    @Test
    public void testPut() {
        Lifetime<SentMessage> l = new Lifetime<SentMessage>(100,10);
        SentMessage um = new SentMessage(MessageTypes.USERS_MESSAGE);
        Assert.assertEquals(0, l.size());
        l.put(um);
        Assert.assertEquals(1, l.size());
        l.put(um);
        Assert.assertEquals(1, l.size());
        um = new SentMessage(MessageTypes.RAM_LIMIT);
        l.put(um);
        Assert.assertEquals(2, l.size());
        l.put(um);
        Assert.assertEquals(2, l.size());
    }

    @Test
    public void testGet() {
        MessageTypes type = MessageTypes.USERS_MESSAGE;
        Lifetime<SentMessage> l = new Lifetime<SentMessage>(100,10);
        l.put(new SentMessage(type, null, "123","1"));
        SentMessage um2 = new SentMessage(type, null, "123","2");
        l.put(um2);
        l.put(new SentMessage(type, null, "123","3"));
        Assert.assertEquals(l.get(1),um2);
    }

    @Test
    public void testGetOldest() {
        MessageTypes type = MessageTypes.USERS_MESSAGE;
        Lifetime<SentMessage> l = new Lifetime<SentMessage>(100,10);
        SentMessage um1 = new SentMessage(type, null, "123","1");
        SentMessage um2 = new SentMessage(type, null, "123","2");
        l.put(um1);
        l.put(um2);
        Assert.assertEquals(2, l.size());
        Assert.assertEquals(um1, l.getOldest());
    }

    @Test
    public void testSize() {
        MessageTypes type = MessageTypes.USERS_MESSAGE;
        Lifetime<SentMessage> l = new Lifetime<SentMessage>(2,1);
        SentMessage um = new SentMessage(type, DevelopersNotificationMessenger.ALL_AVAILABLE ,"123","qwe");
        Assert.assertEquals(0, l.size());
        l.put(um);
        Assert.assertEquals(1, l.size());
        l.put(um);
        Assert.assertEquals(1, l.size());
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {}
        Assert.assertEquals(0, l.size());
    }

    @Test
    public void testContains() {
        Lifetime<SentMessage> l = new Lifetime<SentMessage>(100,10);
        SentMessage um1 = new SentMessage(MessageTypes.USERS_MESSAGE, null,"123","qwe");
        SentMessage um2 = new SentMessage(MessageTypes.DISK_LIMIT, null,"123","qwe");
        Assert.assertEquals(false, l.contains(um1));
        l.put(um1);
        Assert.assertEquals(true, l.contains(um1));
        Assert.assertEquals(true, l.contains(l.get(0)));
        Assert.assertEquals(true, l.contains(l.getOldest()));

        Assert.assertEquals(false, l.contains(um2));
        l.put(um2);
        Assert.assertEquals(true, l.contains(um2));
        Assert.assertEquals(true, l.contains(l.getOldest()));
    }
}