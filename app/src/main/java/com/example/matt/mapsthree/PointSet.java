package com.example.matt.mapsthree;

import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

/**
 * Created by matt on 1/4/18.
 */

public class PointSet {

    int priorityLevel;
    ArrayList<LatLng> points = new ArrayList<LatLng>();

    public PointSet(int arg0)
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
        points.set(index, location);
    }

    public int getPriorityLevel()
    {
        return priorityLevel;
    }

    public double[] readX()
    {
        double[] x = new double[points.size()];
        for (int i = 0; i < points.size(); i++)
        {
            x[i] = (points.get(i)).longitude;
        }
        return x;
    }

    public double[] readY()
    {
        double[] y = new double[points.size()];
        for (int i = 0; i < points.size(); i++)
        {
            y[i] = (points.get(i)).latitude;
        }
        return y;
    }

    public ArrayList readPoints()
    {
        return points;
    }
    public int count()
    {
        return points.size();
    }
}
