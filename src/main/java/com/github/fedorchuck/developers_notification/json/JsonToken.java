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

/**
 * Enumeration for basic token types used for returning results of parsing JSON content.
 *
 * @since 0.2.0
 */
enum JsonToken {

    /**
     * START_OBJECT is returned when encountering '{' which signals starting of an Object value.
     */
    START_OBJECT("{", JsonTokenId.ID_START_OBJECT),
        
    /**
     * END_OBJECT is returned when encountering '}' which signals ending of an Object value
     */
    END_OBJECT("}", JsonTokenId.ID_END_OBJECT),

    /**
     * START_ARRAY is returned when encountering '[' which signals starting of an Array value
     */
    START_ARRAY("[", JsonTokenId.ID_START_ARRAY),

    /**
     * END_ARRAY is returned when encountering ']' which signals ending of an Array value
     */
    END_ARRAY("]", JsonTokenId.ID_END_ARRAY),
        
    /**
     * FIELD_NAME is returned when a String token is encountered as a field name
     * (same lexical value, different function)
     */
    FIELD_NAME(null, JsonTokenId.ID_FIELD_NAME),

    /**
     * VALUE_STRING is returned when a String token is encountered in value context
     * (array element, field value, or root-level stand-alone value)
     */
    VALUE_STRING(null, JsonTokenId.ID_STRING),

    /**
     * VALUE_NUMBER_INT is returned when an integer numeric token is encountered in value context: that is, a number
     * that does not have floating point or exponent marker in it (consists only of an optional sign, followed by
     * one or more digits)
     */
    VALUE_NUMBER_INT(null, JsonTokenId.ID_NUMBER_INT),

    /**
     * VALUE_NUMBER_INT is returned when a numeric token other that is not an integer is encountered: that is, a
     * number that does have floating point or exponent marker in it, in addition to one or more digits.
     */
    VALUE_NUMBER_FLOAT(null, JsonTokenId.ID_NUMBER_FLOAT),
    
    /**
     * VALUE_TRUE is returned when encountering literal "true" in value context
     */
    VALUE_TRUE("true", JsonTokenId.ID_TRUE),

    /**
     * VALUE_FALSE is returned when encountering literal "false" in value context
     */
    VALUE_FALSE("false", JsonTokenId.ID_FALSE),

    /**
     * VALUE_NULL is returned when encountering literal "null" in value context
     */
    VALUE_NULL("null", JsonTokenId.ID_NULL),
    ;
    
    private final String serialized;
    private final int id;
    private final boolean isNumber;
    private final boolean isBoolean;

    /**
     * @param token representation for this token, if there is a single static representation; null otherwise
     */
    JsonToken(String token, int id) {
        if (token == null)
            serialized = null;
        else
            serialized = token;
        this.id = id;

        isBoolean = (id == JsonTokenId.ID_FALSE || id == JsonTokenId.ID_TRUE);
        isNumber = (id == JsonTokenId.ID_NUMBER_INT || id == JsonTokenId.ID_NUMBER_FLOAT);
    }

    public int getId() {
        return id;
    }

    /**
     * @since 0.2.0
     */
    public final String asString() { return serialized; }
    /**
     * @since 0.2.0
     **/
    public final boolean isNumeric() { return isNumber; }
    /**
     * @since 0.2.0
     **/
    public final boolean isBoolean() { return isBoolean; }
}
