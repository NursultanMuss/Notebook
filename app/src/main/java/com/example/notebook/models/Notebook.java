package com.example.notebook.models;

public class Notebook {
    private String user;
    private String notebookName;

    public Notebook(){

    }

    public Notebook( String _user, String _notebookName){
        this.user = _user;
        this.notebookName = _notebookName;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNotebookName() {
        return notebookName;
    }

    public void setNotebookName(String notebookName) {
        this.notebookName = notebookName;
    }
}
