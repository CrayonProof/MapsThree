package com.example.matt.mapsthree;

import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

/**
 * Created by matt on 1/4/18.
 */

public class PointSet {

    int priorityLevel;
    ArrayList<LatLng> points = new ArrayList<LatLng>();

    public void pointSet(int arg0)
    {
        priorityLevel = arg0;
    }

    public void addPoint(LatLng location, int index)
    {
        points.add(index, location);
    }

    public void removePoint(int index)
    {
        points.remove(index);
    }

    public double getY(int index)
    {
        return (points.get(index)).longitude;
    }

    public double getX(int index)
    {
        return (points.get(index)).latitude;
    }

    public String toString()
    {
        String converted = "";
        String darti;
        for (int i = 0; i <= points.size(); i++)
        {
            darti = Double.toString(points.get(i).latitude) + "," + Double.toString(points.get(i).longitude);
            converted = converted + ("[" + darti + "],");
        }
        return converted;
    }

    public void updatePoint(int index, LatLng location)
    {

    }

    public int getPriorityLevel()
    {

    }

    public double[] readX()
    {

    }

    public double[] readY()
    {

    }

    public LatLng[] readPoints()
    {

    }
}
