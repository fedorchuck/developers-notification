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

import com.github.fedorchuck.developers_notification.DevelopersNotificationUtil;
import lombok.*;

import java.util.List;

/**
 * <pre>
 {
    "messenger": [
        {
        "name": "",
        "token": "",
        "channel": ""
        }
    ],
    "monitoring": {
        "period": 10,
        "unit": "seconds",
        "max_ram": 90,
        "max_write": 2
    },
    "show_whole_log_details": true,
    "connect_timeout": 5000,
    "user_agent": "Mozilla/5.0"
 }
 * </pre>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>.
 */
@EqualsAndHashCode @ToString
@AllArgsConstructor @NoArgsConstructor
public class Config {
    @Getter
    private List<Messenger> messenger;
    @Getter
    private Monitoring monitoring;
    private Boolean show_whole_log_details;
    private Integer connect_timeout;
    private String user_agent;

    public Boolean getShowWholeLogDetails() {
        if (show_whole_log_details == null)
            return false;
        else
            return show_whole_log_details;
    }

    public Integer getConnectTimeout() {
        if (connect_timeout == null) {
            connect_timeout = 5000;
        }
        return connect_timeout;
    }

    public String getUserAgent() {
        if (DevelopersNotificationUtil.isNullOrEmpty(user_agent)) {
            user_agent = "Mozilla/5.0";
        }
        return user_agent;
    }

    public String getPublicToString() {
        StringBuilder res = new StringBuilder();
        res.append(" Messengers: ");
        for (Messenger m : messenger)
            res.append(m.getName()).append(", ");
        res.append("monitoring period: ").append(monitoring.getPeriod()).append(", ");
        res.append("monitoring unit: ").append(monitoring.getUnit()).append(", ");
        res.append("monitoring max_ram: ").append(monitoring.getMaxRam()).append(", ");
        res.append("monitoring max_write: ").append(monitoring.getMaxWrite()).append(", ");
        res.append("show_whole_log_details: ").append(this.getShowWholeLogDetails()).append(", ");
        res.append("connect_timeout: ").append(this.getConnectTimeout()).append(", ");
        res.append("user_agent: ").append(this.getUserAgent()).append(".");
        return res.toString();
    }
}
