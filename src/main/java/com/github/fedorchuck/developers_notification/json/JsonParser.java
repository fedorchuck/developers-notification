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
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class contain methods for parsing JSON
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.2.0
 */
class JsonParser {
    /**
     * input JSON to parse
     **/
    private final String json;
    /**
     * equal <code>{@link #json}.length()</code>
     **/
    private final int lengthOfString;
    /**
     * equal <code>{@link #json}.length() - 1</code>
     **/
    private final int lastIndexOfString;
    /**
     * Results of parsing JSON content
     **/
    @Getter
    private JsonToken valueToken;
    /**
     * Current position in JSON line
     **/
    @Getter
    private int currentPosition;
    /**
     * Results of parsing JSON content
     **/
    @Getter
    private String parsingContext;

    /**
    * @param json to parse
    **/
    JsonParser(String json) {
        this.json = json;
        this.lengthOfString = json.length();
        this.lastIndexOfString = lengthOfString - 1;
    }

    /**
     * Return answer: is JSON has next element
     * @return true if current JSON contains next element and false if not
     * @since 0.2.0
     * */
    boolean hasNext() {
        return currentPosition < lastIndexOfString;
    }

    /**
     * Return field name
     * @return field name
     * @since 0.2.0
     **/
    String getFieldName() {
        char currentChar;
        StringBuilder value = new StringBuilder();

        if (json.charAt(currentPosition)!='"')
            throw new JsonProcessingException("Unexpected char: " + json.charAt(currentPosition) +
                    " Position: " + currentPosition + " JSON: \n" + json);

        currentPosition = currentPosition + 1; //remove first <">

        for (int i = currentPosition; i < lengthOfString; i++) {
            currentChar = json.charAt(i);
            switch (currentChar){
                case '\"':
                    moveToNextCharPosition(i);
                    valueToken = JsonToken.FIELD_NAME;
                    parsingContext = value.toString();
                    return parsingContext;
                case ',':
                case ' ':
                case '\\':
                case ':':
                    throw new JsonProcessingException("Unexpected char: " + json.charAt(currentPosition) +
                            " Position: " + currentPosition + " JSON: \n" + json);
                default:
                    value.append(currentChar);
                    break;
            }
        }
        throw new JsonProcessingException("Unknown error. Is it JSON? Position: " + currentPosition);
    }

    /**
     * Analise what the object are next
     * @return {@link JsonToken} with characteristic of the next object
     * @since 0.2.0
     **/
    JsonToken getFieldValueToken() {
        skipPropertySeparator();
        switch (json.charAt(currentPosition)) {
            case '"':
                valueToken = JsonToken.VALUE_STRING;
                break;
            case '[':
                valueToken = JsonToken.START_ARRAY;
                break;
            case '{':
                valueToken = JsonToken.START_OBJECT;
                break;
            case 'T':
            case 't':
                valueToken = matchTrue();
                break;
            case 'F':
            case 'f':
                valueToken = matchFalse();
                break;
            case 'N':
            case 'n':
                valueToken = matchNull();
                break;
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                valueToken = matchNumber();
                break;
            default:
                throw new JsonProcessingException("Unexpected char: " + json.charAt(currentPosition) +
                        " Position: " + currentPosition);
        }
        return valueToken;
    }

    /**
     * Return value of field for string
     * @return string value of field
     * @since 0.2.0
     **/
    String getStringValue() {
        char currentChar;
        char nextChar;
        int nextCharIndex;
        Object[] nextCharIgnoringSpace;
        StringBuilder value = new StringBuilder();
        currentPosition = currentPosition + 1; //remove first <">

        for (int i = currentPosition; i < json.length(); i++) {
            currentChar = json.charAt(i);
            if (lastIndexOfString >= i+1) {
                nextCharIgnoringSpace = getNextCharIgnoringSpace(i);
                nextChar = (Character) nextCharIgnoringSpace[0];
                nextCharIndex = (Integer) nextCharIgnoringSpace[1];
            } else {
                nextChar = json.charAt(lastIndexOfString);
                nextCharIndex = lastIndexOfString;
            }

            if ((currentChar == '\\') && (nextChar == '\"')) { // \"
                if (i == lastIndexOfString) {
                    currentPosition = i;
                    break;
                } else
                    value.append("\"");
            } else if ((currentChar == '\"') && (nextChar == ',')) { // ",
                currentPosition = nextCharIndex;
                break;
            } else if ((currentChar == '\"') && (i == lastIndexOfString)) { // "
                currentPosition = i;
                break;
            } else
                value.append(currentChar);
        }

        parsingContext = value.toString();
        valueToken = JsonToken.VALUE_STRING;

        return parsingContext;
    }

    /**
     * Return value of field for JSON array
     * @return JSON array value of field as string
     * @since 0.2.0
     **/
    String getArrayValue() {
        StringBuilder jsonArray = new StringBuilder();
        int nesting = 0;
        char currentChar;
        for (int i = currentPosition; i < lengthOfString; i++) {
            currentChar = json.charAt(i);

            if (currentChar=='[')
                nesting++;
            else if (currentChar==']')
                nesting--;

            jsonArray.append(currentChar);
            if (nesting==0) {
                currentPosition = i;
                break;
            }
        }
        parsingContext = jsonArray.toString();
        return parsingContext;
    }

    /**
     * Return value of field for JSON array
     * @return JSON array value of field as string
     * @since 0.2.0
     **/
    List<String> getJsonArrayValue() {
        List<String> jsonArray = new ArrayList<String>(0);
        int nesting = 0;
        int startStatement = currentPosition;
        char currentChar;
        for (int i = currentPosition; i < lengthOfString; i++) {
            currentChar = json.charAt(i);
            if (currentChar=='{') {
                nesting++;
            } else if (currentChar=='}') {
                nesting--;
            }

            if (nesting==0) {
                jsonArray.add(json.substring(startStatement, i+1));
                for (int j = i+1; j < lengthOfString; j++) {
                    if (json.charAt(j)=='{') {
                        startStatement = j;
                        break;
                    }
                }
                i++;
            }
        }

        return jsonArray;
    }

    /**
     * Return value of field for JSON object
     * @return JSON object value of field as string
     * @since 0.2.0
     **/
    String getObjectValue() {
        StringBuilder jsonObject = new StringBuilder();
        int nesting = 0;
        char currentChar;
        for (int i = currentPosition; i < lengthOfString; i++) {
            currentChar = json.charAt(i);
            if (currentChar=='{')
                nesting++;
            else if (currentChar=='}')
                nesting--;
            jsonObject.append(currentChar);
            if (nesting==0) {
                currentPosition = i;
                break;
            }
        }
        parsingContext = jsonObject.toString();
        return parsingContext;
    }

    /**
     * Return boolean value of field as string
     * @return boolean value of field as string
     * @since 0.2.0
     **/
    String getBooleanValue() {
        if ((valueToken!=null) && (valueToken.isBoolean()))
            return parsingContext;
        else {
            switch (json.charAt(currentPosition)) {
                case 'T':
                case 't':
                    valueToken = matchTrue();
                    break;
                case 'F':
                case 'f':
                    valueToken = matchFalse();
                    break;
            }
            return parsingContext;
        }
    }

    /**
     * Return numeric value of field as string
     * @return numeric value of field as string
     * @since 0.2.0
     **/
    String getNumericValue() {
        if ((valueToken!=null) && (valueToken.isNumeric()))
            return parsingContext;
        else {
            matchNumber();
            return parsingContext;
        }
    }

    /**
     * Changes {@link JsonParser#currentPosition} to the next char sequence started. Skip separators:
     * <code>
     *     <b>space</b>
     *     <b>"</b>
     *     <b>]</b>
     *     <b>}</b>
     * </code>
     *
     * @since 0.2.0
     **/
    void skipSeparator() {
        char currentChar;
        for (int i = currentPosition; i < lengthOfString; i++) {
            currentChar = json.charAt(i);
            switch (currentChar) {
                case ' ':
                case '\"':
                case ']':
                case '}':
                    moveToNextCharPosition(i);
                    return;
                default:
                    break;
            }
        }
    }

    /**
     * Method check, is characters sequence equals to digital sequence.
     * @return
     * <ul>
     *      <li> {@link JsonToken#VALUE_NUMBER_FLOAT} if sequence equals to double number;</li>
     *      <li> {@link JsonToken#VALUE_NUMBER_INT} if sequence equals to integer number;</li>
     *      <li> {@link JsonToken#VALUE_STRING} if sequence equals to string sequence;</li>
     * </ul>
     * @since 0.2.0
     **/
    private JsonToken matchNumber() {
        char currentChar;
        boolean isFloat = false;
        StringBuilder parsingNumber = new StringBuilder();
        for (int i = currentPosition; i < lengthOfString; i++) {
            currentChar = json.charAt(i);
            if (currentChar == '-') {
                parsingNumber.append(currentChar);
            } else if (currentChar == '.') {
                isFloat = true;
                parsingNumber.append(currentChar);
            } else if (currentChar == ' ' || currentChar == ',') {
                currentPosition = i;
                break;

            } else if (!Character.isDigit(currentChar)) {
                return JsonToken.VALUE_STRING;
            } else if (i == lastIndexOfString) {
                currentPosition = i;
                parsingNumber.append(currentChar);
                break;
            } else
                parsingNumber.append(currentChar);
        }
        parsingContext = parsingNumber.toString();
        if (isFloat)
            return JsonToken.VALUE_NUMBER_FLOAT;
        else
            return JsonToken.VALUE_NUMBER_INT;
    }

    /**
     * Method check, is characters sequence equals to <code>NULL</code>.
     * @return
     * <ul>
     *      <li> {@link JsonToken#VALUE_NULL} if sequence equals to <code>null</code>;</li>
     *      <li> {@link JsonToken#VALUE_STRING} if sequence equals to string sequence;</li>
     * </ul>
     * @since 0.2.0
     **/
    private JsonToken matchNull() {
        if (
                ((json.charAt(currentPosition)     == 'n') || (json.charAt(currentPosition)     == 'N')) &&
                ((json.charAt(currentPosition + 1) == 'u') || (json.charAt(currentPosition + 1) == 'U')) &&
                ((json.charAt(currentPosition + 2) == 'l') || (json.charAt(currentPosition + 2) == 'L')) &&
                ((json.charAt(currentPosition + 3) == 'l') || (json.charAt(currentPosition + 3) == 'L'))
            ) {
            currentPosition = currentPosition + 3;
            parsingContext = "null";
            return JsonToken.VALUE_NULL;
        } else
            return JsonToken.VALUE_STRING;
    }

    /**
     * Method check, is characters sequence equals to <code>false</code>.
     * @return
     * <ul>
     *      <li> {@link JsonToken#VALUE_FALSE} if sequence equals to <code>false</code>;</li>
     *      <li> {@link JsonToken#VALUE_STRING} if sequence equals to string sequence;</li>
     * </ul>
     * @since 0.2.0
     **/
    private JsonToken matchFalse() {
        if (
                ((json.charAt(currentPosition)     == 'f') || (json.charAt(currentPosition)     == 'F')) &&
                ((json.charAt(currentPosition + 1) == 'a') || (json.charAt(currentPosition + 1) == 'A')) &&
                ((json.charAt(currentPosition + 2) == 'l') || (json.charAt(currentPosition + 2) == 'L')) &&
                ((json.charAt(currentPosition + 3) == 's') || (json.charAt(currentPosition + 3) == 'S')) &&
                ((json.charAt(currentPosition + 4) == 'e') || (json.charAt(currentPosition + 4) == 'E'))
            ) {
            currentPosition = currentPosition + 4;
            parsingContext = "false";
            return JsonToken.VALUE_FALSE;
        } else
            return JsonToken.VALUE_STRING;
    }

    /**
     * Method check, is characters sequence equals to <code>true</code>.
     * @return
     * <ul>
     *      <li> {@link JsonToken#VALUE_TRUE} if sequence equals to <code>true</code>;</li>
     *      <li> {@link JsonToken#VALUE_STRING} if sequence equals to string sequence;</li>
     * </ul>
     * @since 0.2.0
     **/
    private JsonToken matchTrue() {
        if (
                ((json.charAt(currentPosition)     == 't') || (json.charAt(currentPosition)     == 'T')) &&
                ((json.charAt(currentPosition + 1) == 'r') || (json.charAt(currentPosition + 1) == 'R')) &&
                ((json.charAt(currentPosition + 2) == 'u') || (json.charAt(currentPosition + 2) == 'U')) &&
                ((json.charAt(currentPosition + 3) == 'e') || (json.charAt(currentPosition + 3) == 'E'))
            ) {
            currentPosition = currentPosition + 3;
            parsingContext = "true";
            return JsonToken.VALUE_TRUE;
        } else
            return JsonToken.VALUE_STRING;
    }

    /**
     * Changes {@link JsonParser#currentPosition} to the next field name. Skip
     * {@link JsonWriter#writePropertySeparator()} <bold>:</bold>
     * @since 0.2.0
     **/
    private void skipPropertySeparator() {
        if (json.charAt(currentPosition)!='\"')
            throw new JsonProcessingException("Unexpected char: " + json.charAt(currentPosition) +
                    " Position: " + currentPosition);

        currentPosition = currentPosition + 1; //remove first <">

        char currentChar;
        int state = 0;
        for (int i = currentPosition; i < lengthOfString; i++) {
            currentChar = json.charAt(i);
            switch (currentChar) {
                case '\"'://"
                    state++;
                    break;
                case ':'://:
                    state++;
                    break;
                case '[':
                case '{':
                case 'T':
                case 't':
                case 'F':
                case 'f':
                case 'N':
                case 'n':
                case '-':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    currentPosition = i;
                    return;
                default: //skip char
                    break;
            }
            if (state == 2) {
                moveToNextCharPosition(i);
                return;
            }
        }
    }

    /**
     * Changes {@link JsonParser#currentPosition} to the next char position, except <code>space</code>.
     * @since 0.2.0
     **/
    private void moveToNextCharPosition(int i) {
        if ((i >= lastIndexOfString))
            return;

        char currentChar;
        for (; i < lengthOfString; i++) {
            currentChar = json.charAt(i);
            switch (currentChar) {
                case '}':
                case ']':
                case ',':
                case ' ':
                    break;
                default:
                    currentPosition = i;
                    return;
            }
        }
    }

    /**
     * Return next character and his position from current position ignoring space.
     * @return Object array which first element is next character and second element is next character position
     * @since 0.2.0
     **/
    private Object[] getNextCharIgnoringSpace(int currentPosition) {
        if (currentPosition - 1 > lastIndexOfString || currentPosition < 0)
            throw new JsonProcessingException("bad index: " + currentPosition);
        if (currentPosition == lastIndexOfString) {
            return new Object[]{json.charAt(lastIndexOfString), lastIndexOfString};
        }

        char nextChar = 0;
        int i = currentPosition + 1;//get next char
        while (i < lastIndexOfString) {
            if (json.charAt(i)!=' ') {
                nextChar = json.charAt(i);
                break;
            }
            i++;
        }
        return new Object[]{nextChar, i};
    }

}
