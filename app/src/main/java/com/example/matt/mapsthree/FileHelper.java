package com.example.matt.mapsthree;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Original class created by Tan on 2/18/2016.
 * Modified by Sin later
 * Cosine joke but I'm too lazy to make one up
 */
public class FileHelper {
    final static String fileName = "data.txt";
    final static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/instinctcoder/readwrite/" ;
    final static String TAG = FileHelper.class.getName();
    static boolean upload;

    public static  String ReadFile( Context context){
        String line = null;

        try {
            FileInputStream fileInputStream = new FileInputStream (new File(path + fileName));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ( (line = bufferedReader.readLine()) != null )
            {
                stringBuilder.append(line + System.getProperty("line.separator"));
            }
            fileInputStream.close();
            line = stringBuilder.toString();

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        }
        catch(IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return line;
    }

    public static boolean saveToFile(List<LatLng> data, int length, List<String> param)
    {
        List<String> vNames = new ArrayList<String>()
        {{
            add("camera_range: ");
            add("coverage: ");
            add("max_cameras: ");
            add("margin_of_safety: ");
            add("max_flight_photos: ");
            add("takeoff_altitude: ");
        }};
        try
        {
            new File(path  ).mkdirs();
            //File file = new File(path+ fileName);

            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM), "pinto.yaml");

            if (!file.exists()) {
                file.createNewFile();
            }

            String darti;
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(("polygon: [").getBytes());
            for (int i = 0; i <= length-2; i++)
            {
                darti = Double.toString(data.get(i).latitude) + "," + Double.toString(data.get(i).longitude);
                fos.write(("[" + darti + "],").getBytes());
            }
            darti = Double.toString(data.get(length-1).latitude) + "," + Double.toString(data.get(length-1).longitude);
            fos.write(("[" + darti + "]]").getBytes());
            for (int i = 0; i < 6; i++)
            {
                darti = vNames.get(i) + (param.get(i));
                fos.write(("\r\n" + darti).getBytes());
            }

            return true;
        }
        catch(FileNotFoundException ex)
        {
            Log.d(TAG, ex.getMessage());
        }
        catch(IOException ex)
        {
            Log.d(TAG, ex.getMessage());
        }
        return  false;
    }

    public static boolean uploadFile() {



        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

        Uri file = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "pinto.yaml"));
        StorageReference riversRef = mStorageRef.child("DCIM");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        upload = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        upload = false;
                    }
                });
        return upload;
    }



    //clears the file...what else would a clearFile method do?
    public static boolean clearFile()
    {
        try {
            new File(path).mkdirs();

            //File file = new File(path+ fileName);
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM), "pinto.yaml");

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream bos = new FileOutputStream(file, false);
            bos.write(("").getBytes());

            return true;
        }
        catch(FileNotFoundException ex)
        {
            Log.d(TAG, ex.getMessage());
        }
        catch(IOException ex)
        {
            Log.d(TAG, ex.getMessage());
        }
        return  false;
    }

}
