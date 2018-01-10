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

package com.github.fedorchuck.developers_notification;

import com.github.fedorchuck.developers_notification.configuration.Messenger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Class contains helper methods
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.0
 */
@SuppressWarnings("SameParameterValue")
public class DevelopersNotificationUtil {

    /**
     * Setting environment variables using reflection.
     * <p>
     *     <b>NOTE:</b>
     *     System environment variables are accessible by <b>any</b> process.
     *     Java system properties are <b>only</b> accessible by the process they are added to.
     *     Probably, In your case should use <i>Java system properties</i> -
     *     for it you can use, for example, {@link System#setProperty(String, String)}
     * </p>
     *
     * Method is deprecated. Will be removed at v 0.4.0
     *
     * @param key name of property
     * @param value of property
     * @throws IllegalStateException if failed to set environment variable
     * @since 0.1.0
     **/
    @SuppressWarnings("unchecked")
    @Deprecated
    public static void setEnvironmentVariable(String key, String value) {
        try {
            Map<String, String> env = System.getenv();
            Class<?> cl = env.getClass();
            Field field = cl.getDeclaredField("m");
            field.setAccessible(true);
            Map<String, String> writableEnv = (Map<String, String>) field.get(env);
            writableEnv.put(key, value);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to set environment variable", e);
        }
    }

    /**
     * Getting environment variable value.
     *
     * @param key name of property
     * @return string value of variable or null, if it is not set
     * @since 0.1.0
     **/
    public static String getEnvironmentVariable(String key) {
        return System.getProperty(key, System.getenv(key));
    }

    /**
     * Prints environment variable value with.
     * {@link DevelopersNotificationLogger#infoEnvironmentVariable(String, String)}.
     *
     * @param name of property
     * @since 0.1.0
     **/
    public static void printToLogEnvironmentVariable(String name) {
        DevelopersNotificationLogger.infoEnvironmentVariable(name, getEnvironmentVariable(name));
    }

    /**
     * Translates a string into application/x-www-form-urlencoded format using UTF-8 encoding scheme.
     * This method uses the supplied encoding scheme to obtain the bytes for unsafe characters.
     *
     * @param param string to be translated
     * @return encoded string
     * @throws UnsupportedEncodingException - If the named encoding is not supported
     * @since 0.1.0
     **/
    public static String urlEncode(String param) throws UnsupportedEncodingException {
        return URLEncoder.encode(param, "UTF-8");
    }

    /**
     * Convert array of {@link StackTraceElement} to string.
     *
     * @param stackTrace - array of {@link StackTraceElement} which will be converted
     * @return convert array as string, each element of array start from new line
     * @since 0.1.0
     **/
    public static String arrayToString(StackTraceElement[] stackTrace) {
        StringBuilder result = new StringBuilder();
        for (StackTraceElement element : stackTrace) {
            result.append(element.toString()).append("\n");
        }
        return result.toString();
    }

    /**
     * Convert {@link Throwable} stack trace to byte[] array
     *
     * @param throwable - which will be converted
     * @return convert throwable as byte array
     * @since 0.3.0
     **/
    public static byte[] getThrowableStackTraceBytes(Throwable throwable) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(baos));
        return baos.toByteArray();
    }

    /**
     * Check is the given string is null or is the empty string.
     *
     * @param string to check
     * @return true if the given string is null or is the empty string.
     * @since 0.1.1
     * */
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    /**
     * Check does the necessary messenger configuration exist
     *
     * @param messenger to check
     * @return  <code>true</code> if configuration exist;
     *          <code>false</code> otherwise.
     * @since 0.3.0
     **/
    public static boolean checkTheNecessaryConfigurationExists(DevelopersNotificationMessenger messenger) {
        if (DevelopersNotification.config == null)
            return false;

        List<Messenger> messengers = DevelopersNotification.config.getMessenger();

        if (messengers == null || messengers.isEmpty())
            return false;

        if (messenger.equals(DevelopersNotificationMessenger.ALL_AVAILABLE)) {
            return checkTheNecessaryConfigurationExists(DevelopersNotificationMessenger.TELEGRAM) &
                    checkTheNecessaryConfigurationExists(DevelopersNotificationMessenger.SLACK);
        }

        for (Messenger m : messengers) {
            if (m.getName().equals(messenger)) {
                return !isNullOrEmpty(m.getChannel()) && !isNullOrEmpty(m.getToken());
            }
        }

        return false;
    }
}
