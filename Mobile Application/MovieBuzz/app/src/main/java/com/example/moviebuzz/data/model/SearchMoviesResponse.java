package com.example.moviebuzz.data.model;

import java.util.ArrayList;
import java.util.List;

public class SearchMoviesResponse {
    private String _index;
    private String _type;
    private String _id;
    private SearchSource _source;

    public SearchMoviesResponse() {
    }

    public SearchMoviesResponse(String _index, String _type, String _id, SearchSource _source) {
        this._index = _index;
        this._type = _type;
        this._id = _id;
        this._source = _source;
    }

    public String get_index() {
        return this._index;
    }

    public String get_type() {
        return this._type;
    }

    public String get_id() {
        return this._id;
    }

    public SearchSource get_source() {
        return this._source;
    }
}
