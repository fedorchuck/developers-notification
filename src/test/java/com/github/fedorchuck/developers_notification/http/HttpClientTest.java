package com.github.fedorchuck.developers_notification.http;

import com.github.fedorchuck.developers_notification.Utils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
public class HttpClientTest {
    @Before
    public void setUp() {
        String stringHttpConfig = "{\"connect_timeout\":5000,\"user_agent\":\"Mozilla/5.0\"}";
        Utils.setConfig(stringHttpConfig);
    }

    @Test
    public void testGet() {
        HttpClient client = new HttpClient();
        HttpResponse response;
        try {
            HashMap<String, String> args = new HashMap<String, String>(1);
            args.put("q", "qwerty");

            response = client.get("https://www.google.com/search", args);
            Assert.assertEquals(200, response.getStatusCode());
            Assert.assertEquals("OK", response.getResponseMessage());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testPost() {
        HttpClient client = new HttpClient();
        HttpResponse response;
        try {
            response = client.post("https://chatapi.viber.com/pa/send_message", "{ \"tracking_data\": \"tracking data\", \"type\": \"picture\", \"text\": \"Photo description\" }");
            Assert.assertEquals(200, response.getStatusCode());
            Assert.assertEquals("OK", response.getResponseMessage());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @After
    public void tearDown() {
        Utils.resetConfig();
    }

}