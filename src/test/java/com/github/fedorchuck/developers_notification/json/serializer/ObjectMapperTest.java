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

package com.github.fedorchuck.developers_notification.json.serializer;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
public class ObjectMapperTest {

    @Test
    public void test001() throws IOException, IllegalAccessException {
        Payload payload = new Payload();
        Attachment attachment = new Attachment();
        attachment.setFallback("The message isn't supported.");
        attachment.setColor("#FF0049");
        attachment.setMrkdwn_in(new String[]{"text","fields"});
        attachment.setAuthor_name(" some project name ");

        payload.setChannel("general");
        payload.setText("it was a wonderful day");
        payload.setIcon_url("http://placehold.it/48x48");
        payload.setUsername("developers notification bot");
        payload.setAttachments(Collections.singletonList(attachment));
        String actual = new ObjectMapper().writeValueAsString(payload);
        String expected = "{\"username\":\"developers notification bot\",\"icon_url\":\"http://placehold.it/48x48\",\"text\":\"it was a wonderful day\",\"channel\":\"general\",\"attachments\":[{\"fallback\":\"The message isn't supported.\",\"color\":\"#FF0049\",\"author_name\":\" some project name \",\"mrkdwn_in\":[\"text\",\"fields\"]}]}";
        Assert.assertEquals(expected, actual);
    }
}