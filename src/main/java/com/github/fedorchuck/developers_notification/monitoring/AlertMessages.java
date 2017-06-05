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
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
class AlertMessages {
    static String getAlertRAMLimitMessage(long current, double currentPercent, Integer limit){
        return "Alert! Excess of the established limit of resources. " +
                "Used RAM " + current + " (" + currentPercent + "%). " +
                "Limit: " + limit + "%";
    }


    static String getAlertDiskLimitMessage(String diskName,
                                           long current,
                                           double currentPercent,
                                           Integer limit) {
        return "Alert! Excess of the established limit of resources. " +
                "Used Disk (" + diskName + ") " + current + " (" + currentPercent + "%). " +
                "Limit: " + limit + "%";
    }

    static String getAlertDiskRateMessage(String diskName,
                                          long rate,
                                          double ratePercent,
                                          Integer limit) {
        return "Alert! Excess of the established limit of resources. " +
                "Disk (" + diskName + ") consumption rate " + rate + " (" + ratePercent + "%). " +
                "Limit: " + limit + "%";
    }
}
