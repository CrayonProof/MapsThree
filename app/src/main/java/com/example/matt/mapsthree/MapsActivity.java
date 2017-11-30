package com.example.matt.mapsthree;

//import packages
import android.content.Intent;
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
import java.util.List;
import android.view.View;
import android.widget.Button;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static GoogleMap mMap;
    public static int i = 0;
    static LatLng provo = new LatLng(40.26844786793794, -111.63785051554441);
    public static List<LatLng> seq = new ArrayList<LatLng>();
    public static List<Polyline> loneboi = new ArrayList<Polyline>();
    public static List<Marker> markiboi = new ArrayList<Marker>();
    static List<Double> dist = new ArrayList<Double>();
    public static int loc;

    Button btnNxt, btnSave, btnRef;
    EditText txtInput;
    TextView txtContent;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_maps);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FileHelper.saveToFile(seq, i)){
                    Toast.makeText(MapsActivity.this,"Saved to file",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MapsActivity.this,"Error save file!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //"Finalize and Continue" button. Advances to the parameter selection activity
        btnNxt = (Button) findViewById(R.id.btnNxt);
        btnNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, ParamActivity.class));
            }
        });
    }
/*
    //retrieves saved data in case of activity refresh.
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        i = savedInstanceState.getInt("i");
        if (i > 0) {
            pline();
            rmark();
        }
    }
*/
    @Override
    protected void onStart()
    {
        super.onStart();
        //"i don't do anything" button. A testing feature to refresh maps activity.
        btnRef = (Button) findViewById(R.id.btnRef);
        btnRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i > 0) {
                    pline();
                    rmark();
                }
            }
        });
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
    //Allows Filehelper class to be called and passed marker LatLng (local data) from other classes.
    public static boolean saveFile()
    {
        if (FileHelper.saveToFile(seq, i)){
            return true;
        }else{
            return false;
        }
    }

    //retrieves location when user drags marker to new location, toasts latlng to screen
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

    //Clears and redraws updated polylines that make up the polygon encompassing the flight area.
    public static void pline() {
        for(Polyline line : loneboi)
        {
            line.remove();
        }

        loneboi.removeAll(loneboi);
        //iterates through each pair of markers, adding a polyline to the polyline arrayList for each one.
        for ( int j = 0; j < i-1; j++)
        {
            loneboi.add(mMap.addPolyline(new PolylineOptions()
                    .add((seq.get(j)), (seq.get(j+1)))
                    .width(5)
                    .color(Color.RED)));

        }
        //Draws the last line, from the last vertice to the first one.
        loneboi.add(mMap.addPolyline(new PolylineOptions()
                .add((seq.get(i-1)), (seq.get(0)))
                .width(5)
                .color(Color.RED)));

    }

    //Refresh all markers when activity is refreshed. Identical to pline method but for markers.
    public static void rmark() {
        for(Marker marki : markiboi)
        {
            marki.remove();
        }

        markiboi.removeAll(markiboi);

        for ( int j = 0; j <= i-1; j++)
        {
            markiboi.add(mMap.addMarker(new MarkerOptions()
                    .position(seq.get(j))
                    .draggable(true)
                    .snippet(String.valueOf(j))));

        }
    }

    //Creates a new marker wherever user long presses the map.
    public void makeMarker() {
        mMap.setOnMapLongClickListener(new OnMapLongClickListener() {

            //@Override
            public void onMapLongClick (LatLng arg0){
                // TODO Auto-generated method stub
                //runs this code for everything after the first three points. Figures out where the new marker fits in to the polygon.
                if (false)// i >= 3)
                {
                    loc = findInx(arg0);

                    // create new marker when user long clicks
                    markiboi.add(loc, (mMap.addMarker(new MarkerOptions()
                            .position(arg0)
                            .draggable(true)
                            .snippet(String.valueOf(i)))));
                    Toast.makeText(MapsActivity.this, "New marker created",
                            Toast.LENGTH_SHORT).show();
                    seq.add(loc, arg0);
                }
                //runs this code for the first three markers created
                else if (true)//(i < 3)
                {
                    // create new marker when user long clicks
                    markiboi.add(mMap.addMarker(new MarkerOptions()
                            .position(arg0)
                            .draggable(true)
                            .snippet(String.valueOf(i))));
                    Toast.makeText(MapsActivity.this, "New marker created",
                            Toast.LENGTH_SHORT).show();
                    seq.add(arg0);

                }

                i++;
                findLocation();
                if (i > 1) {
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

        // mMap.addMarker(new MarkerOptions().position(provo).title("marka").draggable(true).snippet("0"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(provo));

        findLocation();
        makeMarker();
        //seq.add(provo);

        if (i > 0) {
            pline();
            rmark();
        }
    }

    //determines what index to inject new vertices at on the polygon. Returns int
    public int findInx (LatLng x)
    {
        List<Double> dists = new ArrayList<Double>();
        List<Integer> inxs = new ArrayList<Integer>();

        for (int j = 0; j < i; j++ )
        {
            //dists.add(findLineDist(ax, bx, ay, by))
            //YAAAAAAAAAAAAAAAAAAAAAAAAHOOOOOO
        }
        return 0;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("i", i);
        //outState.putList("loneboi", loneboi);
        super.onSaveInstanceState(outState);
    }



}
