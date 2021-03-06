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

package com.github.fedorchuck.developers_notification.integrations.telegram;

import java.io.Serializable;

/**
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.2
 */
@SuppressWarnings("unused")
class Message implements Serializable {
    private String chat_id;
    private String parse_mode;
    private String text;

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public void setParse_mode(String parse_mode) {
        this.parse_mode = parse_mode;
    }

    public void setText(String text) {
        this.text = text;
    }
}
