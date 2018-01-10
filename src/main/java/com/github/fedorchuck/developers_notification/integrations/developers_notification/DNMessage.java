package com.github.fedorchuck.developers_notification.integrations.developers_notification;

import org.apache.http.entity.mime.MultipartEntityBuilder;

/**
 * <p> <b>Author</b>: <a href="https://vl-fedorchuck.firebaseapp.com/">Volodymyr Fedorchuk</a> </p>
 * @author <a href="https://vl-fedorchuck.firebaseapp.com/">Volodymyr Fedorchuk</a>.
 * @since 0.3.0
 */
public class DNMessage {

    private String jsonGeneratedMessages;
    private MultipartEntityBuilder multipartEntityBuilder;

    public String getJsonGeneratedMessages() {
        return jsonGeneratedMessages;
    }

    public void setJsonGeneratedMessages(String jsonGeneratedMessages) {
        this.jsonGeneratedMessages = jsonGeneratedMessages;
    }

    public MultipartEntityBuilder getMultipartEntityBuilder() {
        return multipartEntityBuilder;
    }

    public void setMultipartEntityBuilder(MultipartEntityBuilder multipartEntityBuilder) {
        this.multipartEntityBuilder = multipartEntityBuilder;
    }
}
