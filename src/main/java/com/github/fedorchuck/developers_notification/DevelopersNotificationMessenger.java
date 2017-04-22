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

package com.github.fedorchuck.developers_notification;

/**
 * Enum of available destinations where the message will be sent.
 *
 * <p> <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.0
 */
public enum DevelopersNotificationMessenger {
    /**
     * Provide sending messages via Slack messenger. Required environment configuration:
     * <ul>
     * <li><b>DN_SLACK_TOKEN</b> - access key; required if you use Slack messages</li>
     * <li><b>DN_SLACK_CHANNEL</b> - destination chat; required if you use Slack messages</li>
     * </ul>
     * @since 0.1.0
     **/
    SLACK,
    /**
     * Provide sending messages via Telegram messenger. Required environment configuration:
     * <ul>
     * <li><b>DN_TELEGRAM_TOKEN</b> - access key; required if you use Telegram messages</li>
     * <li><b>DN_TELEGRAM_CHANNEL</b> - destination chat; required if you use Telegram messages</li>
     * </ul>
     * @since 0.1.0
     **/
    TELEGRAM,
    /**
     * Provide sending messages via Slack and Telegram messenger. Required environment configuration:
     * <ul>
     * <li><b>DN_SLACK_TOKEN</b> - access key; required if you use Slack messages</li>
     * <li><b>DN_SLACK_CHANNEL</b> - destination chat; required if you use Slack messages</li>
     * <li><b>DN_TELEGRAM_TOKEN</b> - access key; required if you use Telegram messages</li>
     * <li><b>DN_TELEGRAM_CHANNEL</b> - destination chat; required if you use Telegram messages</li>
     * </ul>
     * @since 0.1.0
     **/
    ALL_AVAILABLE,

}
