package com.example.daltonizator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;

import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //convertImage();
    }

    public void QuitApp(View view) {
        MainActivity.this.finish();
        System.exit(0);
    }

    public void getToActivityConfigureImage(View view){
        Intent intent = new Intent(this,configureImage.class);
        startActivity(intent);
    }

    public void getToActivityAPropos(View view){
        Intent intent = new Intent(this,APropos.class);
        startActivity(intent);
    }

    public void convertImage(){
        String TAG = "test";
        Log.v(TAG, "Point No1");

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);//MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        }
        Log.v(TAG, "Point No2");
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);//MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        }

        Log.v(TAG, "Point No3");

        Bitmap sourceBitmap = BitmapFactory.decodeFile("/storage/emulated/0/testDaltonizator/image.jpg");
        float[] colorTransform = {/*
                0, 1f, 0, 0, 0,
                0, 0, 0f, 0, 0,
                0, 0, 0, 0f, 0,
                0, 0, 0, 1f, 0};*/
                0.567f,0.433f,0,0,0,
                0.558f,0.442f,0,0,0,
                0,0.242f,0.758f,0,0,
                0,0,0,1,0,
                0,0,0,0,1};

        Log.v(TAG, "Point No4");
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f); //Remove Colour
        colorMatrix.set(colorTransform); //Apply the Red
        Log.v(TAG, "Point No41");
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);
        Log.v(TAG, "Point No42");
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Log.v(TAG, "Point No43");

        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(),
                sourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Log.v(TAG, "Point No5");



        Log.v(TAG, "Point No6");
        //image.setImageBitmap(resultBitmap);

        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);

        try (FileOutputStream out = new FileOutputStream("/storage/emulated/0/testDaltonizator/image2.png")) {
            resultBitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Point No7");
        return;
    }
}

