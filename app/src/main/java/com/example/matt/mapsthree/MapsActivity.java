package com.example.matt.mapsthree;

//import necessary packages
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import android.widget.Toast;
import com.google.android.gms.maps.model.Marker;
import android.graphics.Color;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import java.text.*;
import java.util.Date;
import java.io.File;
import java.io.FileWriter;
import android.os.Environment;
import java.io.IOException;
import android.util.Log;
import java.lang.Math;
import java.util.Vector;
//soopre

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "*Click*",
                        Toast.LENGTH_SHORT).show();            }
        });
/*
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
        Date now = new Date();
        String fileName = formatter.format(now) + ".txt";//like 2016_01_12.txt


        try
        {
            File root = new File(Environment.getExternalStorageDirectory()+File.separator+"Music_Folder", "Report Files");
            //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists())
            {
                root.mkdirs();
            }
            File gpxfile = new File(root, fileName);


            FileWriter writer = new FileWriter(gpxfile,true);
            writer.append(sBody+"\n\n");
            writer.flush();
            writer.close();
            Toast.makeText(this, "Data has been written to Report File", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();

        }

*/
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


//retrives location when user drags marker to new location, toasts latlng to screen
    public void findLocation() {
        mMap.setOnMarkerDragListener(new OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
                // TODO Auto-generated method stub
                // Here your code
                Toast.makeText(MapsActivity.this, "Dragging",
                        Toast.LENGTH_SHORT).show();
                String s = marker.getSnippet();
                seq.set(Integer.parseInt(s), marker.getPosition());
                pline();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // TODO Auto-generated method stub
                LatLng position = marker.getPosition(); //
                Toast.makeText(
                        MapsActivity.this,
                        "Lat " + position.latitude + " "
                                + "Long " + position.longitude,
                        Toast.LENGTH_LONG).show();
                String s = marker.getSnippet();
                seq.set(Integer.parseInt(s), position);
                pline();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                // TODO Auto-generated method stub
                // Toast.makeText(MainActivity.this, "Dragging",
                // Toast.LENGTH_SHORT).show();
                System.out.println("Dragging");
                String s = marker.getSnippet();
                seq.set(Integer.parseInt(s), marker.getPosition());
                pline();
            }
        });
    }

    LatLng provo = new LatLng(40.26844786793794, -111.63785051554441);
    int i = 0;
    List<LatLng> seq = new ArrayList<LatLng>();
    List<Polyline> loneboi = new ArrayList<Polyline>();
    List<Marker> markiboi = new ArrayList<Marker>();
    List<Double> dist = new ArrayList<Double>();

    public void pline() {
        for(Polyline line : loneboi)
        {
            line.remove();
        }

        loneboi.removeAll(loneboi);

        for ( int j = 0; j < i; j++)
        {
            loneboi.add(mMap.addPolyline(new PolylineOptions()
                    .add((seq.get(j)), (seq.get(j+1)))
                    .width(5)
                    .color(Color.RED)));

        }
        loneboi.add(mMap.addPolyline(new PolylineOptions()
                .add((seq.get(i)), (seq.get(0)))
                .width(5)
                .color(Color.RED)));
    }

    public int loc;

    public void makeMarker() {
        mMap.setOnMapLongClickListener(new OnMapLongClickListener() {

            //@Override
            public void onMapLongClick (LatLng arg0){
                // TODO Auto-generated method stub

                if ( i >= 3)
                {
                    i++;
                    loc = getRelativeLocation(arg0);

                    // create new marker when user long clicks
                    markiboi.add(loc, (mMap.addMarker(new MarkerOptions()
                            .position(arg0)
                            .draggable(true)
                            .snippet(String.valueOf(i)))));
                    Toast.makeText(MapsActivity.this, "New marker created",
                            Toast.LENGTH_SHORT).show();
                    seq.add(loc, arg0);
                    findLocation();
                    pline();
                }
                else if ( i < 3)
                {
                    i++;

                    // create new marker when user long clicks
                    markiboi.add(mMap.addMarker(new MarkerOptions()
                            .position(arg0)
                            .draggable(true)
                            .snippet(String.valueOf(i))));
                    Toast.makeText(MapsActivity.this, "New marker created",
                            Toast.LENGTH_SHORT).show();
                    seq.add(arg0);
                    findLocation();
                    pline();
                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        // Add a marker in Rock Canyon and move the camera

        mMap.addMarker(new MarkerOptions().position(provo).title("marka").draggable(true).snippet("0"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(provo));

        findLocation();
        makeMarker();
        seq.add(provo);
    }

    public double calculateByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    Vector v = new Vector();

    public int findNearest(LatLng x)
    {
        int minDistInx = 0;
        double presDist = (calculateByDistance(x, seq.get(0)));
        double minDist = presDist;

        for (int in = 0; in < i; in++)
        {
            presDist = (calculateByDistance(x, seq.get(in)));

            if (presDist < minDist)
            {
                minDist = presDist;
                minDistInx = in;
            }
        }
        return minDistInx;
    }

    public int q(double tx, double ty)
    {
        if ((tx > 0)&&(ty > 0))
        {
            return(1);
        }
        else if ((tx > 0)&&(ty < 0))
        {
            return(2);
        }
        else if ((tx < 0)&&(ty < 0))
        {
            return(3);
        }
        else if ((tx < 0)&&(ty > 0))
        {
            return(4);
        }
        else
        {
            return(0);
        }
    }

    public double totadeg(int q,double angle)
    {
        if (q == 1)
        {
            return (90 - angle);
        }
        else if (q == 2)
        {
            return (90 + angle);
        }
        else if (q == 3)
        {
            return (270 - angle);
        }
        else if (q == 4)
        {
            return (270 + angle);
        }
        else
        {
            return (0);
        }
    }

    //********

    public int getRelativeLocation(LatLng x)
    {
        int nearest = findNearest(x);
        int up;
        int down;
        if ((nearest + 1) > (seq.size()-1))
        {
            up = 0;
        }
        else
        {
            up = nearest + 1 ;
        }
        if (nearest == 0)
        {
            down = seq.size()-1;
        }
        else
        {
            down = nearest - 1 ;
        }
        double olat = seq.get(nearest).latitude;
        double olon = seq.get(nearest).longitude;
        double alat = seq.get(down).latitude;
        double alon = seq.get(down).longitude;
        double blat = seq.get(up).latitude;
        double blon = seq.get(up).longitude;
        double ay = alat - olat;
        double ax = alon - olon;
        double by = blat - olat;
        double bx = blon - olon;
        double xy = (x.latitude)-olat;
        double xx = (x.longitude)-olon;
        double adeg = ((Math.atan(Math.abs(ay/ax)))*180)/Math.PI;
        double bdeg = ((Math.atan(Math.abs(by/bx)))*180)/Math.PI;
        double xdeg = ((Math.atan(Math.abs(xy/xx)))*180)/Math.PI;
        int aq =  q(ax, ay);
        int bq =  q(bx, by);
        int xq =  q(xx, xy);
        double awo = totadeg(aq, adeg);
        double bwo = totadeg(bq, bdeg);
        double xwo = totadeg(xq, xdeg);
        double minwo = 0;
        double maxwo = 0;
        int minin = 0;
        int maxin = 0;
        if (awo < bwo)
        {
            minwo = awo;
            minin = nearest;
            maxwo = bwo;
            maxin = up;
        }
        else if (bwo < awo)
        {
            if (ax>0) {
                minwo = bwo;
                minin = up;
                maxwo = awo;
                maxin = nearest;
            }
            else
            {
                minwo = bwo;
                minin = nearest;
                maxwo = awo;
                maxin = down;
            }
        }
        double bisec = (((bwo - awo)/2) + awo);
        if ((xwo < minwo)||( xwo > maxwo))
        {
            bisec = bisec - 180;
            if (bisec < 0)
            {
                bisec = 360 - bisec;
                if ((xwo - (360-bisec)) > 0)
                {
                    return minin;
                }
                else if ((xwo - (360-bisec)) < 0)
                {
                    return maxin;
                }
                else
                {
                    return 0;
                }
            }
            else
            {
                if ((xwo - bisec) > 0)
                {
                    return minin;
                }
                else if ((xwo - bisec) < 0)
                {
                    return maxin;
                }
                else
                {
                    return 0;
                }
            }
        }
        else
        {
            if (xwo < bisec)
            {
                return minin;
            }
            else if (xwo > bisec)
            {
                return maxin;
            }
            else
            {
                return 0;
            }
        }


    }
    static Point lineLineIntersection(Point A, Point B, Point C, Point D)
    {
        // Line AB represented as a1x + b1y = c1
        double a1 = B.y - A.y;
        double b1 = A.x - B.x;
        double c1 = a1*(A.x) + b1*(A.y);

        // Line CD represented as a2x + b2y = c2
        double a2 = D.y - C.y;
        double b2 = C.x - D.x;
        double c2 = a2*(C.x)+ b2*(C.y);

        double determinant = a1*b2 - a2*b1;

        if (determinant == 0)
        {
            // The lines are parallel. This is simplified
            // by returning a pair of FLT_MAX
            return new Point(Double.MAX_VALUE, Double.MAX_VALUE);
        }
        else
        {
            double x = (b2*c1 - b1*c2)/determinant;
            double y = (a1*c2 - a2*c1)/determinant;
            return new Point(x, y);
        }
    }

    public static void helpre(String args[])
    {
        Point A = new Point(1, 1);
        Point B = new Point(4, 4);
        Point C = new Point(1, 8);
        Point D = new Point(2, 4);

        Point intersection = lineLineIntersection(A, B, C, D);

        if (intersection.x == Double.MAX_VALUE &&
                intersection.y == Double.MAX_VALUE)
        {
            System.out.println("The given lines AB and CD are parallel.");
        }

        else
        {
            // NOTE: Further check can be applied in case
            // of line segments. Here, we have considered AB
            // and CD as lines
            System.out.print("The intersection of the given lines AB" +
                    "and CD is: ");
            Point.displayPoint(intersection);
        }
    }
}
