package com.example.assortment.model;

public class MesureDust {
    private int pm10Value;
    private int pm10Value24;
    private int pm25Value;
    private int pm25Value24;

    public MesureDust() {
    }

    public int getPm10Value() {
        return pm10Value;
    }

    public void setPm10Value(int pm10Value) {
        this.pm10Value = pm10Value;
    }

    public int getPm10Value24() {
        return pm10Value24;
    }

    public void setPm10Value24(int pm10Value24) {
        this.pm10Value24 = pm10Value24;
    }

    public int getPm25Value() {
        return pm25Value;
    }

    public void setPm25Value(int pm25Value) {
        this.pm25Value = pm25Value;
    }

    public int getPm25Value24() {
        return pm25Value24;
    }

    public void setPm25Value24(int pm25Value24) {
        this.pm25Value24 = pm25Value24;
    }

    @Override
    public String toString() {
        return "MesureDust{" +
                "pm10Value=" + pm10Value +
                ", pm10Value24=" + pm10Value24 +
                ", pm25Value=" + pm25Value +
                ", pm25Value24=" + pm25Value24 +
                '}';
    }
}
