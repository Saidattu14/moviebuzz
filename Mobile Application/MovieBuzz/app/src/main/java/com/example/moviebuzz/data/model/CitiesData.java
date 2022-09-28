package com.example.moviebuzz.data.model;

public class CitiesData {
    private String CityId;
    private String Name;
    private LocationData Location;

    public CitiesData() {
    }

    public CitiesData(String cityId, String name, LocationData location) {
        this.CityId = cityId;
        this.Name = name;
        this.Location = location;
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
}
