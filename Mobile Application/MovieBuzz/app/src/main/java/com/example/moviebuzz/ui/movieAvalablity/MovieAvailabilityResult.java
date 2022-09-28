package com.example.moviebuzz.ui.movieAvalablity;

import com.example.moviebuzz.data.model.TheatersAndTicketsModel;

import java.util.List;

public class MovieAvailabilityResult {


    public List<TheatersAndTicketsModel> theatersAndTicketsModelList;
    public TheatersAndTicketsModel selectedTheater;
    public String error;
    public boolean showData;

    public MovieAvailabilityResult(List<TheatersAndTicketsModel> theatersAndTicketsModelList, String error,boolean showData) {
        this.theatersAndTicketsModelList = theatersAndTicketsModelList;
        this.error = error;
        this.showData = showData;
    }

    public boolean isShowData() {
        return showData;
    }

    public void setShowData(boolean showData) {
        this.showData = showData;
    }

    public void setSelectedTheater(TheatersAndTicketsModel selectedTheater) {
        this.selectedTheater = selectedTheater;
    }

    public TheatersAndTicketsModel getSelectedTheater() {
        return selectedTheater;
    }

    public List<TheatersAndTicketsModel> getTheatersAndTicketsModelList() {
        return this.theatersAndTicketsModelList;
    }

    public String getError() {
        return error;
    }
}
