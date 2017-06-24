package com.github.fedorchuck.developers_notification.http;

import com.github.fedorchuck.developers_notification.DevelopersNotificationUtil;
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
        String slackToken = DevelopersNotificationUtil.getEnvironmentVariable("TRAVIS_TEST_SLACK_TOKEN");
        String slackChannel = DevelopersNotificationUtil.getEnvironmentVariable("TRAVIS_TEST_SLACK_CHANNEL");
        String telegramToken = DevelopersNotificationUtil.getEnvironmentVariable("TRAVIS_TEST_TELEGRAM_TOKEN");
        String telegramChannel = DevelopersNotificationUtil.getEnvironmentVariable("TRAVIS_TEST_TELEGRAM_CHANNEL");

        DevelopersNotificationUtil.setEnvironmentVariable("DN", "{\"messenger\":[{\"name\":\"SLACK\",\"token\":\"" + slackToken + "\",\"channel\":\"" + slackChannel + "\"},{\"name\":\"TELEGRAM\",\"token\":\"" + telegramToken + "\",\"channel\":\"" + telegramChannel + "\"}],\"show_whole_log_details\":false,\"protection_from_spam\": \"true\",\"project_name\": \"Where this library will be invoked\",\"connect_timeout\":5000,\"user_agent\":\"Mozilla/5.0\",\"monitoring\":{\"period\":5,\"unit\":\"seconds\",\"max_ram\":90,\"max_disk\": 90,\"disk_consumption_rate\":2}}");
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

}