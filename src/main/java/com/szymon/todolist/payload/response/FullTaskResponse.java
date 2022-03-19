package com.szymon.todolist.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Data
@Builder
public class FullTaskResponse {
    private final Integer id;
    private final String title;
    private final String description;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone="Europe/Zagreb")
    private final Date createdOn;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone="Europe/Zagreb")
    private final Date finishedOn;

    private final boolean isActive;
}

