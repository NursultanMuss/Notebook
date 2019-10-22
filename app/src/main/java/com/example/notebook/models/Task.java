package com.example.notebook.models;

public class Task {
    private String taskText;
    private int priority;
    private String timeStamp;

    public Task() {

    }

    public Task(String taskText, int priority, String timeStamp) {
        this.taskText = taskText;
        this.priority = priority;
        this.timeStamp = timeStamp;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
