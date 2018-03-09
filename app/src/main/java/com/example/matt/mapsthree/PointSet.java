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

    public double[] getX()
    {
        double[] x = new double[points.size()];
        for (int i = 0; i < points.size(); i++)
        {
            x[i] = (points.get(i)).longitude;
        }
        return x;
    }

    public double[] getY()
    {
        double[] y = new double[points.size()];
        for (int i = 0; i < points.size(); i++)
        {
            y[i] = (points.get(i)).latitude;
        }
        return y;
    }

    public ArrayList getPoints()
    {
        return points;
    }
    public int count()
    {
        return points.size();
    }
    public ArrayList<LatLng> getMidpoints()
    {
        ArrayList<LatLng> midPoints = new ArrayList<LatLng>();
        for (int i = 0; i < points.size(); i++)
        {
            midPoints.add(new LatLng( ((points.get(i).latitude) - (points.get(i + 1).latitude))/2,
                    ((points.get(i).longitude) - (points.get(i + 1).longitude))/2));
        }
        return midPoints;
    }
    public LatLng getMidPoint(int i)
    {
        return (this.getMidpoints()).get(i);
    }
}
