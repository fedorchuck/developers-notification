package com.github.fedorchuck.developers_notification.helpers;

import com.github.fedorchuck.developers_notification.DevelopersNotification;
import com.github.fedorchuck.developers_notification.DevelopersNotificationMessenger;
import com.github.fedorchuck.developers_notification.configuration.Config;
import com.github.fedorchuck.developers_notification.configuration.Messenger;
import com.github.fedorchuck.developers_notification.integrations.Integration;
import com.github.fedorchuck.developers_notification.integrations.slack.SlackImpl;
import com.github.fedorchuck.developers_notification.integrations.telegram.TelegramImpl;
import com.github.fedorchuck.developers_notification.model.Task;
import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains method for using just by this library
 *
 * <p> <b>Author</b>: <a href="https://vl-fedorchuck.firebaseapp.com/">Volodymyr Fedorchuk</a>. </p>
 * @author <a href="https://vl-fedorchuck.firebaseapp.com/">Volodymyr Fedorchuk</a>.
 * @since 0.2.0
 */
public class InternalUtil {
    private static Config config = DevelopersNotification.getConfiguration();

    /**
     * Return all {@link Integration} from {@link Config} as {@link List}
     *
     * @return available integrations
     * @throws IllegalArgumentException if integration is un known
     * @since 0.3.0
     **/
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

    /**
     * Return all {@link Integration} from input param as {@link List}
     *
     * @param messengerDestination which needed {@link Integration}
     * @return available integrations
     * @throws IllegalArgumentException if integration is un known
     * @since 0.3.0
     **/
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

    /**
     * Generate {@link Task} from input data
     *
     * @param projectName where was method called
     * @param description about situation
     * @param throwable which happened. Can be <code>null</code>
     * @param integrations where this task should will be complete
     * @return available integrations
     * @since 0.3.0
     **/
    public static Task generateTask(final String projectName,
                             final String description,
                             final Throwable throwable,
                             final Integration integrations) {
        return integrations.generateMessage(projectName, description, throwable);
    }

    /**
     * Generate {@link Task} from input data
     *
     * @param projectName where was method called
     * @param event form logger
     * @param integrations where this task should will be complete
     * @return available integrations
     * @since 0.3.0
     **/
    public static Task generateTaskFromLoggingEvent(final String projectName,
                                                    final LoggingEvent event,
                                                    final Integration integrations) {
        return integrations.generateMessageFromLoggingEvent(projectName, event);
    }
}
