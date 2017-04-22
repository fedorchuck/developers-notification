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

package com.github.fedorchuck.developers_notification.http;

import com.github.fedorchuck.developers_notification.DevelopersNotificationLogger;
import com.github.fedorchuck.developers_notification.DevelopersNotificationUtil;
import com.google.common.base.Strings;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * A HTTP client. It allows you to make requests to HTTP servers,
 * and a single client can make requests to any server.
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.0
 */
public class HttpClient {
    private String USER_AGENT;
    private Integer CONNECT_TIMEOUT;

    public HttpClient() {
        USER_AGENT = DevelopersNotificationUtil.getEnvironmentVariable("DN_USER_AGENT");
        if (Strings.isNullOrEmpty(USER_AGENT)) {
            USER_AGENT = "Mozilla/5.0";
        }
        CONNECT_TIMEOUT = Integer.getInteger(
                DevelopersNotificationUtil.getEnvironmentVariable("DN_CONNECT_TIMEOUT"));
        if (CONNECT_TIMEOUT == null) {
            CONNECT_TIMEOUT = 5000;
        }
    }

    /**
     * Pint environment variable value with
     * {@link DevelopersNotificationLogger#infoEnvironmentVariable(String, String)}.
     *
     * @since 0.1.0
     **/
    public static void printConfiguration() {
        DevelopersNotificationUtil.printToLogEnvironmentVariable("DN_USER_AGENT");
        DevelopersNotificationUtil.printToLogEnvironmentVariable("DN_CONNECT_TIMEOUT");
    }

    /**
     * Create an HTTP GET request to send to the server at the specified url,
     * specifying a response handler to receive the response
     * @param stringUrl the url
     * @param arguments of url
     * @return  an HTTP response as {@link HttpResponse}
     *
     * @since 0.1.0
     */
    public HttpResponse get(String stringUrl, Map<String, String> arguments) throws IOException {
        HttpURLConnection connection = HttpClientHelper.getConnection(stringUrl, arguments);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setConnectTimeout(CONNECT_TIMEOUT);

        HttpResponse httpResponse = HttpClientHelper.getResponse(connection);
        connection.disconnect();
        return httpResponse;
    }

    /**
     * Create an HTTP POST request to send to the server at the specified url,
     * specifying json to receive the response
     * @param stringUrl the url
     * @param json for request
     * @return  an HTTP response as {@link HttpResponse}
     *
     * @since 0.1.0
     */
    public HttpResponse post(String stringUrl, String json) throws IOException {
        HttpURLConnection connection = HttpClientHelper.getConnection(stringUrl, null);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setRequestProperty("cache-control", "no-cache");
        connection.setDoOutput(true);

        OutputStream wr = connection.getOutputStream();
        wr.write(json.getBytes("UTF-8"));
        wr.flush();
        wr.close();

        HttpResponse httpResponse = HttpClientHelper.getResponse(connection);
        connection.disconnect();
        return httpResponse;
    }
}
