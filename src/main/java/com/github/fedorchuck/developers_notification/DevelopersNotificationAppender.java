package com.github.fedorchuck.developers_notification;

import com.github.fedorchuck.developers_notification.antispam.SpamProtection;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Provides sending logging events messages by
 * <a href="https://github.com/fedorchuck/developers-notification">https://github.com/fedorchuck/developers-notification</a>
 * using chosen messenger
 *
 * <p>
 * <p> <b>Author</b>: <a href="https://vl-fedorchuck.firebaseapp.com/">Volodymyr Fedorchuk</a>. </p>
 * @author <a href="https://vl-fedorchuck.firebaseapp.com/">Volodymyr Fedorchuk</a>.
 * @since 0.3.0
 */
public class DevelopersNotificationAppender extends AppenderSkeleton {
    private List<Level> availableLevel = new ArrayList<Level>(0);

    /**
     * @see  <a href="https://i.stack.imgur.com/7o9Kk.png">https://i.stack.imgur.com/7o9Kk.png</a>
     **/
    @SuppressWarnings("unused")
    public void setLevel(String level) {
        Level inputLevel = Level.toLevel(level);
//TODO: check is config available
        switch (inputLevel.toInt()) {
            case Level.ALL_INT:
                availableLevel.add(Level.ALL);
            case Level.TRACE_INT:
                availableLevel.add(Level.TRACE);
            case Level.DEBUG_INT:
                availableLevel.add(Level.DEBUG);
            case Level.INFO_INT:
                availableLevel.add(Level.INFO);
            case Level.WARN_INT:
                availableLevel.add(Level.WARN);
            case Level.ERROR_INT:
                availableLevel.add(Level.ERROR);
            case Level.FATAL_INT:
                availableLevel.add(Level.FATAL);
            default://LEVEL OFF or UNKNOWN
                break;
        }

        DevelopersNotificationLogger.infoLoggerLevel(level, inputLevel);
    }

    @Override
    protected void append(LoggingEvent event) {
        if (availableLevel.contains(event.getLevel())) {
            new SpamProtection().sendLogEventIntoMessenger(DevelopersNotification.config.getProtectionFromSpam(), event);
        }
    }

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

}
