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
//import java.util.Collections;
import java.util.List;
import android.view.View;
//import android.view.View.OnClickListener;
import android.widget.Button;
//import java.text.*;
//import java.util.Date;
//import java.io.File;
//import java.io.FileWriter;
//import android.os.Environment;
//import java.io.IOException;
//import android.util.Log;
//import java.lang.Math;
//import java.util.Vector;

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
                    loc = findInx(arg0);

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

    public int findInx (LatLng x)
    {
        List<Double> dists = new ArrayList<Double>();
        List<Integer> inxs = new ArrayList<Integer>();

        for (int j = 0; j < i; j++ )
        {
            //dists.add(findLineDist(ax, bx, ay, by))
            //YAAAAAAAAAAAAAAAAAAAAAAAAYYY
        }
        return 0;
    }

}
