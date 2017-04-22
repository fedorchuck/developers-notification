import com.github.fedorchuck.developers_notification.DevelopersNotificationUtil;

import java.io.UnsupportedEncodingException;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 **/
public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {
        DevelopersNotificationUtil.setEnvironmentVariable("DN_SLACK_TOKEN", "");
        DevelopersNotificationUtil.setEnvironmentVariable("DN_SLACK_CHANNEL", "");
        DevelopersNotificationUtil.setEnvironmentVariable("DN_TELEGRAM_TOKEN", "");
        DevelopersNotificationUtil.setEnvironmentVariable("DN_TELEGRAM_CHANNEL", "");
        DevelopersNotificationUtil.setEnvironmentVariable("DN_MESSENGER", "ALL_AVAILABLE");

        DevelopersNotificationUtil.setEnvironmentVariable("DN_CONNECT_TIMEOUT", "7000");

        A a = new A();
        a.a();
    }
}
