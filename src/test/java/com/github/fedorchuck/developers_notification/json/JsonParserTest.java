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

import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
public class JsonParserTest {
    @Test
    public void testGetFieldName() {
        Assert.assertEquals("ok", new JsonParser("\"ok\":true").getFieldName());
    }

    @Test
    public void testGetFieldValueToken() {
        Assert.assertEquals(JsonToken.VALUE_TRUE, new JsonParser("\":true,").getFieldValueToken());
        Assert.assertEquals(JsonToken.VALUE_TRUE, new JsonParser("\":tRue,").getFieldValueToken());
        Assert.assertEquals(JsonToken.VALUE_FALSE, new JsonParser("\":false,").getFieldValueToken());
        Assert.assertEquals(JsonToken.VALUE_FALSE, new JsonParser("\":faLse,").getFieldValueToken());
        Assert.assertEquals(JsonToken.VALUE_NULL, new JsonParser("\":null,").getFieldValueToken());
        Assert.assertEquals(JsonToken.VALUE_NULL, new JsonParser("\":nULl").getFieldValueToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, new JsonParser("\":-1.5,").getFieldValueToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, new JsonParser("\":1.5,").getFieldValueToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, new JsonParser("\":-15,").getFieldValueToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, new JsonParser("\":15").getFieldValueToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, new JsonParser("\":\"abcd\",").getFieldValueToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, new JsonParser("\":\"ab cd\",").getFieldValueToken());
        Assert.assertEquals(JsonToken.START_ARRAY, new JsonParser("\":[,").getFieldValueToken());
        Assert.assertEquals(JsonToken.START_OBJECT, new JsonParser("\":{,").getFieldValueToken());
    }

    @Test
    public void testGetStringValue() {
        Assert.assertEquals("ab cd", new JsonParser("\"ab cd\"").getStringValue());
        Assert.assertEquals("ab\"cd", new JsonParser("\"ab\"cd\"").getStringValue());
    }

    @Test
    public void testGetArrayValue() {
        String expected = "[{\"type\":\"bold\",\"offset\":0,\"length\":7},{\"type\":\"bold\",\"offset\":34,\"length\":7},{\"type\":\"bold\",\"offset\":76,\"length\":9},{\"type\":\"code\",\"offset\":86,\"length\":27},{\"type\":\"bold\",\"offset\":114,\"length\":11},{\"type\":\"pre\",\"offset\":126,\"length\":238}]";
        String actual = new JsonParser(expected).getArrayValue();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetObjectValue() {
        String expected = "{\"message_id\":131,\"from\":{\"id\":317208208,\"first_name\":\"developers notification\",\"username\":\"developers_notification_bot\"},\"chat\":{\"id\":-197235129,\"title\":\"test group developers notification\",\"type\":\"group\",\"all_members_are_administrators\":true},\"date\":1494064518,\"text\":\"Project: developers-notification \\nMessage: test with full method signature \\nThrowable: java.lang.Throwable: abcd \\nStack trace: com.github.fedorchuck.developers_notification.example.B.b(B.java:28)\\ncom.github.fedorchuck.developers_notification.example.A.a(A.java:25)\\ncom.github.fedorchuck.developers_notification.example.IntegrationTest.main(IntegrationTest.java:45)\"}";
        String actual = new JsonParser(expected).getObjectValue();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetBooleanValue() {
        Assert.assertEquals("true", new JsonParser("true,").getBooleanValue());
        Assert.assertEquals("true", new JsonParser("tRue,").getBooleanValue());
        Assert.assertEquals("true", new JsonParser("tRue ").getBooleanValue());
        Assert.assertEquals("false", new JsonParser("false").getBooleanValue());
        Assert.assertEquals("false", new JsonParser("false,").getBooleanValue());
        Assert.assertEquals("false", new JsonParser("FALSE,").getBooleanValue());
    }

    @Test
    public void testGetNumericValue() {
        Assert.assertEquals("5", new JsonParser("5").getNumericValue());
        Assert.assertEquals("-8", new JsonParser("-8").getNumericValue());
        Assert.assertEquals("-5.3", new JsonParser("-5.3 ").getNumericValue());
        Assert.assertEquals("7", new JsonParser("7,").getNumericValue());
    }

}