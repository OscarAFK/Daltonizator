package com.example.daltonizator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;


//Cette activity permet d'afficher quelques infos sur l'applications et de mettre des sources.
public class APropos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_propos);
        TextView textview = findViewById(R.id.textView);
        textview.setMovementMethod(new ScrollingMovementMethod());
    }

    public void closeActivity(View view){
        finish();
    }
}
