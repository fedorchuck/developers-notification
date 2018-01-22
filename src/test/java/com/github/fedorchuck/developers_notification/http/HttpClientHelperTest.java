package com.github.fedorchuck.developers_notification.http;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
public class HttpClientHelperTest {

    @Test(expected = IllegalArgumentException.class)
    public void testGetConnectionIllegalArg() throws IOException {
        HttpClientHelper.getConnection(null, null);
    }

    @Test
    public void testGetConnection() {
        HttpURLConnection connection;
        try {
            connection = HttpClientHelper.getConnection("https://wsww.google.com/", null);
                Assert.assertEquals(null, connection.getContentType());
            connection = HttpClientHelper.getConnection("https://www.google.com/", null);
                Assert.assertEquals(true, connection.getContentType().contains("text/html"));
            HashMap<String, String> args = new HashMap<String, String>(1);
            args.put("q", "qwerty");
            connection = HttpClientHelper.getConnection("https://www.google.com/search", args);
                Assert.assertEquals("https://www.google.com/search?q=qwerty", connection.getURL().toString());
                Assert.assertEquals("https", connection.getURL().getProtocol());
            connection = HttpClientHelper.getConnection("http://www.google.com/search", args);
                Assert.assertEquals("http://www.google.com/search?q=qwerty", connection.getURL().toString());
                Assert.assertEquals("http", connection.getURL().getProtocol());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetResponse() {
        HttpURLConnection connection;
        try {
            connection = HttpClientHelper.getConnection("https://www.google.com/", null);
            HttpResponse response = HttpClientHelper.getResponse(connection);
            Assert.assertEquals(200, response.getStatusCode());
            Assert.assertEquals("OK", response.getResponseMessage());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetResponseWithError() {
        HttpURLConnection connection;
        try {
            connection = HttpClientHelper.getConnection("https://api.telegram.org/bot\" + telegramToken + \"/sendMessage", null);
            HttpResponse response = HttpClientHelper.getResponse(connection);
            Assert.assertEquals(404, response.getStatusCode());
            Assert.assertEquals("Not Found", response.getResponseMessage());
            Assert.assertEquals("application/json", response.getContentType());
            Assert.assertEquals("{\"ok\":false,\"error_code\":404,\"description\":\"Not Found\"}", response.getResponseContent());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

        try {
            connection = HttpClientHelper.getConnection("https://hooks.slack.com/services/123/abc/789xyz", null);
            HttpResponse response = HttpClientHelper.getResponse(connection);
            Assert.assertEquals(404, response.getStatusCode());
            Assert.assertEquals("Not Found", response.getResponseMessage());
            Assert.assertEquals("text/html", response.getContentType());
            Assert.assertEquals("No team", response.getResponseContent());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

}