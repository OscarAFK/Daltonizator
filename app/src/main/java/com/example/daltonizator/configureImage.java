package com.example.daltonizator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

public class configureImage extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    ImageView imageView;
    Button buttonSuivant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_image);

        buttonSuivant = findViewById(R.id.buttonSuivant);
        buttonSuivant.setAlpha(.5f);
        buttonSuivant.setClickable(false);
        imageView = findViewById(R.id.imageSelect);
        imageView.setImageResource(android.R.color.transparent);
    }

    public void closeActivity(View view){
        finish();
    }

    public void chooseDaltonism(View view){
        Intent intent = new Intent(this,choisirTypeDaltonisme.class);

        intent.putExtra("imageUri",imageUri.toString());

        startActivity(intent);
    }

    public void openGallery(View view){

        if (ContextCompat.checkSelfPermission(configureImage.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(configureImage.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);

        }

        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode==PICK_IMAGE){
            if(data.getData()==null) return;

            buttonSuivant.setAlpha(1f);
            buttonSuivant.setClickable(true);
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}
