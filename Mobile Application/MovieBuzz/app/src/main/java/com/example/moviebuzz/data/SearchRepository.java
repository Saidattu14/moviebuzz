package com.example.moviebuzz.data;


import com.example.moviebuzz.data.model.SearchMoviesResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SearchRepository {

    @GET("/api/v1/search_movies/{country_name}/{lat}/{lon}")
    Call<List<SearchMoviesResponse>> searchMoviesByLocation(
            @Header("authorization") String token,
            @Path(value="country_name", encoded=true) String country_name,
                                        @Path(value="lat", encoded=true) double lat,
                                        @Path(value="lon", encoded=true) double lon,  @Query("From") int from, @Query("Size") int size);

    @GET("/api/v1/search_movies1/{country_name}/{city_name}")
    Call<List<SearchMoviesResponse>> searchMoviesByCountryCity(
            @Header("authorization") String token,
            @Path(value="country_name", encoded=true) String country_name,
            @Path(value="city_name", encoded=true) String city_name,  @Query("Size") int size);


    @GET("/api/v1/search_movies/country")
    Call<List<SearchMoviesResponse>> searchMoviesByCountry(
            @Header("authorization") String token,
            @Query("name") String country_name, @Query("From") int from,  @Query("Size") int size);

    @GET("/api/v1/search_movies_genre/")
    Call<List<SearchMoviesResponse>> searchMoviesByGenre(
            @Header("authorization") String token,
            @Query("type") String type,
            @Query("From") int from,@Query("Size") int size);

    @GET("/api/v1/search_top_rated_movies/")
    Call<List<SearchMoviesResponse>> searchMoviesByRating(   @Header("authorization") String token,
                                                             @Query("From") int from,
                                                             @Query("Size") int size);

    @GET("/api/v1/search_most_popular_movies/")
    Call<List<SearchMoviesResponse>> searchMoviesByPopularity(
            @Header("authorization") String token,
            @Query("From") int from,    @Query("Size") int size
    );

    @GET("/api/v1/search_movies/wildcard/{name}")
    Call<List<SearchMoviesResponse>> searchActorsWriterDirectorsOtherMovies(
            @Header("authorization") String token,
            @Path(value="name", encoded=true) String name,  @Query("From") int from,    @Query("Size") int size
    );

    @GET("/api/v1/search_movies/wildcardonAll/{name}")
    Call<List<SearchMoviesResponse>> searchOnAllData(
            @Header("authorization") String token,
            @Path(value="name", encoded=true) String name,  @Query("From") int from,@Query("Size") int size
    );
}
