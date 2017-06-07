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
 * Stack with the lifetime of objects. This stack automatic remove objects which are obsolete.
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.2.0
 */
@SuppressWarnings("WeakerAccess")
public class Lifetime<V> {
    private final LinkedList<CacheObject<V>> list = new LinkedList<CacheObject<V>>();
    private long timeToLive;

    /**
     * Constructor of stack. It receive data about time of living for added objects and time of
     * interval updating for find and remove obsolete objects.
     *
     * @param timeToLive time of living for added objects
     * @param updateTimerInterval time of interval updating for find and remove obsolete objects
     * @since 0.2.0
     **/
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

    /**
     * Appends the specified element to the end of this stack. Append JUST unique elements.
     *
     * @param value element to be appended to this stack
     * @since 0.2.0
     **/
    @SuppressWarnings("unchecked")
    public void put(V value) {
        synchronized (list) {
            if (!this.contains(value))
                list.add(new CacheObject(value));
        }
    }

    /**
     * Returns the element at the specified position in this stack.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this stack
     * @throws IndexOutOfBoundsException if the index is out of range <code>(index < 0 || index >= size())</code>
     * @since 0.2.0
     **/
    public V get(int index) {
        synchronized (list) {
            return list.get(index).value;
        }
    }

    /**
     * Returns the element at the specified position in this stack.
     *
     * @return the first element in this stack or <code>null</code> if this stack is empty
     * @since 0.2.0
     **/
    public V getOldest() {
        synchronized (list) {
            if (list.size() == 0)
                return null;
            return list.getFirst().value;
        }
    }

    /**
     * Returns the number of elements in this stack.
     *
     * @return the number of elements in this stack
     * @since 0.2.0
     **/
    public int size() {
        synchronized (list) {
            return list.size();
        }
    }

    /**
     * Returns true if this stack contains the specified element.
     *
     * @param object element whose presence in this stack is to be tested
     * @return true if this stack contains the specified element
     * @since 0.2.0
     **/
    @SuppressWarnings("SimplifiableIfStatement")
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

    /**
     * It is provide clean up this stack of objects from objects, which are obsolete.
     *
     * @since 0.2.0
     **/
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
