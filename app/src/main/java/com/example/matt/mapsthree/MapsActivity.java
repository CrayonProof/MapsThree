package com.example.matt.mapsthree;

//import packages
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.PolyUtil;

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
import com.google.android.gms.location.FusedLocationProviderClient;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import java.util.PriorityQueue;


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
    public static ArrayList<LatLng> val = new ArrayList<LatLng>();
    public static ArrayList<Polyline> lines = new ArrayList<Polyline>();
    static List<Double> dist = new ArrayList<Double>();
    public static int loc;
    public static int areaMode = 0; //0: general, 1: Medium Priority, 2: High Priority
    public static int areaColor = 16729600;
    public static PolygonOptions[] areas = new PolygonOptions[3];
    ArrayList<Polygon> perim = new ArrayList<Polygon>();
    public static ArrayList<Polygon> polyOne = new ArrayList<Polygon>();
    ArrayList<Polygon> polyTwo = new ArrayList<Polygon>();
    ArrayList<Polygon> polyThree = new ArrayList<Polygon>();
    ArrayList<Polygon> zDelete = new ArrayList<Polygon>();

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
    RadioGroup polyType;
    private FusedLocationProviderClient mFusedLocationClient;


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

        if (ContextCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {



            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION},
                    102);
        } else {
            // Permission has already been granted
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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
        polyType = (RadioGroup)findViewById(R.id.polyType);
        radOne = findViewById(R.id.radOne);
        radTwo = findViewById(R.id.radTwo);
        radThree = findViewById(R.id.radThree);
        polyType.clearCheck();

        polyType.setVisibility(View.INVISIBLE);

        FloatingActionButton newArea = findViewById(R.id.newArea);
        newArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (polyOne.size() < 1)
                {
                    radOne.setVisibility(View.VISIBLE);
                    radTwo.setVisibility(View.INVISIBLE);
                    radThree.setVisibility(View.INVISIBLE);
                }
                else
                    {
                        radOne.setVisibility(View.INVISIBLE);
                        radTwo.setVisibility(View.VISIBLE);
                        radThree.setVisibility(View.VISIBLE);
                    }
                polyType.setVisibility(View.VISIBLE);

                radOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        polyType.setVisibility(View.INVISIBLE);
                        Is_MAP_Moveable = true;
                        read_in_polygon(1);
                    }
                });
                radTwo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        polyType.setVisibility(View.INVISIBLE);
                        Is_MAP_Moveable = true;
                        read_in_polygon(2);
                    }
                });
                radThree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        polyType.setVisibility(View.INVISIBLE);
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

    @Override
    public void onBackPressed()
    {
        if (zDelete.size() > 0)
        {
            if (polyOne.contains(zDelete.get(zDelete.size()-1)))
            {
                polyOne.remove(zDelete.get(zDelete.size()-1));
            }
            if (polyTwo.contains(zDelete.get(zDelete.size()-1)))
            {
                polyTwo.remove(zDelete.get(zDelete.size()-1));
            }
            if (polyThree.contains(zDelete.get(zDelete.size()-1)))
            {
                polyThree.remove(zDelete.get(zDelete.size()-1));
            }
            zDelete.get(zDelete.size()-1).remove();
            zDelete.remove(zDelete.size()-1);
        }

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
                if (zDelete.contains(a))
                {
                    zDelete.remove(a);
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
        if (FileHelper.saveToFile(polyOne.get(0).getPoints(), polyOne.get(0).getPoints().size(), arg0)){
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
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(provo));
        centerMapOnMyLocation();


    }

    private void centerMapOnMyLocation() {
        if(ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {mMap.setMyLocationEnabled(true);

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            LatLng myLocation = new LatLng(40.6892, -74.0445);
                            if (location != null) {
                                myLocation = new LatLng(location.getLatitude(),
                                        location.getLongitude());
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16));
                            }
                                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16));
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }


    }

    private boolean isPointInPolygon(LatLng tap, Polygon aPoly) {

        ArrayList<LatLng> vertices = new ArrayList<LatLng>();
        vertices.addAll(aPoly.getPoints());

        int intersectCount = 0;
        for (int j = 0; j < vertices.size() - 1; j++) {
            if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
                intersectCount++;
            }
        }

        return ((intersectCount % 2) == 1); // odd = inside, even = outside;
    }

    private boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {

        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = tap.latitude;
        double pX = tap.longitude;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY)
                || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or
            // b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX); // Rise over run
        double bee = (-aX) * m + aY; // y = mx + b
        double x = (pY - bee) / m; // algebra

        return x > pX;
    }

    private LatLng findNearestBound(LatLng point)
    {
        LatLng closest = polyOne.get(0).getPoints().get(0);
        for (int i = 1; i < polyOne.get(0).getPoints().size(); i ++)
        {
            if (distanceBetween(polyOne.get(0).getPoints().get(i), point) < distanceBetween(closest, point))
            {
                closest = polyOne.get(0).getPoints().get(i);
            }
        }
        return closest;
    }

    private double distanceBetween(LatLng a, LatLng b)
    {
        return Math.sqrt(Math.pow(a.latitude - b.latitude, 2) + Math.pow(a.longitude - b.longitude, 2));
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

                    /*
                    if (!(type == 1))
                    {
                        if(!PolyUtil.containsLocation(latLng, polyOne.get(0).getPoints(), false))
                        {
                            latLng = findNearestBound(latLng);
                        }
                    }
                    */

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
                                zDelete.add(polyOne.get(polyOne.size() - 1));
                                polyOne.get(polyOne.size()-1).setPoints(simplifyPoly(polyOne.get(polyOne.size()-1)));
                                polyOne.get(polyOne.size()-1).setClickable(true);
                                polyOne.get(polyOne.size()-1).setPoints(setPolyClockwise(polyOne.get(polyOne.size()-1)));
                            }
                            else if (type == 2)
                            {
                                polyTwo.add(mMap.addPolygon(opt));
                                zDelete.add(polyTwo.get(polyTwo.size() - 1));
                                polyTwo.get(polyTwo.size()-1).setClickable(true);
                                polyTwo.get(polyTwo.size()-1).setPoints(simplifyPoly(polyTwo.get(polyTwo.size()-1)));
                                polyTwo.get(polyTwo.size()-1).setPoints(setPolyClockwise(polyTwo.get(polyTwo.size()-1)));
                                polyTwo.get(polyTwo.size()-1).setPoints(trimGon(polyTwo.get(polyTwo.size()-1)));
                            }
                            else
                            {
                                polyThree.add(mMap.addPolygon(opt));
                                zDelete.add(polyThree.get(polyThree.size() - 1));
                                polyThree.get(polyThree.size()-1).setClickable(true);
                                polyThree.get(polyThree.size()-1).setPoints(simplifyPoly(polyThree.get(polyThree.size()-1)));
                                polyThree.get(polyThree.size()-1).setPoints(setPolyClockwise(polyThree.get(polyThree.size()-1)));
                                polyThree.get(polyThree.size()-1).setPoints(trimGon(polyThree.get(polyThree.size()-1)));
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

    private ArrayList<LatLng> simplifyPoly(Polygon p)
    {
        int vertices =4;

        if (vertices == -1)
        {

            ArrayList<LatLng> pList = new ArrayList<LatLng>();
            pList.addAll(p.getPoints());
            double deltaTheta = 0;
            double x1;
            double x2;
            double y1;
            double y2;
            ArrayList<Integer> toDelete = new ArrayList<Integer>();
            double THETA_THRESHOLD = Math.toRadians(60); //critical value for significant vertices.
            int lookingAt = 0;
            for (int i = 1; i < pList.size(); i ++)
            {
                x1 = pList.get(lookingAt).longitude;
                x2 = pList.get(i).longitude;
                y1 = pList.get(lookingAt).latitude;
                y2 = pList.get(i).latitude;
                if ((x1 == x2)&&(y1==y2))
                    toDelete.add(i);
                else if((y2 < y1) == (x2 < x1))
                    deltaTheta = (Math.abs((y2-y1)/(x2-x1)));
                else
                    deltaTheta = (Math.abs((x2-x1)/(y2-y1)));
                System.out.println((y2-y1)/(x2-x1));
                System.out.println("deltaTheta: " + deltaTheta);
                System.out.println("THRESHOLD: " + THETA_THRESHOLD);
                if (!(deltaTheta > THETA_THRESHOLD))
                {
                    toDelete.add(i);
                }
                else
                {
                    deltaTheta = 0;
                    lookingAt = i;
                }
            }
            System.out.println("toDelete size: " + toDelete.size());
            System.out.println("pList size 1: " + pList.size());
            for (int i = toDelete.size() - 1; i > 0; i--)
            {
                pList.remove((int) toDelete.get(i));
            }
            System.out.println("pList size 2: " + pList.size());

            return pList;
        }
        else
        {
            CyclicArrayList<LatLng> pList = new CyclicArrayList<LatLng>();
            pList.addAll(p.getPoints());
            for (int i = 1; i < pList.size(); i ++)
            {
                //TODO: create array of top [vertices] angles, then replace pList with that
            }
            return pList;
        }


    }

    private double findAngle(LatLng a, LatLng b, LatLng c)
    {
        double ab = Math.sqrt(Math.pow((a.longitude - b.longitude), 2) + Math.pow((a.latitude - b.latitude), 2));
        double bc = Math.sqrt(Math.pow((b.longitude - c.longitude), 2) + Math.pow((b.latitude - c.latitude), 2));
        double ac = Math.sqrt(Math.pow((a.longitude - c.longitude), 2) + Math.pow((a.latitude - c.latitude), 2));

        return Math.acos((Math.pow(ab, 2) + (Math.pow(ac, 2) - (Math.pow(bc, 2))))/(2 * ab * ac));
    }

    //recursively finds boundary to within 1 meter with bianary search
    public LatLng findIntersect(LatLng a, LatLng b, int i) //a is outside, b is inside
    {
        LatLng c = new LatLng ((a.latitude - b.latitude) /2 + b.latitude, (a.longitude - b.longitude) /2 + b.longitude);
        boolean pointInside = PolyUtil.containsLocation(c, polyOne.get(0).getPoints(), false);
        if (pointInside)
        {
            if (i > 15)
            {
                return c;
            }
            else
            {
                return findIntersect(a, c, i +1);
            }
        }
        else
        {
            if (i > 15)
            {
                return b;
            }
            else
            {
                return findIntersect(c, b, i +1);
            }
        }

    }

    //TODO:Check where points are being added. Should add only at entry and exit. Maybe check recursive boundary search

    private List trimGon(Polygon po)
    {
        int priorityStart = 0;
        int priorityEnd = 0;
        int generalStart = 0;
        int generalEnd = 0;
        LatLng[] entryIntersect = new LatLng[4]; //stores 4 points necessary to calculate intersection, priority line is defined by first two points, general line is defined by second two
        LatLng[] exitIntersect = new LatLng[4]; //same as above but for exit intersection
        List poList = po.getPoints();
        ArrayList<LatLng> inList = new ArrayList<LatLng>();
        ArrayList<LatLng> outList = new ArrayList<LatLng>();
        int s = 0;

        if (PolyUtil.containsLocation(po.getPoints().get(i), polyOne.get(0).getPoints(), false))
        {

        }
        boolean addToEnd = true;
        boolean currentPointInside;
        boolean previousPointInside;

        //creates an ordered list of all points inside priority area from first inside to last inside
        for (int i = 0; i < polyOne.get(0).getPoints().size(); i++) //for all survey points
        {
            currentPointInside = PolyUtil.containsLocation(polyOne.get(0).getPoints().get(i), po.getPoints(), false);
            previousPointInside = PolyUtil.containsLocation(polyOne.get(0).getPoints().get(trueMod((i - 1), polyOne.get(0).getPoints().size())), po.getPoints(), false);
            System.out.println(i + ":  currPo: " + currentPointInside + " prePo: " + previousPointInside);
            if(!currentPointInside) //outside priority area
            {
                if (currentPointInside = !previousPointInside) //just exited
                {
                    addToEnd = false;
                    generalEnd = i;
                    //entryIntersect[2] = polyOne.get(0).getPoints().get(i);
                    //entryIntersect[3] = polyOne.get(0).getPoints().get(trueMod((i - 1), polyOne.get(0).getPoints().size()));
                    //inList.add(findIntersect(polyOne.get(0).getPoints().get(i), polyOne.get(0).getPoints().get(trueMod(i-1, polyOne.get(0).getPoints().size())), 0));
                    //inList.add(new LatLng(0, 0));
                }

            }
            else // inside priority area
            {
                if (currentPointInside = !previousPointInside) //just entered
                {
                    generalStart = i;
                    //exitIntersect[2] = polyOne.get(0).getPoints().get(i);
                    //exitIntersect[3] = polyOne.get(0).getPoints().get(trueMod((i - 1), polyOne.get(0).getPoints().size()));
                    //inList.add(findIntersect(polyOne.get(0).getPoints().get(trueMod(i-1, polyOne.get(0).getPoints().size())), polyOne.get(0).getPoints().get(i), 0));
                    //inList.add(new LatLng(0, 0));
                }
                if (addToEnd)
                {
                    inList.add(polyOne.get(0).getPoints().get(i));
                }
                else
                {
                    inList.add(s, polyOne.get(0).getPoints().get(i));
                    s++;
                }
            }
        }

        boolean alreadyBeenIn = false;
        for (int i = 0; i < po.getPoints().size(); i++) //for all priority points
        {
            currentPointInside = PolyUtil.containsLocation(po.getPoints().get(i), polyOne.get(0).getPoints(), false);
            previousPointInside = PolyUtil.containsLocation(po.getPoints().get(trueMod((i - 1), po.getPoints().size())), polyOne.get(0).getPoints(), false);
            if (!currentPointInside) //outside survey area
            {
                //if just exited
                if (currentPointInside = !previousPointInside)
                {
                    priorityStart = i;
                    exitIntersect[0] = po.getPoints().get(i);
                    exitIntersect[1] = polyOne.get(0).getPoints().get(trueMod((i - 1), po.getPoints().size()));
                }
                poList.remove(po.getPoints().get(i));
            }
            else
            {
                if (currentPointInside = !previousPointInside)
                {
                    priorityEnd = i;
                    entryIntersect[0] = po.getPoints().get(i);
                    entryIntersect[1] = polyOne.get(0).getPoints().get(trueMod((i - 1), po.getPoints().size()));
                }
            }
        }
        //poList.add(trueMod(priorityEnd, poList.size()), findIntersectionByPoints(entryIntersect));
        for (int i = inList.size()-1; i > -1; i--)
        {
            //poList.add(trueMod(priorityStart, poList.size()), inList.get(i));
        }
        //poList.add(trueMod(priorityStart, poList.size()), findIntersectionByPoints(exitIntersect));
        for (int i = 0 ; i <  inList.size(); i++)
        {
            poList.add(inList.get(i));
        }

        return poList;
    }

    private LatLng findIntersectionByPoints(LatLng[] points)
    {
        double ya1 = points[1].latitude;
        double ya2 = points[2].latitude;
        double xa1 = points[1].longitude;
        double xa2 = points[2].longitude;
        double yb1 = points[3].latitude;
        double yb2 = points[4].latitude;
        double xb1 = points[3].longitude;
        double xb2 = points[4].longitude;

        double ma = (ya1-ya2)/(xa1-xa2);
        double mb = (yb1-yb2)/(xb1-xb2);

        double ba = ya1 - (ma*xa1);
        double bb = yb1 - (mb*xb1);

        double x = (ba-bb)/(ma*mb);
        double y = ma*x + ba;

        return new LatLng(x, y);
    }

    private int trueMod(int number, int mod)
    {
       return ((number % mod) + mod) % mod;
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

    private List<LatLng> setPolyClockwise(Polygon p) {
        List<LatLng> pList = p.getPoints();
        int minX = 0;
        int minY = 0;
        int maxX = 0;
        int maxY = 0;

        for(int i = 1; i < pList.size(); i++)
        {
            if(pList.get(i).latitude < pList.get(minY).latitude) //min y
            {
                minY = i;

            }
            else if(pList.get(i).latitude > pList.get(maxY).latitude) //max y
            {
                maxY = i;
            }
            if(pList.get(i).longitude < pList.get(minX).longitude) //min x
            {
                minX = i;

            }
            else if(pList.get(i).longitude > pList.get(maxX).longitude) //max x
            {
                maxX = i;
            }
        }
        int k = minY;
        boolean clockwise;
        while (true)
        {
            if(pList.get(k).equals(pList.get(minX))) //hits min first (clockwise)
            {
                clockwise = true;
                break;
            }
            else if(pList.get(k).equals(pList.get(maxX))) //hits max first (counterclockwise)
            {
                clockwise = false;
                break;
            }
            k = trueMod(k + 1, pList.size());
        }
        if (clockwise) {return pList;}
        else {System.out.println("FLIPPED"); return reverseList(pList);}
    }

    private List<LatLng> reverseList(List<LatLng> list)
    {
        LatLng container;
        for (int i = 0; i < Math.floor(list.size()/2); i ++)
        {
            container = list.get(i);
            list.set(i, list.get(list.size()-1-i));
            list.set(list.size()-1-i, container);

        }
        return list;
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
