package com.example.matt.mapsthree;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by matt on 5/22/18.
 */

public class Vertice implements Comparable<Vertice> {

    private double angle;
    private LatLng location;
    private int compareMetric; //1 = interiorAngle, 2 = x, 3 = y

    public Vertice(double angle, LatLng location)
    {
        this.angle = angle;
        this.location = location;
        compareMetric = 1;
    }

    public Vertice(double angle, LatLng location, String a)
    {
        this.angle = angle;
        this.location = location;
        if (a.equals("interiorAngle"))
            compareMetric = 1;
        if (a.equals("x"))
            compareMetric = 2;
        if (a.equals("y"))
            compareMetric = 3;
    }


    public int compareTo(Vertice b)
    {
        if (compareMetric == 1)
        {
            if (b.getInteriorAngle() > this.getInteriorAngle())
                return -1;
            else if (b.getInteriorAngle() < this.getInteriorAngle())
                return 1;
            else
                return 0;
        }
        else if (compareMetric == 2)
        {
            if (b.getX() > this.getX())
                return -1;
            else if (b.getX() < this.getX())
                return 1;
            else
                return 0;
        }
        else
        {
            if (b.getY() > this.getY())
                return -1;
            else if (b.getY() < this.getY())
                return 1;
            else
                return 0;
        }
    }

    public double getInteriorAngle()
    {
        return angle;
    }

    public LatLng getLocation()
    {
        return location;
    }

    public double getX()
    {
        return location.longitude;
    }

    public double getY()
    {
        return location.latitude;
    }

    public void setCompareMetric(String a)
    {
        if (a.equals("interiorAngle"))
            compareMetric = 1;
        if (a.equals("x"))
            compareMetric = 2;
        if (a.equals("y"))
            compareMetric = 3;
    }
}
