package com.szymon.todolist.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TaskRequest {
    @NotBlank(message = "Title can't be empty!")
    @Size(max = 50)
    private String title;

    @NotBlank(message = "Description can't be empty!")
    @Size(max = 200)
    private String description;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}
