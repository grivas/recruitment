package com.alienvault.github;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class Issue {
    @JsonProperty
    private long id;
    @JsonProperty
    private String state;
    @JsonProperty
    private String title;
    @JsonProperty("repository")
    private String repository;
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    @JsonIgnore
    public LocalDate getCreatedDate() {
        return createdAt.toLocalDate();
    }
}
