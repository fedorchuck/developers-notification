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

import com.github.fedorchuck.developers_notification.json.exceptions.JsonDecodeException;
import com.github.fedorchuck.developers_notification.json.exceptions.JsonEncodeException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
public class Json {

    public static String encode(Object obj) throws JsonEncodeException {
        try {
            return new JsonSerializer().writeValueAsString(obj);
        } catch (Exception e) {
            throw new JsonEncodeException("Failed to encode as JSON: " + e.getMessage());
        }
    }

    /**
    * @throws JsonDecodeException if the input JSON structure does not match structure
    * expected for result type (or has other mismatch issues)
    **/
    public static <T> T decodeValue(String str, Class<T> clazz) throws JsonDecodeException {
        try {
            return new JsonDeserializer().readValue(str, clazz);
        } catch (Exception e) {
            throw new JsonDecodeException("Failed to decode:" + e.getMessage());
        }
    }

    /**
     * @throws JsonDecodeException if the input JSON structure does not match structure
     * expected for result type (or has other mismatch issues)
     **/
    public static <T> T decodeValue(InputStream inputStream, Class<T> clazz) throws JsonDecodeException {
        try {
            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            final StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(inputStream, "UTF-8");
            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
            return decodeValue(out.toString(), clazz);
        } catch (Exception e) {
            throw new JsonDecodeException("Failed to decode:" + e.getMessage());
        }
    }
}
