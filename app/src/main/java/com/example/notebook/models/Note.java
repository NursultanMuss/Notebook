package com.example.notebook.models;

public class Note {
    private String title;
    private String content;
    private String notebook;
    private String timestamp;

    public Note(){

    }

    public Note(String title, String content, String timestamp, String notebook ){
        this.notebook = notebook;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Note(String title, String content, String timestamp){
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }





    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotebook() {
        return notebook;
    }

    public void setNotebook(String notebook) {
        this.notebook = notebook;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
