package com.example.daltonizator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//Cette activity permet de choisir l'image qu'on va daltonizer. On peut la choisir dans un dossier ou prendre une photo.
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

        //Ces trois lignes permettent de mettre le bouton "suivant" en grisé
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

    //La fonction appelée lors de l'appuie du bouton suivant, et qui emmène à l'activity choisirTypeDaltonisme.
    public void chooseDaltonism(View view){
        Intent intent = new Intent(this,choisirTypeDaltonisme.class);

        intent.putExtra("imageUri",imageUri.toString());

        startActivity(intent);
    }

    //La fonction appelée lors de l'appuie du bouton "Choisir une image dans un dossier"
    public void openGallery(View view){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    //La fonction appelée lors de l'appuie du bouton "Prendre une photo"
    public void takePicture(View view) {
        if(isStoragePermissionGranted()) {
            // créer un intent pour ouvrir une fenêtre pour prendre la photo
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // test pour voir si l'intent peut être géré
            if (intent.resolveActivity(getPackageManager()) != null) {
                // création d'un nom de fichier (pour le tmp)
                String tmp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                try {
                    File photoFile = File.createTempFile("photo" + tmp, ".png", photoDir);

                    // enregistrer le chemin complet de l'image
                    photoPath = photoFile.getAbsolutePath();

                    // création de l'URI
                    Uri photoUri = FileProvider.getUriForFile(configureImage.this,
                            configureImage.this.getApplicationContext().getPackageName() + ".provider",
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //vérification du code de retour et de son état
        if(resultCode == RESULT_OK && requestCode==PICK_IMAGE){
            if(data.getData()==null) return;

            buttonSuivant.setAlpha(1f);
            buttonSuivant.setClickable(true);
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
        else if (resultCode == RESULT_OK && requestCode==RETURN_TAKE_PICTURE) {
            Bitmap image = BitmapFactory.decodeFile(photoPath);
            imageView.setImageBitmap(image);
            buttonSuivant.setAlpha(1f);
            buttonSuivant.setClickable(true);
            imageUri = getImageUri(getApplicationContext(), image);
        }
    }

    //Cette fonction permet de transformer une image de type bitmap en Uri. C'est utile pour transférer les image d'une activity à l'autre
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
