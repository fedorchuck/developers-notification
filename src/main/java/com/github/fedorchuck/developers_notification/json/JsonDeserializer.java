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

import com.github.fedorchuck.developers_notification.json.exceptions.JsonProcessingException;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This mapper (or, data binder, or codec) provides functionality for converting between
 * Java objects (instances of JDK provided core classes, beans), and matching JSON constructs.
 * The main conversion API is defined in {@link JsonParser}.
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.2.0
 */
class JsonDeserializer {
    /**
     * Method to deserialize JSON content from given JSON content String.
     * @param json to convert to instance of class
     * @param clazz to map JSON
     * @throws NoSuchMethodException when got problem creating class from inputted JSON and class
     * @throws IllegalAccessException when got problem creating class from inputted JSON and class
     * @throws InvocationTargetException when got problem creating class from inputted JSON and class
     * @throws InstantiationException when got problem creating class from inputted JSON and class
     * @return created class
     * @since 0.2.0
     */
    @SuppressWarnings("unchecked")
    <T> T readValue(String json, Class<T> clazz)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        Constructor<T> constructor = clazz.getConstructor();
        constructor.setAccessible(true);
        T createdClass = constructor.newInstance();
        constructor.setAccessible(false);
        Map<String, String> values = jsonToMap(json);
        for (Field field : fields) {
            if (values.containsKey(field.getName())) {
                Type type = field.getGenericType();
                if (
                        (type == String.class)          ||
                                (type == AtomicInteger.class)   ||
                                (type == AtomicLong.class)      ||
                                (type == BigDecimal.class)      ||
                                (type == BigInteger.class)      ||
                                (type == Byte.class)            ||
                                (type == Double.class)          ||
                                (type == Float.class)           ||
                                (type == Integer.class)         ||
                                (type == Long.class)            ||
                                (type == Short.class)           ||
                                (type == Boolean.class)
                        ) {
                    setField(createdClass, field, values.get(field.getName()));
                } else if ("java.util.List".equals(field.getType().getName())) {
                    Type[] types = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                    List<Object> array = readArray(values.get(field.getName()), types[0]);
                    setField(createdClass, field, array);
                } else {
                    Object innerCreatedClass = readValue(values.get(field.getName()), field.getType());
                    setField(createdClass, field, innerCreatedClass);
                }
            }
        }
        return createdClass;
    }

    /**
     * Method to deserialize JSON array from given JSON as String.
     * @param json to convert to instance of class
     * @param type to map JSON
     * @throws NoSuchMethodException when got problem creating class from inputted JSON and class
     * @throws IllegalAccessException when got problem creating class from inputted JSON and class
     * @throws InvocationTargetException when got problem creating class from inputted JSON and class
     * @throws InstantiationException when got problem creating class from inputted JSON and class
     * @return list of mapped JSON objects
     * @since 0.2.0
     */
    private List<Object> readArray(String json, Type type)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException
    {
        json = removeArrayBrackets(json);
        JsonParser jsonParser = new JsonParser(json);
        List<Object> response = new ArrayList<Object>(0);
        List<String> stringArray = jsonParser.getJsonArrayValue();
        for (String jsonObject : stringArray) {
            Object a = readValue(jsonObject, (Class<?>) type);
            response.add(a);
        }
        return response;
    }

    /**
     * The method setting deserialized JSON value to chosen field of object instance.
     * @param instance of class where will be set value
     * @param field where will be set value
     * @param value which will be setting
     * @throws IllegalAccessException when got problem creating class from inputted JSON and class
     * @since 0.2.0
     */
    private <T> void setField(T instance, Field field, Object value) throws IllegalAccessException {
        boolean accessible = field.isAccessible();
        Type type = field.getType();
        field.setAccessible(true);
        String stringValue = String.valueOf(value);

        if (type == String.class) {
            field.set(instance, stringValue);
        } else if (type == AtomicInteger.class) {
            field.set(instance, new AtomicInteger(Integer.parseInt(stringValue)));
        } else if (type == AtomicLong.class) {
            field.set(instance, new AtomicLong(Long.parseLong(stringValue)));
        } else if (type == BigDecimal.class) {
            if (stringValue.contains(".")) {
                field.set(instance, BigDecimal.valueOf(Double.parseDouble(stringValue)));
            } else {
                field.set(instance, BigDecimal.valueOf(Long.parseLong(stringValue)));
            }
        } else if (type == BigInteger.class) {
            field.set(instance, BigInteger.valueOf(Long.parseLong(stringValue)));
        } else if (type == Byte.class) {
            field.set(instance, Byte.parseByte(stringValue));
        } else if (type == Double.class) {
            field.set(instance, Double.parseDouble(stringValue));
        } else if (type == Float.class) {
            field.set(instance, Float.parseFloat(stringValue));
        } else if (type == Integer.class) {
            field.set(instance, Integer.parseInt(String.valueOf(value)));
        } else if (type == Long.class) {
            field.set(instance, Long.parseLong(stringValue));
        } else if (type == Short.class) {
            field.set(instance, Short.parseShort(stringValue));
        } else if (type.toString().contains("java.util.List")) {
            field.set(instance, value);
        } else if (type == Boolean.class) {
            field.set(instance, Boolean.parseBoolean(stringValue));
        } else if (type == value.getClass()) {
            field.set(instance, value);
        }

        field.setAccessible(accessible);
    }

    /**
     * Method convert JSON content from given JSON as String to Map<<code>fieldName, jsonValue</code>>
     * @param json which will be converted to Map
     * @return Map with <code>fieldName</code> as key and <code>jsonValue</code> as value of converted JSON
     * @since 0.2.0
     */
    Map<String, String> jsonToMap(String json) {
        json = removeObjectBrackets(json);
        JsonParser jsonParser = new JsonParser(json);
        Map<String, String> response = new HashMap<String, String>(0);
        while (jsonParser.hasNext()) {
            String fieldName = jsonParser.getFieldName();
            JsonToken token = jsonParser.getFieldValueToken();
            switch (token) {
                case VALUE_STRING:
                    response.put(fieldName, jsonParser.getStringValue());
                    jsonParser.skipSeparator();
                    break;
                case START_ARRAY:
                    response.put(fieldName, jsonParser.getArrayValue());
                    jsonParser.skipSeparator();
                    break;
                case START_OBJECT:
                    response.put(fieldName, jsonParser.getObjectValue());
                    jsonParser.skipSeparator();
                    break;
                case VALUE_NULL:
                    response.put(fieldName, null);
                    jsonParser.skipSeparator();
                    break;
                case VALUE_TRUE:
                case VALUE_FALSE:
                    response.put(fieldName, jsonParser.getBooleanValue());
                    jsonParser.skipSeparator();
                    break;
                case VALUE_NUMBER_INT:
                case VALUE_NUMBER_FLOAT:
                    response.put(fieldName, jsonParser.getNumericValue());
                    jsonParser.skipSeparator();
                    break;
                default:
                    throw new JsonProcessingException(token.asString(), new IllegalStateException("Invalid token. "));
            }
        }
        jsonParser = null;
        return response;
    }

    /**
     * Method remove first and last brackets for
     * {@link JsonToken#START_OBJECT} and {@link JsonToken#END_OBJECT} from inputted string.
     * @param json - inputted string
     * @return input string without brackets
     * @since 0.2.0
     **/
    private String removeObjectBrackets(String json) {
        int first = 0, second = 0;

        for (int i = 0; i < json.length() - 1; i++) {
            if (json.charAt(i) == '{') {
                first = i;
                break;
            }
        }
        for (int i = json.length() - 1; i > 0; i--) {
            if (json.charAt(i) == '}') {
                second = i;
                break;
            }
        }

        return json.substring(first + 1, second);
    }

    /**
     * Method remove first and last brackets for {@link JsonToken#START_ARRAY}, {@link JsonToken#END_ARRAY}
     * from inputted string.
     * @param json - inputted string
     * @return input string without brackets
     * @since 0.2.0
     **/
    private String removeArrayBrackets(String json) {
        int first = 0, second = 0;

        for (int i = 0; i < json.length() - 1; i++) {
            if (json.charAt(i) == '[') {
                first = i;
                break;
            }
        }
        for (int i = json.length() - 1; i > 0; i--) {
            if (json.charAt(i) == ']') {
                second = i;
                break;
            }
        }

        return json.substring(first + 1, second);
    }
}
