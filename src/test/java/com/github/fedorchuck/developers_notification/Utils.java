package com.github.fedorchuck.developers_notification;

import com.github.fedorchuck.developers_notification.configuration.Config;
import com.github.fedorchuck.developers_notification.json.Json;
import org.junit.Assert;

import java.lang.reflect.Field;

/**
 * @author <a href="https://vl-fedorchuck.firebaseapp.com/">Volodymyr Fedorchuk</a>.
 */
public class Utils {

    public static void setConfig(String jsonConfig) {
        Config config = Json.decodeValue(jsonConfig, Config.class);
        updateConfig(config);
    }

    public static void resetConfig() {
        updateConfig(null);
    }

    private static void updateConfig(Config config) {
        DevelopersNotification dn = new DevelopersNotification();

        Field field;
        try {
            field = dn.getClass().getDeclaredField("config");
            field.setAccessible(true);
            field.set(dn, config);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Assert.fail(e.getMessage());
        }
    }
}
