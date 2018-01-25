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

package com.github.fedorchuck.developers_notification.configuration;

import com.github.fedorchuck.developers_notification.DevelopersNotificationLogger;

import java.util.concurrent.TimeUnit;

/**
 * Part of configuration for this library.
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.2.0
 */
@SuppressWarnings("unused")
public class Monitoring {
    private Long period;
    private String unit;
    private Integer max_ram;
    private Integer max_disk;
    private Integer disk_consumption_rate;

    public Long getPeriod() {
        return period;
    }

    /**
     * Return TimeUnit
     *
     * @return TimeUnit
     * @throws IllegalArgumentException if unable to map string to {@link TimeUnit}
     * @since 0.2.0
     **/
    public TimeUnit getUnit() {
        if (TimeUnit.SECONDS.name().equals(unit.toUpperCase()))
            return TimeUnit.SECONDS;
        if (TimeUnit.MINUTES.name().equals(unit.toUpperCase()))
            return TimeUnit.MINUTES;
        if (TimeUnit.HOURS.name().equals(unit.toUpperCase()))
            return TimeUnit.HOURS;
        if (TimeUnit.DAYS.name().equals(unit.toUpperCase()))
            return TimeUnit.DAYS;

        DevelopersNotificationLogger.errorWrongConfig(unit, "UNIT", "Unit should be: SECONDS or MINUTES or HOURS or DAYS. ");
        throw new IllegalArgumentException("Bad argument. Current unit value: " + unit + " . ");
    }

    public Integer getMaxRam() {
        return max_ram;
    }

    public Integer getMaxDisk() {
        return max_disk;
    }

    public Integer getDiskConsumptionRate() {
        return disk_consumption_rate;
    }
}
