package com.example.daltonizator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class configureImage extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    ImageView imageView;
    Button buttonSuivant;
    Button buttonPrendrePhoto;
    private String photoPath = null;

    //constante
    private static final int RETURN_TAKE_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_image);

        buttonSuivant = findViewById(R.id.buttonSuivant);
        buttonSuivant.setAlpha(.5f);
        buttonSuivant.setClickable(false);
        buttonPrendrePhoto = findViewById(R.id.btnTakePicture);
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

       /* if (ContextCompat.checkSelfPermission(configureImage.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(configureImage.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);

        }*/

        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    // accès à l'appareil photo + mémorisation dans un fichier temporaire

    public void takePicture(View view) {
        // créer un intent pour ouvrir une fenêtre pour prendre la photo
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // test pour voir si l'intent peut être géré
        if(intent.resolveActivity(getPackageManager()) != null) {
            // création d'un nom de fichier (pour le tmp)
            String tmp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            try {
                File photoFile = File.createTempFile("photo"+tmp, ".png", photoDir);

                // enregistrer le chemin complet de l'image
                photoPath = photoFile.getAbsolutePath();

                // création de l'URI
                Uri photoUri = FileProvider.getUriForFile(configureImage.this,
                        configureImage.this.getApplicationContext().getPackageName()+".provider",
                        photoFile);

                // transfert URI vers l'intent pour enregistrer la photo dans le fichier temporaire
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                // ouverture de l'activité par rapport à l'intent
                startActivityForResult(intent, RETURN_TAKE_PICTURE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } // fin takePicture()

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //vérification du code de retour et de son état
        if(resultCode == RESULT_OK && requestCode==PICK_IMAGE){
            if(data.getData()==null) return;

            Bitmap image = BitmapFactory.decodeFile(photoPath);
            imageView.setImageBitmap(image);
            buttonSuivant.setAlpha(1f);
            buttonSuivant.setClickable(true);
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}
