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

package com.github.fedorchuck.developers_notification.integrations.slack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.0
 */
@SuppressWarnings("unused")
class Payload implements Serializable {
    private String username;
    private String icon_url;
    private String text;
    private String channel;
    private List<Attachment> attachments = new ArrayList<Attachment>(0);

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
