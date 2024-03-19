package com.example.moviebuzz.data.model;

import java.util.List;

public class CountryData {
    int Id;
    String Name;
    List<CitiesData> Cities;

    public CountryData(int id, String name, List<CitiesData> cities) {
        Id = id;
        Name = name;
        Cities = cities;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public List<CitiesData> getCities() {
        return Cities;
    }
}
