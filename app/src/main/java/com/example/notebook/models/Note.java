package com.example.notebook.models;

public class Note {
    private String username;
    private String title;
    private String note;
    private String notebook;
    private int timestamp;

    public Note(){

    }

    public Note(String username,String title, String note, String notebook, int timestamp ){
        this.username = username;
        this.notebook = notebook;
        this.title = title;
        this.note = note;
        this.timestamp = timestamp;
    }

    public Note(String title, String note, int timestamp){
        this.title = title;
        this.note = note;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNotebook() {
        return notebook;
    }

    public void setNotebook(String notebook) {
        this.notebook = notebook;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
