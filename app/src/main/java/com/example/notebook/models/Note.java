package com.example.notebook.models;

public class Note {
    private String id;
    private String title;
    private String note;
    private String notebook;
    private int timestamp;

    public Note(){

    }

    public Note(String id, String title, String note, String notebook, int timestamp ){
        this.id = id;
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

    public String  getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
