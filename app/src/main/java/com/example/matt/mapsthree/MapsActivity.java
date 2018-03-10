package com.example.matt.mapsthree;

//import packages
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;

import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.google.android.gms.maps.model.Marker;
import android.graphics.Color;

import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.os.Vibrator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    PointSet general = new PointSet(1);
    PointSet low = new PointSet(2);
    PointSet med = new PointSet(3);
    PointSet high = new PointSet(4);
    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    FrameLayout fram_map = (FrameLayout) findViewById(R.id.fram_map);
    Button btn_draw_State = (Button) findViewById(R.id.btn_draw_State);
    Boolean Is_MAP_Moveable = false; // to detect map is movable

    private static GoogleMap mMap;
    public static int i = 0;
    static LatLng provo = new LatLng(40.26844786793794, -111.63785051554441);
    public static List<LatLng> seq = new ArrayList<LatLng>();
    public static List<Polyline> loneboi = new ArrayList<Polyline>();
    public static List<Marker> markiboi = new ArrayList<Marker>();
    public static List<Marker> lowPrio = new ArrayList<Marker>();
    public static List<Marker> medPrio = new ArrayList<Marker>();
    public static List<Marker> hiPrio = new ArrayList<Marker>();
    ArrayList<LatLng> val = new ArrayList<LatLng>();
    static List<Double> dist = new ArrayList<Double>();
    public static int loc;
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
                if (FileHelper.clearFile())
                {
                    if (FileHelper.saveToFile(seq, i, def))
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

        //"Finalize and Continue" button. Advances to the parameter selection activity
        btnNxt = (Button) findViewById(R.id.btnNxt);
        btnNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, ParamActivity.class));
            }
        });

        //Turn on and off drawing
        btn_draw_State.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Is_MAP_Moveable = !Is_MAP_Moveable;
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priorityType, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        fram_map.setOnTouchListener(new View.OnTouchListener() {     @Override
        public boolean onTouch(View v, MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            int x_co = Math.round(x);
            int y_co = Math.round(y);

            Projection projection = mMap.getProjection();
            Point x_y_points = new Point(x_co, y_co);

            LatLng latLng = mMap.getProjection().fromScreenLocation(x_y_points);
            //latitude = latLng.latitude;

            //longitude = latLng.longitude;



            int eventaction = event.getAction();
            switch (eventaction) {
                case MotionEvent.ACTION_DOWN:
                    // finger touches the screen
                    val.add(new LatLng(latLng.latitude, latLng.longitude));

                case MotionEvent.ACTION_MOVE:
                    // finger moves on the screen
                    val.add(new LatLng(latLng.latitude, latLng.longitude));

                case MotionEvent.ACTION_UP:
                    // finger leaves the screen
                    Draw_Map();
                    break;
            }

            return Is_MAP_Moveable;

        }
        });
    }

    public void Draw_Map() {
        PolygonOptions rectOptions = new PolygonOptions();
        rectOptions.addAll(val);
        rectOptions.strokeColor(Color.BLUE);
        rectOptions.strokeWidth(7);
        rectOptions.fillColor(Color.CYAN);
        Polygon polygon = mMap.addPolygon(rectOptions);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        //"i don't do anything" button. A testing feature to refresh maps activity.

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

        // Add a marker in Rock Canyon and move the camera

        // mMap.addMarker(new MarkerOptions().position(provo).title("marka").draggable(true).snippet("0"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(provo));
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
