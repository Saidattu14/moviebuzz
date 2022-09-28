package com.example.moviebuzz.data.model;


public class GenreMain{
    String Type;
    int Id;
    public GenreMain(String type, int id) {
        this.Type = type;
        this.Id = id;
    }

    public String getType() {
        return Type;
    }

    public int getId() {
        return Id;
    }
}