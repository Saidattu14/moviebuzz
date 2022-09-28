package com.example.moviebuzz.data.model;

public class MainData {
    int Id;
    String Name;

    public MainData(int id, String name) {
        this.Id = id;
        this.Name = name;
    }

    public String getName() {
        return this.Name;
    }
}
