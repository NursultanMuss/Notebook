package com.example.notebook.models;

public class Note {
    private String username;
    private String title;
    private String content;
    private String notebook;
    private String timestamp;

    public Note(){

    }

    public Note(String username,String title, String note, String notebook, String timestamp ){
        this.username = username;
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



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
