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

package com.github.fedorchuck.developers_notification.helpers;

import java.util.LinkedList;

/**
 * Stack with the lifetime of objects
 * Stack with the lifetime
 *
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
@SuppressWarnings("WeakerAccess")
public class Lifetime<V> {
    private final LinkedList<CacheObject<V>> list = new LinkedList<CacheObject<V>>();
    private long timeToLive;

    public Lifetime(final long timeToLive, final long updateTimerInterval) {
        this.timeToLive = timeToLive * 1000;

        if (timeToLive > 0 && updateTimerInterval > 0) {
            Thread t = new Thread(
                    Constants.THREAD_GROUP,
                    new Runnable() {
                        public void run() {
                            while (true) {
                                try {
                                    Thread.sleep(updateTimerInterval * 1000);
                                } catch (InterruptedException ignored) {
                                }
                                cleanup();
                            }
                        }
                    },
                    Constants.THREAD_NAME_STACK
            );

            t.setDaemon(true);
            t.start();
        }
    }

    @SuppressWarnings("unchecked")
    public void put(V value) {
        synchronized (list) {
            if (!this.contains(value))
                list.add(new CacheObject(value));
        }
    }

    public V get(int index) {
        synchronized (list) {
            return list.get(index).value;
        }
    }

    public V getOldest() {
        synchronized (list) {
            if (list.size() == 0)
                return null;
            return list.getFirst().value;
        }
    }

    public int size() {
        synchronized (list) {
            return list.size();
        }
    }

    @SuppressWarnings({"SimplifiableIfStatement"})
    public boolean contains(V object) {
        synchronized (list) {
            if (list.size() == 0)
                return false;
            if (list.getLast().value.equals(object))
                return true;
            else
                return list.contains(new CacheObject<V>(object));
        }
    }

    @SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
    private void cleanup() {
        long now = System.currentTimeMillis();
        synchronized (list) {
            if (list.size() == 0)
                return;

            CacheObject<V> c;
            for (CacheObject<V> object : list) {
                c = object;
                if (c != null && (now > (timeToLive + c.lastAccessed))) {
                    list.remove(c);
                }
            }
        }
    }
}
