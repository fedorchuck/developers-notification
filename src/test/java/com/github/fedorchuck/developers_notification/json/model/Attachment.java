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

package com.github.fedorchuck.developers_notification.json.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.1.0
 */
public class Attachment implements Serializable {
    private String fallback;
    private String title;
    private String color;
    private String author_name;
    private String[] mrkdwn_in;
    private String text;

    public String getFallback() {
        return fallback;
    }

    public void setFallback(String fallback) {
        this.fallback = fallback;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String[] getMrkdwn_in() {
        return mrkdwn_in;
    }

    public void setMrkdwn_in(String[] mrkdwn_in) {
        this.mrkdwn_in = mrkdwn_in;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return Objects.equals(fallback, that.fallback) &&
                Objects.equals(title, that.title) &&
                Objects.equals(color, that.color) &&
                Objects.equals(author_name, that.author_name) &&
                Arrays.equals(mrkdwn_in, that.mrkdwn_in) &&
                Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(fallback, title, color, author_name, text);
        result = 31 * result + Arrays.hashCode(mrkdwn_in);
        return result;
    }
}
