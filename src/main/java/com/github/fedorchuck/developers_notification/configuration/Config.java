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

import com.github.fedorchuck.developers_notification.DevelopersNotificationUtil;

import java.util.List;

/**
 * Configuration class for this library.
 * This class is mapped with JSON:
 * <pre><code>
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
        "max_disk": 90,
        "disk_consumption_rate": 2
    },
    "project_name": "Where this library will be invoked",
    "protection_from_spam": "true",
    "show_whole_log_details": true,
    "connect_timeout": 5000,
    "user_agent": "Mozilla/5.0"
 }
 * </code></pre>
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.2.0
 */
@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
public class Config {
    private List<Messenger> messenger;
    private Monitoring monitoring;
    private String project_name;
    private Boolean protection_from_spam;
    private Boolean show_whole_log_details;
    private Integer connect_timeout;
    private String user_agent;

    public List<Messenger> getMessenger() {
        return messenger;
    }

    public Monitoring getMonitoring() {
        return monitoring;
    }

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

    public String getProjectName() {
        return project_name;
    }

    public Boolean getProtectionFromSpam() {
        if (protection_from_spam==null)
            return false;
        else
            return protection_from_spam;
    }

    /**
     * It create string with "public" fields
     *
     * @return string with "public" fields
     * @since 0.2.0
     **/
    public String getPublicToString() {
        StringBuilder res = new StringBuilder();
        res.append("Messengers: ");
        for (Messenger m : messenger)
            res.append(m.getName()).append(", ");
        res.append("project name: ").append(this.getProjectName()).append(", ");
        res.append("protection from spam: ").append(this.getProtectionFromSpam()).append(", ");
        res.append("monitoring period: ").append(monitoring.getPeriod()).append(", ");
        res.append("monitoring unit: ").append(monitoring.getUnit()).append(", ");
        res.append("monitoring max_ram: ").append(monitoring.getMaxRam()).append(", ");
        res.append("monitoring disk_consumption_rate: ").append(monitoring.getDiskConsumptionRate()).append(", ");
        res.append("show_whole_log_details: ").append(this.getShowWholeLogDetails()).append(", ");
        res.append("connect_timeout: ").append(this.getConnectTimeout()).append(", ");
        res.append("user_agent: ").append(this.getUserAgent()).append(".");
        return res.toString();
    }

    /**
     * It create string with "sensitive data"
     *
     * @return string with "sensitive data"
     * @since 0.3.0
     **/
    public String getPrivateToString() {
        StringBuilder res = new StringBuilder();
        res.append("Messengers: [");
        for (Messenger m : messenger) {
            res.append("{ name: ");
            res.append(m.getName());
            res.append(", ");
            res.append("token: ");
            res.append(m.getToken());
            res.append(", ");
            res.append("chanel: ");
            res.append(m.getChannel());
            res.append(" }");
        }
        res.append("], ");
        res.append("project name: ").append(this.getProjectName()).append(", ");
        res.append("protection from spam: ").append(this.getProtectionFromSpam()).append(", ");
        res.append("monitoring period: ").append(monitoring.getPeriod()).append(", ");
        res.append("monitoring unit: ").append(monitoring.getUnit()).append(", ");
        res.append("monitoring max_ram: ").append(monitoring.getMaxRam()).append(", ");
        res.append("monitoring disk_consumption_rate: ").append(monitoring.getDiskConsumptionRate()).append(", ");
        res.append("show_whole_log_details: ").append(this.getShowWholeLogDetails()).append(", ");
        res.append("connect_timeout: ").append(this.getConnectTimeout()).append(", ");
        res.append("user_agent: ").append(this.getUserAgent()).append(".");
        return res.toString();
    }
}
