package com.example.moviebuzz.data.model;

import java.util.List;
import java.util.UUID;

public class TheatersAndTicketsResponseModel {

    UUID requestId;
    String requestType;
    List<TheatersAndTicketsModel> theatersAndTicketsModelList;

    public TheatersAndTicketsResponseModel(UUID requestId, String requestType, List<TheatersAndTicketsModel> theatersAndTicketsModelList) {
        this.requestId = requestId;
        this.requestType = requestType;
        this.theatersAndTicketsModelList = theatersAndTicketsModelList;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public String getRequestType() {
        return requestType;
    }

    public List<TheatersAndTicketsModel> getTheatersAndTicketsModelList() {
        return theatersAndTicketsModelList;
    }
}
