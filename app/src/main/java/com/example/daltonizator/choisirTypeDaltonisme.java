package com.example.daltonizator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class choisirTypeDaltonisme extends AppCompatActivity {

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choisir_type_daltonisme);


        ImageView imageView = findViewById(R.id.hue);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            imageUri = Uri.parse(bundle.getString("imageUri"));
            //imageView.setImageURI(imageUri);
        }
    }

    public void closeActivity(View view){
        finish();
    }

    public void daltonizer(View view){

    }
}
