package com.example.moviebuzz.data.model;

public class CitiesData {
    private String CityId;
    private String Name;
    private LocationData Location;
    private String State;

    public CitiesData() {
    }

    public CitiesData(String cityId, String name, LocationData location, String state) {
        CityId = cityId;
        Name = name;
        Location = location;
        State = state;
    }

    public String getCityId() {
        return CityId;
    }

    public String getName() {
        return Name;
    }

    public LocationData getLocation() {
        return Location;
    }

    public String getState() {
        return State;
    }
}
