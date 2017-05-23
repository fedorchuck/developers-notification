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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This mapper (or, data binder, or codec) provides functionality for converting between
 * Java objects (instances of JDK provided core classes, beans), and matching JSON constructs.
 * The main conversion API is defined in {@link JsonWriter} and {@link JsonParser}.
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.2
 */
class JsonSerializer {
    private JsonWriter jsonWriter;
    private Writer writer;

    JsonSerializer() {
        this.writer = new StringWriter();
        this.jsonWriter = new JsonWriter(writer);
    }

    /**
     * Write inputting object as JSON string.
     * @param obj to convert
     * @throws IOException when cannot write json
     * @throws IllegalAccessException when cannot get field value
     * @since 0.1.2
     **/
    String writeValueAsString(Object obj) throws IOException, IllegalAccessException {
        jsonWriter.writeObjectBegin();

        List<Field> fields = getNotNullFields(obj);

        String canonicalField;
        String[] parts;
        for (Field field : fields) {
            parts = field.getName().split(obj.getClass().getSimpleName());
            canonicalField = parts[0];

            field.setAccessible(true);
            Object value = field.get(obj);

            jsonWriter.writeString(canonicalField);
            jsonWriter.writePropertySeparator();

            if (field.getType().isArray()) {
                jsonWriter.writeArrayBegin();
                for (int i = 0; i < ((Object[]) value).length; i++) {
                    writeValue(field.getType().getComponentType(), ((Object[]) value)[i]);
                    if ((i + 1) < (((Object[]) value).length)) {
                        jsonWriter.writeSeparator();
                    }
                }
                jsonWriter.writeArrayEnd();
            } else {
                writeValue(field.getGenericType(), value);
            }

            if (fields.indexOf(field) + 1 < fields.size()) {
                jsonWriter.writeSeparator();
            }
        }

        jsonWriter.writeObjectEnd();
        return writer.toString();
    }

    /**
     * Convert inputting {@link Object} to {@link List} of {@link Field} with not null value
     * @param object to convert
     * @return {@link List} of {@link Field} with not null value
     * @throws IllegalArgumentException when cannot get field value
     * @since 0.1.2
     **/
    private List<Field> getNotNullFields(Object object) throws IllegalAccessException {
        List<Field> fields = Arrays.asList(object.getClass().getDeclaredFields());
        List<Field> notNullFields = new ArrayList<Field>(0);
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(object) != null)
                notNullFields.add(field);
        }
        return notNullFields;
    }

    /**
     * Method write value to the {@link JsonWriter}.
     * @param type of field
     * @param value of field
     * @throws IOException when cannot write json
     * @throws IllegalAccessException when cannot get field value
     * @since 0.1.2
     **/
    private void writeValue(Type type, Object value) throws IOException, IllegalAccessException {
        if (type == String.class) {
            jsonWriter.writeString((String) value);
        } else if ((type == AtomicInteger.class) ||
                (type == AtomicLong.class) ||
                (type == BigDecimal.class) ||
                (type == BigInteger.class) ||
                (type == Byte.class) ||
                (type == Double.class) ||
                (type == Float.class) ||
                (type == Integer.class) ||
                (type == Long.class) ||
                (type == Short.class)) {
            jsonWriter.writeNumber((Number) value);
        } else if (type.toString().contains("java.util.List")) {
            jsonWriter.writeArrayBegin();
            for (Object child : (List) value) {
                writeValueAsString(child);
            }
            jsonWriter.writeArrayEnd();
        } else if (type == Boolean.class) {
            jsonWriter.writeBoolean((Boolean) value);
        }
    }

}
