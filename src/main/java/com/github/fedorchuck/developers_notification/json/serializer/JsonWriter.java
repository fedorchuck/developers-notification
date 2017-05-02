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

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

/**
 * ObjectMapper for class {@link java.io.Writer}.
 * <p>
 * Methods:
 *     <pre>
 *         {@link JsonWriter#writeObjectBegin()}
 *         {@link JsonWriter#writeObjectEnd()}
 *         {@link JsonWriter#writeArrayBegin()}
 *         {@link JsonWriter#writeArrayEnd()}
 *         {@link JsonWriter#writeString(String line)}
 *         {@link JsonWriter#writeNumber(Number writeNumber)}
 *         {@link JsonWriter#writeSeparator()}
 *         {@link JsonWriter#writePropertySeparator()}
 *         {@link JsonWriter#writeBoolean(Boolean value)}
 *         {@link JsonWriter#writeNull()}
 *         {@link JsonWriter#flush()}
 *     </pre>
 * @see <a href="https://tools.ietf.org/html/rfc4627">https://tools.ietf.org/html/rfc4627</a> chapter 2.5
 * @see <a href="http://www.json.org/">http://www.json.org/</a>
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.2
 */
class JsonWriter {

    private Writer json;

    JsonWriter(Writer json) {
        this.json = json;
    }

    /**
     * Write character <b>{</b>
     * @since 0.1.2
     **/
    void writeObjectBegin() throws IOException {
        json.append('{');
    }

    /**
     * Write character <b>}</b>
     * @since 0.1.2
     **/
    void writeObjectEnd() throws IOException {
        json.append('}');
    }

    /**
     * Write character <b>[</b>
     * @since 0.1.2
     **/
    void writeArrayBegin() throws IOException {
        json.append('[');
    }

    /**
     * Write character <b>]</b>
     * @since 0.1.2
     **/
    void writeArrayEnd() throws IOException {
        json.append(']');
    }

    /**
     * Writes downstream string, escaped characters, adds on both sides characters <b>"</b>
     * @param value to write
     *
     * @since 0.1.2
     **/
    void writeString(String value) throws IOException {
        value = value.replace("\"","\\\"");
        value = value.replace("\n","\\n");
//        value = value.replace("\\","\\u005C");

        for (int a = 0x0000; a < 0x001F; a++)
            value = value.replace(Arrays.toString(Character.toChars(a)),"\\u"+a);

        json.append("\"");
        json.append(value);
        json.append("\"");
    }

    /**
     * Writes downstream number
     * @param value to write
     *
     * @since 0.1.2
     **/
    void writeNumber(Number value) throws IOException {
        json.append(value.toString());
    }

    /**
     * Writes downstream boolean value
     * @param value to write
     *
     * @since 0.1.2
     **/
    void writeBoolean(Boolean value) throws IOException {
        if (value==null)
            writeNull();
        else {
            if (value)
                json.append("true");
            else
                json.append("false");
        }
    }

    /**
     * Writes downstream null value
     * @since 0.1.2
     **/
    void writeNull() throws IOException {
        json.append("null");
    }

    /**
     * Write character <b>,</b>
     * @since 0.1.2
     **/
    void writeSeparator() throws IOException {
        json.append(',');
    }

    /**
     * Write character <b>:</b>
     * @since 0.1.2
     **/
    void writePropertySeparator() throws IOException {
        json.append(":");
    }

    /**
     * Flushes the stream. If the stream has saved any characters from the various write() methods in a buffer,
     * write them immediately to their intended destination. Then, if that destination is another character
     * or byte stream, flush it. Thus one flush() invocation will flush all the buffers in a chain of Writers
     * and OutputStreams.
     * <p>
     * If the intended destination of this stream is an abstraction provided by the underlying operating system,
     * for example a file, then flushing the stream guarantees only that bytes previously written to the stream
     * are passed to the operating system for writing; it does not guarantee that they are actually written to
     * a physical device such as a disk drive.
     * @since 0.1.2
     **/
    void flush() throws IOException {
        json.flush();
    }
}
