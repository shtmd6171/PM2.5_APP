package com.example.assortment.model;

public class Nearobservatory {
    private String address;
    private String stationName;


    public Nearobservatory() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public String toString() {
        return "Nearobservatory{" +
                "address='" + address + '\'' +
                ", stationName='" + stationName + '\'' +
                '}';
    }
}
