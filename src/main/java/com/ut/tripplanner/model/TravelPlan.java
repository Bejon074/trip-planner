package com.ut.tripplanner.model;

public class TravelPlan {
    private String departureStop;
    private String arrivalStop;
    private String dateAndTime;

    public String getDepartureStop() {
        return departureStop;
    }

    public void setDepartureStop(String departureStop) {
        this.departureStop = departureStop;
    }

    public String getArrivalStop() {
        return arrivalStop;
    }

    public void setArrivalStop(String arrivalStop) {
        this.arrivalStop = arrivalStop;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    @Override
    public String toString() {
        return "TravelPlan{" +
                "departureStop='" + departureStop + '\'' +
                ", arrivalStop='" + arrivalStop + '\'' +
                ", dateAndTime='" + dateAndTime + '\'' +
                '}';
    }
}
