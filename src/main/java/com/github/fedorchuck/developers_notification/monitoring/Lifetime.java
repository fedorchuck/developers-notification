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

package com.github.fedorchuck.developers_notification.monitoring;

import lombok.ToString;

import java.util.LinkedList;

/**
 * Stack with the lifetime of objects
 * Stack with the lifetime
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
@SuppressWarnings("WeakerAccess")
public class Lifetime<V> {
    private long timeToLive;
    private final LinkedList<V> list;

    @SuppressWarnings("unchecked")
    @ToString
    protected class CacheObject {
        public long lastAccessed = System.currentTimeMillis();
        public V value;

        CacheObject(V value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheObject that = (CacheObject) o;
            return value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }

    public Lifetime(long timeToLive, final long updateTimerInterval) {
        this.timeToLive = timeToLive * 1000;
        list = new LinkedList<V>();

        if (timeToLive > 0 && updateTimerInterval > 0) {

            Thread t = new Thread(
                new ThreadGroup("Developers notification"),
                new Runnable() {
                    public void run() {
                        while (true) {
                            try {Thread.sleep(updateTimerInterval * 1000);} catch (InterruptedException ignored) {}
                            cleanup();
                        }
                    }
                },
            "Stack with the lifetime of objects"
            );

            t.setDaemon(true);
            t.start();
        }
    }

    @SuppressWarnings("unchecked")
    public void put(V value) {
        synchronized (list) {
            list.add((V) new CacheObject(value));
        }
    }

    @SuppressWarnings("unchecked")
    public V getOldest() {
        synchronized (list) {
            if (list.size() == 0)
                return null;
            CacheObject c = (CacheObject) list.getFirst();
            return c.value;
        }
    }

    public int size() {
        synchronized (list) {
            return list.size();
        }
    }

    @SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
    public void cleanup() {
        long now = System.currentTimeMillis();
        synchronized (list) {
            if (list.size()==0)
                return;

            CacheObject c;
            for (V object : list){
                c = (CacheObject) object;
                if (c != null && (now > (timeToLive + c.lastAccessed))) {
                    list.remove(c);
                }
            }
        }
    }
}
