import com.github.fedorchuck.developers_notification.DevelopersNotification;
import com.github.fedorchuck.developers_notification.DevelopersNotificationMessenger;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 */
class B {
    void b() throws InterruptedException {
        DevelopersNotification.printConfiguration();
        DevelopersNotification.send(DevelopersNotificationMessenger.TELEGRAM,
                "test", "with full method signature", new IllegalAccessException("abcd"));
        DevelopersNotification.send(DevelopersNotificationMessenger.SLACK,
                "test", "with full method signature", new IllegalStateException("abcd"));

        DevelopersNotification.send(
                "test", "with out full method signature", new IllegalAccessException());
    }
}
