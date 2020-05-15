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
import android.widget.Button;

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
}

