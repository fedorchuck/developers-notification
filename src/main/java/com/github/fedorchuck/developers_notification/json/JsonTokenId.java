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
 * Interface defined to contain ids accessible with {@link JsonToken#id}. Needed to define these constants in
 * {@link JsonToken} itself, as static constants.
 *
 * @since 2.0
 */
interface JsonTokenId {

    /**
     * Id used to represent {@link JsonToken#START_OBJECT}
     */
    int ID_START_OBJECT = 1;

    /**
     * Id used to represent {@link JsonToken#END_OBJECT}
     */
    int ID_END_OBJECT = 2;

    /**
     * Id used to represent {@link JsonToken#START_ARRAY}
     */
    int ID_START_ARRAY = 3;

    /**
     * Id used to represent {@link JsonToken#END_ARRAY}
     */
    int ID_END_ARRAY = 4;

    /**
     * Id used to represent {@link JsonToken#FIELD_NAME}
     */
    int ID_FIELD_NAME = 5;

    /**
     * Id used to represent {@link JsonToken#VALUE_STRING}
     */
    int ID_STRING = 6;

    /**
     * Id used to represent {@link JsonToken#VALUE_NUMBER_INT}
     */
    int ID_NUMBER_INT = 7;

    /**
     * Id used to represent {@link JsonToken#VALUE_NUMBER_FLOAT}
     */
    int ID_NUMBER_FLOAT = 8;

    /**
     * Id used to represent {@link JsonToken#VALUE_TRUE}
     */
    int ID_TRUE = 9;

    /**
     * Id used to represent {@link JsonToken#VALUE_FALSE}
     */
    int ID_FALSE = 10;

    /**
     * Id used to represent {@link JsonToken#VALUE_NULL}
     */
    int ID_NULL = 11;

}
