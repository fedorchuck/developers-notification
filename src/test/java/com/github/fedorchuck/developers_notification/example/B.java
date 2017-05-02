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

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 */
class B {
    void b() {
        DevelopersNotification.printConfiguration();
        DevelopersNotification.send(DevelopersNotificationMessenger.TELEGRAM, "developers_notification",
                "test with full method signature", new Throwable("abcd"));
        DevelopersNotification.send(DevelopersNotificationMessenger.SLACK, "developers_notification",
                "test with full method signature", new Throwable("abcd"));
        DevelopersNotification.send("developers_notification",
                "test without full method signature", new Throwable());
    }
}
