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
public class SentMessageTest {
    @Test
    public void testEquals() {
        SentMessage a = new SentMessage(MessageTypes.RAM_LIMIT, "project name", "description");
        SentMessage b = new SentMessage(MessageTypes.RAM_LIMIT, "project name", "description");
        SentMessage c = new SentMessage(MessageTypes.DISK_LIMIT, "project name", "description");
        SentMessage d = new SentMessage(MessageTypes.DISK_LIMIT, "project", "description");
        SentMessage e = new SentMessage(MessageTypes.DISK_LIMIT, "project name", "description");
        SentMessage f = new SentMessage(MessageTypes.DISK_LIMIT, "project", "name");

        Assert.assertEquals(true, a.equals(b));
        Assert.assertEquals(true, b.equals(a));
        Assert.assertEquals(false, c.equals(a));
        Assert.assertEquals(false, a.equals(c));
        Assert.assertEquals(false, d.equals(a));
        Assert.assertEquals(false, a.equals(d));
        Assert.assertEquals(false, e.equals(a));
        Assert.assertEquals(false, a.equals(e));
        Assert.assertEquals(false, f.equals(a));
        Assert.assertEquals(false, a.equals(f));
    }

}