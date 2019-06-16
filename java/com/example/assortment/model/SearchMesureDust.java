package com.example.assortment.model;

public class SearchMesureDust {
    private String cityName;
    private String pm10Value;
    private String pm25Value;
    private String rootcity;

    private int idx;
    private boolean favor;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getRootcity() {
        return rootcity;
    }

    public void setRootcity(String rootcity) {
        this.rootcity = rootcity;
    }

    public boolean isFavor() {
        return favor;
    }

    public void setFavor(boolean favor) {
        this.favor = favor;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPm10Value() {
        return pm10Value;
    }

    public void setPm10Value(String pm10Value) {
        this.pm10Value = pm10Value;
    }

    public String getPm25Value() {
        return pm25Value;
    }

    public void setPm25Value(String pm25Value) {
        this.pm25Value = pm25Value;
    }

    @Override
    public String toString() {
        return "SearchMesureDust{" +
                "cityName='" + cityName + '\'' +
                ", pm10Value='" + pm10Value + '\'' +
                ", pm25Value='" + pm25Value + '\'' +
                '}';
    }
}
