package com.example.daltonizator;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


//Cette activity est l'écran de présentation de l'application
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

