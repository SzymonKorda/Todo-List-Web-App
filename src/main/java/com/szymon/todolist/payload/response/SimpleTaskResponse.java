package com.szymon.todolist.payload.response;

public class SimpleTaskResponse {
    private final Integer id;
    private final String title;
    private final String description;

    private SimpleTaskResponse(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public static class Builder {
        private Integer id;
        private String title;
        private String description;

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

        public SimpleTaskResponse build() {
            return new SimpleTaskResponse(this);
        }
    }
}
