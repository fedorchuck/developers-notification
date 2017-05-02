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
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
public class JsonWriterTest {

    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @Before
    public void setUp() throws Exception {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    public void test001() throws IOException {

        jsonWriter.writeObjectBegin();
        jsonWriter.writeString("hello");
        jsonWriter.writePropertySeparator();
        jsonWriter.writeObjectBegin();
        jsonWriter.writeString("x");
        jsonWriter.writePropertySeparator();
        jsonWriter.writeNumber(10);
        jsonWriter.writeSeparator();
        jsonWriter.writeString("y");
        jsonWriter.writePropertySeparator();
        jsonWriter.writeNumber(12.5);
        jsonWriter.writeObjectEnd();
        jsonWriter.writeSeparator();
        jsonWriter.writeString("friends");
        jsonWriter.writePropertySeparator();
        jsonWriter.writeArrayBegin();
        jsonWriter.writeString("bob");
        jsonWriter.writeSeparator();
        jsonWriter.writeString("john");
        jsonWriter.writeArrayEnd();
        jsonWriter.writeSeparator();
        jsonWriter.writeString("a");
        jsonWriter.writePropertySeparator();
        jsonWriter.writeNumber(5);
        jsonWriter.writeSeparator();
        jsonWriter.writeString("b");
        jsonWriter.writePropertySeparator();
        jsonWriter.writeNumber(5445.6);
        jsonWriter.writeSeparator();
        jsonWriter.writeString("points");
        jsonWriter.writePropertySeparator();
        jsonWriter.writeArrayBegin();
        jsonWriter.writeObjectBegin();
        jsonWriter.writeString("x");
        jsonWriter.writePropertySeparator();
        jsonWriter.writeNumber(10);
        jsonWriter.writeSeparator();
        jsonWriter.writeString("y");
        jsonWriter.writePropertySeparator();
        jsonWriter.writeNumber(12.5);
        jsonWriter.writeObjectEnd();
        jsonWriter.writeSeparator();
        jsonWriter.writeObjectBegin();
        jsonWriter.writeString("x");
        jsonWriter.writePropertySeparator();
        jsonWriter.writeNumber(10);
        jsonWriter.writeSeparator();
        jsonWriter.writeString("y");
        jsonWriter.writePropertySeparator();
        jsonWriter.writeNumber(12.5);
        jsonWriter.writeObjectEnd();
        jsonWriter.writeArrayEnd();
        jsonWriter.writeObjectEnd();


        String actual = stringWriter.toString();
        String expected = "{\"hello\":{\"x\":10,\"y\":12.5},\"friends\":[\"bob\",\"john\"],\"a\":5,\"b\":5445.6,\"points\":[{\"x\":10,\"y\":12.5},{\"x\":10,\"y\":12.5}]}";
        Assert.assertEquals(expected, actual);
    }
}