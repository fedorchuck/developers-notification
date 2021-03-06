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

/**
 * Model for monitoring
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.2.0
 */
class JVM {
    /**
     * Maximum ram memory for JVM (bytes)
     **/
    private long maxRamMemory;
    /**
     * Used ram memory for JVM (bytes)
     **/
    private long usedRamMemory;
    /**
     * Available ram memory for JVM (bytes)
     **/
    private long totalRamMemory;
    /**
     * Free ram memory for JVM (bytes)
     **/
    private long freeRamMemory;

    public long getMaxRamMemory() {
        return maxRamMemory;
    }

    public void setMaxRamMemory(long maxRamMemory) {
        this.maxRamMemory = maxRamMemory;
    }

    public long getUsedRamMemory() {
        return usedRamMemory;
    }

    public void setUsedRamMemory(long usedRamMemory) {
        this.usedRamMemory = usedRamMemory;
    }

    public long getTotalRamMemory() {
        return totalRamMemory;
    }

    public void setTotalRamMemory(long totalRamMemory) {
        this.totalRamMemory = totalRamMemory;
    }

    public long getFreeRamMemory() {
        return freeRamMemory;
    }

    public void setFreeRamMemory(long freeRamMemory) {
        this.freeRamMemory = freeRamMemory;
    }
}
