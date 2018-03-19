package com.example.matt.mapsthree;

//import packages
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.gms.maps.model.Marker;
import android.graphics.Color;

import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnPolygonClickListener;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.os.Vibrator;

import android.widget.EditText;
import android.widget.TextView;
import android.Manifest;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    PointSet general = new PointSet(1);
    PointSet low = new PointSet(2);
    PointSet med = new PointSet(3);
    PointSet high = new PointSet(4);

    Boolean Is_MAP_Moveable = false;


    private static GoogleMap mMap;
    public static int i = 0;
    static LatLng provo = new LatLng(40.26844786793794, -111.63785051554441);
    public static List<LatLng> seq = new ArrayList<LatLng>();
    public static List<Polyline> loneboi = new ArrayList<Polyline>();
    public static List<Marker> markiboi = new ArrayList<Marker>();
    public static List<Marker> lowPrio = new ArrayList<Marker>();
    public static List<Marker> medPrio = new ArrayList<Marker>();
    public static List<Marker> hiPrio = new ArrayList<Marker>();
    public static ArrayList<LatLng> val = new ArrayList<LatLng>();
    public static ArrayList<Polyline> lines = new ArrayList<Polyline>();
    static List<Double> dist = new ArrayList<Double>();
    public static int loc;
    public static int areaMode = 0; //0: general, 1: Medium Priority, 2: High Priority
    public static int areaColor = 16729600;
    public static PolygonOptions[] areas = new PolygonOptions[3];
    ArrayList<Polygon> perim = new ArrayList<Polygon>();
    ArrayList<Polygon> polyOne = new ArrayList<Polygon>();
    ArrayList<Polygon> polyTwo = new ArrayList<Polygon>();
    ArrayList<Polygon> polyThree = new ArrayList<Polygon>();
    static List<String> def = new ArrayList<String>()
    {{
        add("0");
        add("0");
        add("0");
        add("0");
        add("0");
        add("0");
    }};
    static int pointMode = 0; //0 = area, 1 = low priority, 2 = med priority, 3 = high priority
    private Spinner spini;
    Button btnNxt, btnSave, radOne, radTwo, radThree, newArea;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //Button btn_draw_State = (Button) findViewById(R.id.btn_draw_State);
         // to detect map is movable

        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_maps);

        // Request write storage permissions
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {



                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        101);
        } else {
            // Permission has already been granted
        }

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FileHelper.clearFile())
                {
                    if (FileHelper.saveToFile(seq, seq.size(), def))
                    {
                        if (FileHelper.uploadFile()) {
                            Toast.makeText(MapsActivity.this,"Saved and uploaded",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MapsActivity.this,"Saved to LOCAL file only",Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(MapsActivity.this,"Error, File Not Saved!!!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MapsActivity.this,"Error, File Not Saved!!!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        for(i = 0; i < 3; i++)
        {
            areas[i] = new PolygonOptions();
        }

        //"Finalize and Continue" button. Advances to the parameter selection activity
        btnNxt = (Button) findViewById(R.id.btnNxt);
        btnNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, ParamActivity.class));
            }
        });

        radOne = findViewById(R.id.radOne);
        radTwo = findViewById(R.id.radTwo);
        radThree = findViewById(R.id.radThree);

        radOne.setVisibility(View.INVISIBLE);
        radTwo.setVisibility(View.INVISIBLE);
        radThree.setVisibility(View.INVISIBLE);

        FloatingActionButton newArea = findViewById(R.id.newArea);
        newArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(polyOne.size());
                if (polyOne.size() < 1) { radOne.setVisibility(View.VISIBLE); }
                radTwo.setVisibility(View.VISIBLE);
                radThree.setVisibility(View.VISIBLE);

                radOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radOne.setVisibility(View.INVISIBLE);
                        radTwo.setVisibility(View.INVISIBLE);
                        radThree.setVisibility(View.INVISIBLE);
                        Is_MAP_Moveable = true;
                        read_in_polygon(1);
                    }
                });
                radTwo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radOne.setVisibility(View.INVISIBLE);
                        radTwo.setVisibility(View.INVISIBLE);
                        radThree.setVisibility(View.INVISIBLE);
                        Is_MAP_Moveable = true;
                        read_in_polygon(2);
                    }
                });
                radThree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radOne.setVisibility(View.INVISIBLE);
                        radTwo.setVisibility(View.INVISIBLE);
                        radThree.setVisibility(View.INVISIBLE);
                        Is_MAP_Moveable = true;
                        read_in_polygon(3);
                    }
                });
            }
        });


        //Turn on and off drawing
        /*
        btn_draw_State.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Is_MAP_Moveable = !Is_MAP_Moveable;
            }
        });
        */

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priorityType, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }



    public void editPoly()
    {
        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon a)
            {
                if (polyOne.contains(a))
                {
                    polyOne.remove(a);
                }
                if (polyTwo.contains(a))
                {
                    polyTwo.remove(a);
                }
                if (polyThree.contains(a))
                {
                    polyThree.remove(a);
                }
                a.remove();
            }
        });
    }

    public void Drawp_Map() {
        seq.addAll(val);
        areas[SpinnerActivity.areaMode].addAll(val);
        if (SpinnerActivity.areaMode == 0) {
            areas[SpinnerActivity.areaMode].strokeColor(Color.RED);
        }
        else if (SpinnerActivity.areaMode == 1) {
            areas[SpinnerActivity.areaMode].strokeColor(Color.YELLOW);
        }
        else {
            areas[SpinnerActivity.areaMode].strokeColor(Color.GREEN);
        }
        //rectOptions.fillColor(Color.argb(50, 00, 100, 255));
        areas[SpinnerActivity.areaMode].strokeWidth(10);
        areas[SpinnerActivity.areaMode].strokeJointType(2);
        if (SpinnerActivity.areaMode == 0) {
            Polygon gen = mMap.addPolygon(areas[SpinnerActivity.areaMode]);
        }
        else if (SpinnerActivity.areaMode == 1) {
            Polygon med = mMap.addPolygon(areas[SpinnerActivity.areaMode]);
        }
        else {
            Polygon hi = mMap.addPolygon(areas[SpinnerActivity.areaMode]);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }

    public class SpinnerActivity extends MapsActivity implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            System.out.println(parent.getItemAtPosition(pos));
            if (parent.getItemAtPosition(pos).equals("General Area"))
            {
                areaMode = 0;
            }
            if (parent.getItemAtPosition(pos).equals("Low Priority"))
            {
                areaMode = 1;
            }
            if (parent.getItemAtPosition(pos).equals("High Priority"))
            {
                areaMode = 2;
            }
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
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
    public static boolean saveFile(List<String> arg0)
    {
        if (FileHelper.saveToFile(seq, i, arg0)){
            return true;
        }else{
            return false;
        }
    }

    public void refresh(){}

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLng(provo));

    }

    public static double preLatitude;
    public static double preLongitude;
    public void read_in_polygon(final int type)
    {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        FrameLayout fram_map = (FrameLayout) findViewById(R.id.fram_map);
        fram_map.setOnTouchListener(new View.OnTouchListener() {

            PolygonOptions opt = new PolygonOptions();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Switch drawMode = findViewById(R.id.drawMode);
                //Is_MAP_Moveable =  drawMode.isChecked();
                if (Is_MAP_Moveable) {
                    float x = event.getX();
                    float y = event.getY();

                    int x_co = Math.round(x);
                    int y_co = Math.round(y);

                    Projection projection = mMap.getProjection();
                    Point x_y_points = new Point(x_co, y_co);

                    LatLng latLng = mMap.getProjection().fromScreenLocation(x_y_points);
                    double latitude = latLng.latitude;
                    double longitude = latLng.longitude;


                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            System.out.println("down");
                            // finger touches the screen
                            //poly.add(new LatLng(latitude, longitude));

                            preLatitude = latitude;
                            preLongitude = longitude;
                            opt.add(new LatLng(latitude, longitude));

                            return true;

                        case MotionEvent.ACTION_MOVE:
                            // finger moves on the screen
                            System.out.println("moving");

                            PolylineOptions polyOpt = new PolylineOptions();
                            polyOpt.add(new LatLng(preLatitude, preLongitude));
                            polyOpt.add(new LatLng(latitude, longitude));
                            if (type == 1) {
                                polyOpt.color(Color.RED);
                            } else if (type == 2) {
                                polyOpt.color(Color.YELLOW);
                            } else {
                                polyOpt.color(Color.GREEN);
                            }
                            polyOpt.width(10);

                            opt.add(new LatLng(latitude, longitude));

                            drawLines(polyOpt);
                            System.out.print(lines.size());

                            preLatitude = latitude;
                            preLongitude = longitude;

                            return true;

                        case MotionEvent.ACTION_UP:
                            System.out.println("up");

                            for(Polyline line : lines)
                            {
                                line.remove();
                            }


                            Is_MAP_Moveable = false;

                            if (type == 1) {
                                opt.strokeColor(Color.RED);
                            } else if (type == 2) {
                                opt.strokeColor(Color.YELLOW);
                            } else {
                                opt.strokeColor(Color.GREEN);
                            }

                            if (type == 1)
                            {
                                polyOne.add(mMap.addPolygon(opt));
                                polyOne.get(polyOne.size()-1).setClickable(true);
                            }
                            else if (type == 2)
                            {
                                polyTwo.add(mMap.addPolygon(opt));
                                polyTwo.get(polyTwo.size()-1).setClickable(true);
                            }
                            else
                            {
                                polyThree.add(mMap.addPolygon(opt));
                                polyThree.get(polyThree.size()-1).setClickable(true);
                            }

                            editPoly();
                            return (false);
                        //break;

                    }


                    return Is_MAP_Moveable;
                }
                return Is_MAP_Moveable;


            }
        });

        System.out.println("return op");
    }

    public void drawLines(PolylineOptions a)
    {
        lines.add(mMap.addPolyline(a));
    }

    ArrayList<Polygon> gen = new ArrayList<Polygon>();

    public PolygonOptions Draw_Map(int type, int draw, PolygonOptions opt) {
        PolygonOptions rectOptions = new PolygonOptions();
        seq.addAll(val);
        rectOptions.addAll(val);
        System.out.println(SpinnerActivity.areaMode);
        if (type == 1) {
            rectOptions.strokeColor(Color.RED);
        } else if (type == 2) {
            rectOptions.strokeColor(Color.YELLOW);
        } else {
            rectOptions.strokeColor(Color.GREEN);
        }
        //rectOptions.fillColor(Color.argb(50, 00, 100, 255));
        rectOptions.strokeWidth(10);
        rectOptions.strokeJointType(2);
        if (draw == 0) {

            gen.add(mMap.addPolygon(rectOptions));
        }
        else
        {
            gen.add(mMap.addPolygon(rectOptions));
            System.out.println("remove");
            perim.add(mMap.addPolygon(opt));
            for(Polygon n: gen) {
                n.remove();
            }
        }
        return(rectOptions);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("i", i);
        //outState.putList("loneboi", loneboi);
        super.onSaveInstanceState(outState);
    }

    public void fileUpload() {
        Toast.makeText(MapsActivity.this,"Uploaded",Toast.LENGTH_SHORT).show();
    }

    public void fileNotUpload() {
        Toast.makeText(MapsActivity.this,"NOT Uploaded",Toast.LENGTH_SHORT).show();
    }



}
