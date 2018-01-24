package com.github.fedorchuck.developers_notification.model;

import com.github.fedorchuck.developers_notification.integrations.Integration;
import org.apache.http.entity.mime.MultipartEntityBuilder;

/**
 * <p> <b>Author</b>: @author <a href="https://vl-fedorchuck.firebaseapp.com/">Volodymyr Fedorchuk</a>. </p>
 * @author <a href="https://vl-fedorchuck.firebaseapp.com/">Volodymyr Fedorchuk</a>.
 * @since 0.3.0
 */
public class Task {
    private Integration integration;
    private String jsonGeneratedMessages;
    private MultipartEntityBuilder multipartEntityBuilder;

    private String projectName;
    private String description;
    private Throwable throwable;

    public Task(Integration integration,
                String projectName,
                String description,
                Throwable throwable) {
        this.integration = integration;
        this.projectName = projectName;
        this.description = description;
        this.throwable = throwable;
    }

    public Integration getIntegration() {
        return integration;
    }

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

    public String getProjectName() {
        return projectName;
    }

    public String getDescription() {
        return description;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
