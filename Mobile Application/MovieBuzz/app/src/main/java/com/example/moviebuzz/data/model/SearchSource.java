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
    private List<MainData> Writer;
    private List<MainData> Actors;
    private List<MainData> Director;
    private List<CountryData> Country;
    private List<MovieReviewsModel> Reviews;

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

    public List<MainData> getWriter() {
        return this.Writer;
    }

    public List<MainData> getActors() {
        return this.Actors;
    }

    public List<MainData> getDirector() {
        return this.Director;
    }

    public List<CountryData> getCountry() {
        return this.Country;
    }

    public SearchSource(String title, String poster, String released,
                        String imdbID, String plot, float imdbRating, int imdbVotes,
                        String runtime, String language,
                        List<GenreMain> genre,
                        List<MainData> writer, List<MainData> actors, List<MainData> director,
                        List<CountryData> country,List<MovieReviewsModel> reviews) {
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
         this.Writer = writer;
         this.Actors = actors;
         this.Director = director;
         this.Country = country;
         this.Reviews = reviews;
    }

    public List<MovieReviewsModel> getReviews() {
        return Reviews;
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
