package com.example.firebasedatabaseproject.googlemap.model;

import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;

public class PolyLineModel {
    private String distance;
    private String duration;
    private ArrayList<Polyline> polyline;

    public PolyLineModel(String distance, String duration, ArrayList<Polyline> polyline) {
        this.distance = distance;
        this.duration = duration;
        this.polyline = polyline;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public ArrayList<Polyline> getPolyline() {
        return polyline;
    }

    public void setPolyline(ArrayList<Polyline> polyline) {
        this.polyline = polyline;
    }
}
