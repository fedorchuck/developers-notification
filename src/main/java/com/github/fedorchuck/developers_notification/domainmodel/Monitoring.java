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

package com.github.fedorchuck.developers_notification.domainmodel;

import lombok.*;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
@EqualsAndHashCode @ToString
@AllArgsConstructor @NoArgsConstructor
public class Monitoring {
    @Getter
    private Long period;
    private String unit;
    private Integer max_ram;
    private Integer max_write;

    public TimeUnit getUnit() {
        if (TimeUnit.SECONDS.name().equals(unit.toUpperCase()))
            return TimeUnit.SECONDS;
        if (TimeUnit.MINUTES.name().equals(unit.toUpperCase()))
            return TimeUnit.MINUTES;
        if (TimeUnit.HOURS.name().equals(unit.toUpperCase()))
            return TimeUnit.HOURS;
        if (TimeUnit.DAYS.name().equals(unit.toUpperCase()))
            return TimeUnit.DAYS;

        throw new IllegalArgumentException("Bad argument. Current unit value: " + unit + " . Unit should be: SECONDS or MINUTES or HOURS or DAYS. ");
    }

    public Integer getMaxRam() {
        return max_ram;
    }

    public Integer getMaxWrite() {
        return max_write;
    }
}
