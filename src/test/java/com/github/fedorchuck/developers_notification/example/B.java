/*
 * Copyright 2017 Volodymyr Fedorchuk <vl.fedorchuck@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.fedorchuck.developers_notification.example;

import com.github.fedorchuck.developers_notification.DevelopersNotification;
import com.github.fedorchuck.developers_notification.DevelopersNotificationMessenger;
import org.junit.Assert;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 */
class B {
    void b() {
        DevelopersNotification.printConfiguration();

        DevelopersNotification.monitoringStart();

        DevelopersNotification.send("test with signature: " +
                "'DevelopersNotification.send(description, throwable);'" +
                        "java: " + Runtime.class.getPackage().getImplementationVersion(),
                new Throwable("test")
        );
        DevelopersNotification.send(
                "integration test",
                "test with signature: " +
                "'DevelopersNotification.send(projectName, description, throwable);'" +
                        "java: " + Runtime.class.getPackage().getImplementationVersion(),
                new Throwable("test")
        );
        DevelopersNotification.send(DevelopersNotificationMessenger.TELEGRAM,
                "integration test",
                "test with signature: " +
                "'DevelopersNotification.send(DevelopersNotificationMessenger.TELEGRAM, projectName, description, throwable);'" +
                        "java: " + Runtime.class.getPackage().getImplementationVersion(),
                new Throwable("test")
        );
        DevelopersNotification.send(DevelopersNotificationMessenger.SLACK,
                "integration test",
                "test with signature: " +
                "'DevelopersNotification.send(DevelopersNotificationMessenger.SLACK, projectName, description, throwable);'" +
                        "java: " + Runtime.class.getPackage().getImplementationVersion(),
                new Throwable("test")
        );
        DevelopersNotification.send(DevelopersNotificationMessenger.ALL_AVAILABLE,
                "integration test",
                "test with signature: " +
                "'DevelopersNotification.send(DevelopersNotificationMessenger.ALL_AVAILABLE, projectName, description, throwable);'" +
                        "java: " + Runtime.class.getPackage().getImplementationVersion(),
                new Throwable("test")
        );
        DevelopersNotification.send(
                true,
                "integration test",
                "test with signature: " +
                "'DevelopersNotification.send(protectionFromSpam, projectName, description, throwable);'" +
                "java: " + Runtime.class.getPackage().getImplementationVersion(),
                new Throwable("test")
        );

        Assert.assertTrue("messages was sent without exceptions", true);

        try {Thread.sleep(10000);} catch (InterruptedException ignored) { }

        Assert.assertEquals(true, DevelopersNotification.isMonitoringStateAlive());
        DevelopersNotification.monitoringStart();
        DevelopersNotification.monitoringStop();
        Assert.assertEquals(false, DevelopersNotification.isMonitoringStateAlive());
    }
}
