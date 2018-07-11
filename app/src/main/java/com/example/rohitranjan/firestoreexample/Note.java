package com.example.rohitranjan.firestoreexample;

public class Note {

    String title,description;
    public Note()
    {

    }

    public Note(String title,String description)
    {
        this.title = title;
        this.description = description;
    }
    public String getTitle() {
        return title;
    }



    public String getDescription() {
        return description;
    }


}
