package com.szymon.todolist.payload;

public class TaskCountResponse {
    private long activeCount;
    private long finishedCount;

    public TaskCountResponse() {
        this.activeCount = activeCount;
        this.finishedCount = finishedCount;
    }

    public long getActiveCount() {
        return activeCount;
    }

    public long getFinishedCount() {
        return finishedCount;
    }

    public void setActiveCount(long activeCount) {
        this.activeCount = activeCount;
    }

    public void setFinishedCount(long finishedCount) {
        this.finishedCount = finishedCount;
    }
}
