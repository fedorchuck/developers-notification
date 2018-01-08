package com.github.fedorchuck.developers_notification;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static com.github.fedorchuck.developers_notification.DevelopersNotificationMessenger.*;
import static com.github.fedorchuck.developers_notification.DevelopersNotificationUtil.checkTheNecessaryConfigurationExists;

/**
 * @author <a href="https://vl-fedorchuck.firebaseapp.com/">Volodymyr Fedorchuk</a>.
 */
public class DevelopersNotificationUtilTest {

    @Test
    public void testCheckTheNecessaryConfigurationExists() {
        String telegramToken = "token";
        String telegramChannel = "channel";
        String slackToken = "token";
        String slackChannel = "channel";

        String stringEmptyMessengerConfig = "{\"monitoring\":{\"period\":5,\"unit\":\"seconds\",\"max_ram\":90,\"max_disk\": 90,\"disk_consumption_rate\":2}}";
        Utils.setConfig(stringEmptyMessengerConfig);

        Assert.assertFalse(checkTheNecessaryConfigurationExists(TELEGRAM));
        Assert.assertFalse(checkTheNecessaryConfigurationExists(SLACK));
        Assert.assertFalse(checkTheNecessaryConfigurationExists(ALL_AVAILABLE));

        String stringTelegramConfig = "{\"messenger\":[{\"name\":\"TELEGRAM\",\"token\":\"" + telegramToken + "\",\"channel\":\"" + telegramChannel + "\"}]}";
        Utils.setConfig(stringTelegramConfig);
        Assert.assertTrue(checkTheNecessaryConfigurationExists(TELEGRAM));
        Assert.assertFalse(checkTheNecessaryConfigurationExists(SLACK));
        Assert.assertFalse(checkTheNecessaryConfigurationExists(ALL_AVAILABLE));

        String stringSlackConfig = "{\"messenger\":[{\"name\":\"SLACK\",\"token\":\"" + slackToken + "\",\"channel\":\"" + slackChannel + "\"}]}";
        Utils.setConfig(stringSlackConfig);
        Assert.assertFalse(checkTheNecessaryConfigurationExists(TELEGRAM));
        Assert.assertTrue(checkTheNecessaryConfigurationExists(SLACK));
        Assert.assertFalse(checkTheNecessaryConfigurationExists(ALL_AVAILABLE));

        String stringSlackAndTelegramConfig = "{\"messenger\":[{\"name\":\"TELEGRAM\",\"token\":\"" + telegramToken + "\",\"channel\":\"" + telegramChannel + "\"},{\"name\":\"SLACK\",\"token\":\"" + slackToken + "\",\"channel\":\"" + slackChannel + "\"}]}";
        Utils.setConfig(stringSlackAndTelegramConfig);
        Assert.assertTrue(checkTheNecessaryConfigurationExists(TELEGRAM));
        Assert.assertTrue(checkTheNecessaryConfigurationExists(SLACK));
        Assert.assertTrue(checkTheNecessaryConfigurationExists(ALL_AVAILABLE));
    }

    @After
    public void tearDown() {
        Utils.resetConfig();
    }
}