package com.example.assortment.model;

public class TmLocation {
    private double x;
    private double y;

    public TmLocation() {
    }

    public TmLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "TmLocation{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
