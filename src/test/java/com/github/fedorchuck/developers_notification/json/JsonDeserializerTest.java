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


import com.github.fedorchuck.developers_notification.json.model.Entity;
import com.github.fedorchuck.developers_notification.json.model.Result;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
public class JsonDeserializerTest {
    @Test
    public void testReadValue() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String input = "{\"result\":{\"message_id\":131,\"entities\":[" +
                "{\"type\":\"bold\",\"offset\":0,\"length\":7}," +
                "{\"type\":\"bold\",\"offset\":34,\"length\":7}," +
                "{\"type\":\"bold\",\"offset\":76,\"length\":9}," +
                "{\"type\":\"code\",\"offset\":86,\"length\":27}," +
                "{\"type\":\"bold\",\"offset\":114,\"length\":11}," +
                "{\"type\":\"pre\",\"offset\":126,\"length\":238}" +
                "]}";
        Result actual = new JsonDeserializer().readValue(input, Result.class);

        List<Entity> entities = new ArrayList<Entity>(0);
            entities.add(new Entity("bold", 0, 7));
            entities.add(new Entity("bold", 34, 7));
            entities.add(new Entity("bold", 76, 9));
            entities.add(new Entity("code", 86, 27));
            entities.add(new Entity("bold", 114, 11));
            entities.add(new Entity("pre", 126, 238));

        Result expected = new Result();
            expected.setMessage_id(131);
            expected.setEntities(entities);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJsonToMapObjects() {
        Map<String, String> actual;
        Map<String, String> expected;
        String input = "{\"ok\":true,\"result\":{\"message_id\":131,\"from\":{\"id\":317208208,\"first_name\":\"developers notification\",\"username\":\"developers_notification_bot\"},\"chat\":{\"id\":-197235129,\"title\":\"test group developers notification\",\"type\":\"group\",\"all_members_are_administrators\":true},\"date\":1494064518,\"text\":\"Project: developers-notification \\nMessage: test with full method signature \\nThrowable: java.lang.Throwable: abcd \\nStack trace: com.github.fedorchuck.developers_notification.example.B.b(B.java:28)\\ncom.github.fedorchuck.developers_notification.example.A.a(A.java:25)\\ncom.github.fedorchuck.developers_notification.example.IntegrationTest.main(IntegrationTest.java:45)\"}}";
        actual = new JsonDeserializer().jsonToMap(input);
        expected = new HashMap<String, String>(0);
        expected.put("ok","true");//
        expected.put("result", "{\"message_id\":131,\"from\":{\"id\":317208208,\"first_name\":\"developers notification\",\"username\":\"developers_notification_bot\"},\"chat\":{\"id\":-197235129,\"title\":\"test group developers notification\",\"type\":\"group\",\"all_members_are_administrators\":true},\"date\":1494064518,\"text\":\"Project: developers-notification \\nMessage: test with full method signature \\nThrowable: java.lang.Throwable: abcd \\nStack trace: com.github.fedorchuck.developers_notification.example.B.b(B.java:28)\\ncom.github.fedorchuck.developers_notification.example.A.a(A.java:25)\\ncom.github.fedorchuck.developers_notification.example.IntegrationTest.main(IntegrationTest.java:45)\"}");
        Assert.assertEquals(expected, actual);
        actual = new JsonDeserializer().jsonToMap(expected.get("result"));
        expected = new HashMap<String, String>(0);
        expected.put("message_id","131");
        expected.put("from","{\"id\":317208208,\"first_name\":\"developers notification\",\"username\":\"developers_notification_bot\"}");
        expected.put("chat","{\"id\":-197235129,\"title\":\"test group developers notification\",\"type\":\"group\",\"all_members_are_administrators\":true}");
        expected.put("date","1494064518");
        expected.put("text","Project: developers-notification \\nMessage: test with full method signature \\nThrowable: java.lang.Throwable: abcd \\nStack trace: com.github.fedorchuck.developers_notification.example.B.b(B.java:28)\\ncom.github.fedorchuck.developers_notification.example.A.a(A.java:25)\\ncom.github.fedorchuck.developers_notification.example.IntegrationTest.main(IntegrationTest.java:45)");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJsonToMapArrays() {
        Map<String, String> actual;
        Map<String, String> expected;
        String input = "{\"ok\":true,\"result\":{\"message_id\":131,\"from\":{\"id\":317208208,\"first_name\":\"developers notification\",\"username\":\"developers_notification_bot\"},\"text\":\"Project: developers-notification \\nMessage: test with full method signature \\nThrowable: java.lang.Throwable: abcd \\nStack trace: com.github.fedorchuck.developers_notification.example.B.b(B.java:28)\\ncom.github.fedorchuck.developers_notification.example.A.a(A.java:25)\\ncom.github.fedorchuck.developers_notification.example.IntegrationTest.main(IntegrationTest.java:45)\" ,\"entities\":[{\"type\":\"bold\",\"offset\":0,\"length\":7},{\"type\":\"bold\",\"offset\":34,\"length\":7},{\"type\":\"bold\",\"offset\":76,\"length\":9},{\"type\":\"code\",\"offset\":86,\"length\":27},{\"type\":\"bold\",\"offset\":114,\"length\":11},{\"type\":\"pre\",\"offset\":126,\"length\":238}]}}";
        actual = new JsonDeserializer().jsonToMap(input);
        expected = new HashMap<String, String>(0);
        expected.put("ok","true");
        expected.put("result", "{\"message_id\":131,\"from\":{\"id\":317208208,\"first_name\":\"developers notification\",\"username\":\"developers_notification_bot\"},\"text\":\"Project: developers-notification \\nMessage: test with full method signature \\nThrowable: java.lang.Throwable: abcd \\nStack trace: com.github.fedorchuck.developers_notification.example.B.b(B.java:28)\\ncom.github.fedorchuck.developers_notification.example.A.a(A.java:25)\\ncom.github.fedorchuck.developers_notification.example.IntegrationTest.main(IntegrationTest.java:45)\" ,\"entities\":[{\"type\":\"bold\",\"offset\":0,\"length\":7},{\"type\":\"bold\",\"offset\":34,\"length\":7},{\"type\":\"bold\",\"offset\":76,\"length\":9},{\"type\":\"code\",\"offset\":86,\"length\":27},{\"type\":\"bold\",\"offset\":114,\"length\":11},{\"type\":\"pre\",\"offset\":126,\"length\":238}]}");
        Assert.assertEquals(expected, actual);

        actual = new JsonDeserializer().jsonToMap(expected.get("result"));
        expected = new HashMap<String, String>(0);
        expected.put("message_id","131");
        expected.put("from","{\"id\":317208208,\"first_name\":\"developers notification\",\"username\":\"developers_notification_bot\"}");
        expected.put("text","Project: developers-notification \\nMessage: test with full method signature \\nThrowable: java.lang.Throwable: abcd \\nStack trace: com.github.fedorchuck.developers_notification.example.B.b(B.java:28)\\ncom.github.fedorchuck.developers_notification.example.A.a(A.java:25)\\ncom.github.fedorchuck.developers_notification.example.IntegrationTest.main(IntegrationTest.java:45)");
        expected.put("entities","[{\"type\":\"bold\",\"offset\":0,\"length\":7},{\"type\":\"bold\",\"offset\":34,\"length\":7},{\"type\":\"bold\",\"offset\":76,\"length\":9},{\"type\":\"code\",\"offset\":86,\"length\":27},{\"type\":\"bold\",\"offset\":114,\"length\":11},{\"type\":\"pre\",\"offset\":126,\"length\":238}]");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testJsonToMapArray() {
        Map<String, String> actual;
        Map<String, String> expected = new HashMap<String, String>(0);
        expected.put("entities", "[{\"type\": \"bold\",\"offset\": 0,\"length\": 7}, { \"type\": \"bold\", \"offset\": 114, \"length\": 11}, { \"type\": \"pre\", \"offset\": 126, \"length\": 238}]");
        actual = new JsonDeserializer().jsonToMap("{\"entities\" : [{\"type\": \"bold\",\"offset\": 0,\"length\": 7}, { \"type\": \"bold\", \"offset\": 114, \"length\": 11}, { \"type\": \"pre\", \"offset\": 126, \"length\": 238}]}");
        Assert.assertEquals(expected, actual);
    }
}