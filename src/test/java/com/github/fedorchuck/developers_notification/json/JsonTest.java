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

package com.github.fedorchuck.developers_notification.json;

import com.github.fedorchuck.developers_notification.json.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
public class JsonTest {

    @Test
    public void testEncode() {
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
        String actual = Json.encode(payload);
        String expected = "{\"username\":\"developers notification bot\",\"icon_url\":\"http://placehold.it/48x48\",\"text\":\"it was a wonderful day\",\"channel\":\"general\",\"attachments\":[{\"fallback\":\"The message isn't supported.\",\"color\":\"#FF0049\",\"author_name\":\" some project name \",\"mrkdwn_in\":[\"text\",\"fields\"]}]}";

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDecodeValue() {
        String input = "{\"ok\":true,\"result\":{\"message_id\":131,\"from\":{\"id\":317208208,\"first_name\":\"developers notification\",\"username\":\"developers_notification_bot\"},\"chat\":{\"id\":-197235129,\"title\":\"test group developers notification\",\"type\":\"group\",\"all_members_are_administrators\":true},\"date\":1494064518,\"text\":\"Project: developers-notification \\nMessage: test with full method signature \\nThrowable: java.lang.Throwable: abcd \\nStack trace: com.github.fedorchuck.developers_notification.example.B.b(B.java:28)\\ncom.github.fedorchuck.developers_notification.example.A.a(A.java:25)\\ncom.github.fedorchuck.developers_notification.example.IntegrationTest.main(IntegrationTest.java:45)\"}}";

        From from = new From();
            from.setId(317208208);
            from.setFirst_name("developers notification");
            from.setUsername("developers_notification_bot");
        Chat chat = new Chat();
            chat.setId(-197235129);
            chat.setTitle("test group developers notification");
            chat.setType("group");
            chat.setAll_members_are_administrators(true);

        Result result = new Result();
            result.setMessage_id(131);
            result.setFrom(from);
            result.setChat(chat);
            result.setDate(1494064518);
            result.setText("Project: developers-notification \\nMessage: test with full method signature \\nThrowable: java.lang.Throwable: abcd \\nStack trace: com.github.fedorchuck.developers_notification.example.B.b(B.java:28)\\ncom.github.fedorchuck.developers_notification.example.A.a(A.java:25)\\ncom.github.fedorchuck.developers_notification.example.IntegrationTest.main(IntegrationTest.java:45)");

        A expected = new A();
            expected.setOk(true);
            expected.setResult(result);
        A actual = Json.decodeValue(input, A.class);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDecodeValueWithArray() {
        String input = "{\"ok\":true,\"result\":{\"message_id\":131,\"from\":{\"id\":317208208,\"first_name\":\"developers notification\",\"username\":\"developers_notification_bot\"},\"chat\":{\"id\":-197235129,\"title\":\"test group developers notification\",\"type\":\"group\",\"all_members_are_administrators\":true},\"date\":1494064518,\"text\":\"Project: developers-notification \\nMessage: test with full method signature \\nThrowable: java.lang.Throwable: abcd \\nStack trace: com.github.fedorchuck.developers_notification.example.B.b(B.java:28)\\ncom.github.fedorchuck.developers_notification.example.A.a(A.java:25)\\ncom.github.fedorchuck.developers_notification.example.IntegrationTest.main(IntegrationTest.java:45)\",\"entities\":[{\"type\":\"bold\",\"offset\":0,\"length\":7},{\"type\":\"bold\",\"offset\":34,\"length\":7},{\"type\":\"bold\",\"offset\":76,\"length\":9},{\"type\":\"code\",\"offset\":86,\"length\":27},{\"type\":\"bold\",\"offset\":114,\"length\":11},{\"type\":\"pre\",\"offset\":126,\"length\":238}]}}";
        List<Entity> entities = new ArrayList<Entity>(0);
        entities.add(new Entity("bold", 0, 7));
        entities.add(new Entity("bold", 34, 7));
        entities.add(new Entity("bold", 76, 9));
        entities.add(new Entity("code", 86, 27));
        entities.add(new Entity("bold", 114, 11));
        entities.add(new Entity("pre", 126, 238));

        From from = new From();
            from.setId(317208208);
            from.setFirst_name("developers notification");
            from.setUsername("developers_notification_bot");
        Chat chat = new Chat();
            chat.setId(-197235129);
            chat.setTitle("test group developers notification");
            chat.setType("group");
            chat.setAll_members_are_administrators(true);

        Result result = new Result();
            result.setMessage_id(131);
            result.setFrom(from);
            result.setChat(chat);
            result.setDate(1494064518);
            result.setText("Project: developers-notification \\nMessage: test with full method signature \\nThrowable: java.lang.Throwable: abcd \\nStack trace: com.github.fedorchuck.developers_notification.example.B.b(B.java:28)\\ncom.github.fedorchuck.developers_notification.example.A.a(A.java:25)\\ncom.github.fedorchuck.developers_notification.example.IntegrationTest.main(IntegrationTest.java:45)");
            result.setEntities(entities);

        A expected = new A();
            expected.setOk(true);
            expected.setResult(result);
        A actual = Json.decodeValue(input, A.class);

        Assert.assertEquals(expected, actual);
    }

}