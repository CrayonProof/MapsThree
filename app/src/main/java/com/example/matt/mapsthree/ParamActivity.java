package com.example.matt.mapsthree;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class ParamActivity extends AppCompatActivity {
    Button btnAct , btnSave;
    EditText e1, e2, e3, e4, e5, e6;
    String s1, s2, s3, s4, s5, s6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        //ioio
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1 = findViewById(R.id.editText9);
                e2 = findViewById(R.id.editText8);
                e3 = findViewById(R.id.editText10);
                e4 = findViewById(R.id.editText13);
                e5 = findViewById(R.id.editText11);
                e6 = findViewById(R.id.editText12);

                s1 = e1.getText().toString();
                if (s1.equals(""))
                {
                    s1 = "0";
                }
                s2 = e2.getText().toString();
                if (s2.equals(""))
                {
                    s2 = "0";
                }
                s3 = e3.getText().toString();
                if (s3.equals(""))
                {
                    s3 = "0";
                }
                s4 = e4.getText().toString();
                if (s4.equals(""))
                {
                    s4 = "0";
                }
                s5 = e5.getText().toString();
                if (s5.equals(""))
                {
                    s5 = "0";
                }
                s6 = e6.getText().toString();
                if (s6.equals(""))
                {
                    s6 = "0";
                }

                List<String> vContent = new ArrayList<String>()
                {{
                    add(s1);
                    add(s2);
                    add(s3);
                    add(s4);
                    add(s5);
                    add(s6);
                }};

                if (FileHelper.clearFile())
                {
                    if (MapsActivity.saveFile(vContent)){
                        Toast.makeText(ParamActivity.this,"Saved to file",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ParamActivity.this,"Error save file!!!",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(ParamActivity.this,"Error, File Not Saved!!!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnAct = (Button) findViewById(R.id.btnArea);
        btnAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p) {
                startActivity(new Intent(ParamActivity.this, MapsActivity.class));
            }

        });
    }
}
