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

package com.github.fedorchuck.developers_notification.antispam;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
@SuppressWarnings("UnusedReturnValue")
public class FrequencyOfSending {
    private static boolean diskLimitAllow = true;
    private static boolean diskRateAllow = true;
    private static boolean ramLimitAllow = true;
    private static final List<UserMessage> sent = new ArrayList<UserMessage>(0);

    public static boolean canSendMessage(final MessageTypes type) {
        switch (type){
            case RAM_LIMIT:
                return ramLimitAllow;
            case DISK_LIMIT:
                return diskLimitAllow;
            case DISK_CONSUMPTION_RATE:
                return diskRateAllow;
            default:
                throw new IllegalArgumentException("Type " + type + " can not use this method.");
        }
    }

    public static boolean canSendMessage(final MessageTypes type, final String projectName, final String description) {
        switch (type){
            case RAM_LIMIT:
                return ramLimitAllow;
            case DISK_LIMIT:
                return diskLimitAllow;
            case DISK_CONSUMPTION_RATE:
                return diskRateAllow;
            case USERS_MESSAGE:
                return !sent.contains(new UserMessage(projectName, description));
            default:
                throw new IllegalArgumentException("Type " + type + " can not use this method.");
        }
    }

    public static void messageSent(final MessageTypes type, final String projectName, final String description) {
        switch (type){
            case USERS_MESSAGE:
                messageSent(type, projectName, description, TimeUnit.MINUTES.toSeconds(10));
                break;
            default:
                throw new IllegalArgumentException("Type " + type + " can not use this method.");
        }
    }

    public static boolean messageSent(final MessageTypes type){
        switch (type){
            case RAM_LIMIT:
            case DISK_LIMIT:
            case DISK_CONSUMPTION_RATE:
                return messageSent(type, TimeUnit.MINUTES.toSeconds(10));
            default:
                throw new IllegalArgumentException("Type " + type + " can not use this method.");
        }
    }

    public static boolean messageSent(final MessageTypes type, final long waitTime){
        switch (type) {
            case RAM_LIMIT:
                ramLimitAllow = false;
                break;
            case DISK_LIMIT:
                diskLimitAllow = false;
                break;
            case DISK_CONSUMPTION_RATE:
                diskRateAllow = false;
                break;
            default:
                throw new IllegalArgumentException("Type " + type + " can not use this method.");
        }
        Thread t = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {Thread.sleep(waitTime * 1000);} catch (InterruptedException ignored) {}
                    switch (type) {
                        case RAM_LIMIT:
                            ramLimitAllow = true;
                            break;
                        case DISK_LIMIT:
                            diskLimitAllow = true;
                            break;
                        case DISK_CONSUMPTION_RATE:
                            diskRateAllow = true;
                            break;
                    }
                }
            }
        });
        t.setDaemon(true);
        t.start();
        return true;
    }

    public static boolean messageSent(final MessageTypes type,
                                      final String projectName,
                                      final String description,
                                      final long waitTime) {
        if (type!=MessageTypes.USERS_MESSAGE)
            throw new IllegalArgumentException("Type " + type + " can not use this method.");

        if (sent.contains(new UserMessage(projectName, description)))
            return false;

        Thread t = new Thread(new Runnable() {
            public void run() {
                //noinspection InfiniteLoopStatement
                while (true) {
                    sent.add(new UserMessage(projectName, description));
                    try {Thread.sleep(waitTime * 1000);} catch (InterruptedException ignored) {}
                    cleanup();
                }
            }
        });
        t.setDaemon(true);
        t.start();
        return true;
    }

    private static void cleanup() {
        synchronized (sent) {
            for (UserMessage message : sent){
                if (message.getLastAccessed() >= System.currentTimeMillis())
                    sent.remove(message);
            }
        }
        Thread.yield();
    }
}
