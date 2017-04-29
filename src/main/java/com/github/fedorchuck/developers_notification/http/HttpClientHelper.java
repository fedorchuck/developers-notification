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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fedorchuck.developers_notification.DevelopersNotificationUtil;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * A HTTP client. It allows you to make requests to HTTP servers,
 * and a single client can make requests to any server.
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.0
 */
class HttpClientHelper {

    /**
     * Create and <b> open </b> an HTTP of HTTPS connection request to send to the server at the specified url,
     * specifying arguments to receive the response
     * @param stringUrl the url
     * @param arguments of url
     * @return  an HTTP connection as {@link HttpURLConnection}
     *
     * @since 0.1.0
     */
    static HttpURLConnection getConnection(String stringUrl, Map<String, String> arguments) throws IOException {
        if (DevelopersNotificationUtil.isNullOrEmpty(stringUrl)) {
            throw new IllegalArgumentException("URL can not be null or empty.");
        }
        String url = stringUrl;
        if ((arguments != null) && (!arguments.isEmpty())) {
            StringBuilder query = new StringBuilder();
            for (Map.Entry<String, String> entry : arguments.entrySet()) {
                query
                        .append(entry.getKey())
                        .append("=")
                        .append(DevelopersNotificationUtil.urlEncode(entry.getValue()))
                        .append("&");
            }
            String tmp = query.toString();
            String stringQuery = tmp.substring(0, tmp.length() - 1);
            url += "?" + stringQuery;
        }

        HttpURLConnection connection;
        if (stringUrl.startsWith("http")) {
            connection = (HttpURLConnection) new URL(url).openConnection();
        } else {
            connection = (HttpsURLConnection) new URL(url).openConnection();
        }

        return connection;
    }

    /**
     * Convert an {@link HttpURLConnection} to {@link HttpResponse}
     *
     * @param connection which will be converted
     * @return an HTTP response as {@link HttpResponse}
     *
     * @since 0.1.0
     */
    @SuppressWarnings("unchecked")
    static HttpResponse getResponse(HttpURLConnection connection) throws IOException {
        InputStream response = connection.getInputStream();
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode((connection).getResponseCode());
        httpResponse.setResponseMessage(connection.getResponseMessage());
        if ("application/json".equals(connection.getContentType()))
            httpResponse.setJsonResponse(new ObjectMapper().readValue(response, Map.class));

        response.close();
        return httpResponse;
    }
}
