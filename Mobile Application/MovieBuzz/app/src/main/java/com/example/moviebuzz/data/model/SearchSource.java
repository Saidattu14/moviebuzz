package com.example.moviebuzz.data.model;

import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class LocationData{
    double lat;
    double lon;

    public LocationData() {
    }

    public LocationData(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}



public class SearchSource {

    private String Title;
    private String Poster;
    private String Released;
    private String imdbID;
    private String Plot;
    private float imdbRating;
    private int imdbVotes;
    private String Runtime;
    private String Language;
    private List<GenreMain> Genre;
    private List<CitiesData> Cities;
    private List<MainData> Writer;
    private List<MainData> Actors;
    private List<MainData> Director;
    private List<MainData> Country;

    public SearchSource() {
    }

    public String getTitle() {
        return this.Title;
    }

    public String getPoster() {
        return this.Poster;
    }

    public List<GenreMain> getGenre() {
        return this.Genre;
    }

    public List<CitiesData> getCities() {
        return this.Cities;
    }

    public List<MainData> getWriter() {
        return this.Writer;
    }

    public List<MainData> getActors() {
        return this.Actors;
    }

    public List<MainData> getDirector() {
        return this.Director;
    }

    public List<MainData> getCountry() {
        return this.Country;
    }

    public SearchSource(String title, String poster, String released, String imdbID, String plot, float imdbRating, int imdbVotes, String runtime, String language, List<GenreMain> genre, List<CitiesData> cities, List<MainData> writer, List<MainData> actors, List<MainData> director, List<MainData> country) {
         this.Title = title;
         this.Poster = poster;
         this.Released = released;
         this.imdbID = imdbID;
         this.Plot = plot;
         this.imdbRating = imdbRating;
         this.imdbVotes = imdbVotes;
         this.Runtime = runtime;
         this.Language = language;
         this.Genre = genre;
         this.Cities = cities;
         this.Writer = writer;
         this.Actors = actors;
         this.Director = director;
         this.Country = country;
    }

    public String getReleased() {
        return this.Released;
    }

    public String getImdbID() {
        return this.imdbID;
    }

    public String getPlot() {
        return this.Plot;
    }

    public float getImdbRating() {
        return this.imdbRating;
    }

    public int getImdbVotes() {
        return this.imdbVotes;
    }

    public String getRuntime() {
        return this.Runtime;
    }

    public String getLanguage() {
        return this.Language;
    }

}
