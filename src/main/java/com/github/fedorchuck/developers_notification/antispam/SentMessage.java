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

package com.github.fedorchuck.developers_notification.antispam;

/**
 * Model for mechanism witch avoiding spam.
 *
 * <p> <b>Author</b>: <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="http://vl-fedorchuck.rhcloud.com/">Volodymyr Fedorchuk</a>
 * @since 0.2.0
 */
public class SentMessage {
    private MessageTypes type;
    private String projectName;
    private String description;

    public SentMessage(MessageTypes type, String projectName, String description) {
        this.type = type;
        this.projectName = projectName;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SentMessage that = (SentMessage) o;

        return (
                type == that.type &&
                (projectName == null ? that.projectName == null : projectName.equals(that.projectName)) &&
                (description == null ? that.description == null : description.equals(that.description))
        );
    }

    @Override
    public int hashCode() {
        return ((type != null ? type.hashCode() : 0) +
                (projectName != null ? projectName.hashCode() : 0) +
                (description != null ? description.hashCode() : 0)
        );
    }
}
