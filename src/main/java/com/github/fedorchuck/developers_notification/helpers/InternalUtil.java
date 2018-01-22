package com.github.fedorchuck.developers_notification.helpers;

import com.github.fedorchuck.developers_notification.DevelopersNotification;
import com.github.fedorchuck.developers_notification.DevelopersNotificationMessenger;
import com.github.fedorchuck.developers_notification.configuration.Config;
import com.github.fedorchuck.developers_notification.configuration.Messenger;
import com.github.fedorchuck.developers_notification.integrations.Integration;
import com.github.fedorchuck.developers_notification.integrations.slack.SlackImpl;
import com.github.fedorchuck.developers_notification.integrations.telegram.TelegramImpl;
import com.github.fedorchuck.developers_notification.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://vl-fedorchuck.firebaseapp.com/">Volodymyr Fedorchuk</a>.
 */
public class InternalUtil {
    private static Config config = DevelopersNotification.config;

    public static List<Integration> getIntegrations() {
        List<Integration> integrations = new ArrayList<Integration>(0);

        for (Messenger messenger : config.getMessenger()) {
            switch (messenger.getName()){
                case SLACK:
                    integrations.add(new SlackImpl());
                    break;
                case TELEGRAM:
                    integrations.add(new TelegramImpl());
                    break;
                case ALL_AVAILABLE:
                    integrations.add(new SlackImpl());
                    integrations.add(new TelegramImpl());
                    break;
                default:
                    throw new IllegalArgumentException("Un known integration: " + messenger.getName());
            }
        }

        return integrations;
    }


    public static List<Integration> getIntegrations(final DevelopersNotificationMessenger messengerDestination) {
        List<Integration> integrations = new ArrayList<Integration>(0);

        switch (messengerDestination){
            case SLACK:
                integrations.add(new SlackImpl());
                break;
            case TELEGRAM:
                integrations.add(new TelegramImpl());
                break;
            case ALL_AVAILABLE:
                integrations.add(new SlackImpl());
                integrations.add(new TelegramImpl());
                break;
            default:
                throw new IllegalArgumentException("Un known integration: " + messengerDestination);
        }

        return integrations;
    }

    public static Task generateTask(final String projectName,
                             final String description,
                             final Throwable throwable,
                             final Integration integrations) {
        return integrations.generateMessage(projectName, description, throwable);
    }
}
