package com.szymon.todolist.payload;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class FullTaskResponse {
    private final Integer id;
    private final String title;
    private final String description;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone="Europe/Zagreb")
    private final Date createdOn;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone="Europe/Zagreb")
    private final Date finishedOn;

    private final boolean isActive;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public Date getFinishedOn() {
        return finishedOn;
    }

    public boolean isActive() {
        return isActive;
    }

    private FullTaskResponse(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.createdOn = builder.createdOn;
        this.finishedOn = builder.finishedOn;
        this.isActive = builder.isActive;
    }

    public static class Builder {
        private Integer id;
        private String title;
        private String description;
        private Date createdOn;
        private Date finishedOn;
        private boolean isActive;

        public Builder(Integer id) {
            this.id = id;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withCreatedOn(Date createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public Builder withFinishedOn(Date finishedOn) {
            this.finishedOn = finishedOn;
            return this;
        }

        public Builder withIsActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public FullTaskResponse build() {
            return new FullTaskResponse(this);
        }

    }
}
