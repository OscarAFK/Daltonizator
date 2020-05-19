package com.example.daltonizator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Random;

//Cette activity permet de comparer l'image original et celle daltonizée. On peut aussi enregistrer l'image ou la partager.
public class daltonizedResult extends AppCompatActivity {

    ImageView result;
    Bitmap resultBitmap;
    String typeDeDaltonisme;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daltonized_result);


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            imageUri = Uri.parse(bundle.getString("imageUri"));
            typeDeDaltonisme = bundle.getString("valeurSpinner");
        }else{return;}
        result = findViewById(R.id.imageDalt);
        ImageView originale = findViewById(R.id.imageOriginale);
        originale.setImageURI(imageUri);

        //On convertit l'image a l'arrivé dans l'activity.
        try {
            convertImage(imageUri,typeDeDaltonisme);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }

    public void closeActivity(View view){
        finish();
    }

    //Cette fonction renvoie la matrice de couleur pour la conversion suivant le type de daltonisme
    private float[] colorTransform(String typeDeDaltonisme){
        switch (typeDeDaltonisme){
            case "Pas de daltonisme":
                return new float[]{
                        1,0,0,0,0,
                        0,1,0,0,0,
                        0,0,1,0,0,
                        0,0,0,1,0,
                        0,0,0,0,1};
            case "Deutéranomalie":
                return new float[]{
                        0.8f,0.2f,0,0,0,
                        0.258f,0.742f,0,0,0,
                        0,0.142f,0.858f,0,0,
                        0,0,0,1,0,
                        0,0,0,0,1};
            case "Deutéranopie":
                return new float[]{
                        0.625f,0.375f,0,0,0,
                        0.7f,0.3f,0,0,0,
                        0,0.3f,0.7f,0,0,
                        0,0,0,1,0,
                        0,0,0,0,1};
            case "Protanomalie":
                return new float[]{
                        0.817f,0.183f,0,0,0,
                        0.333f,0.667f,0,0,0,
                        0,0.125f,0.875f,0,0,
                        0,0,0,1,0,
                        0,0,0,0,1};
            case "Protanopie":
                return new float[]{
                        0.567f,0.433f,0,0,0,
                        0.558f,0.442f,0,0,0,
                        0,0.242f,0.758f,0,0,
                        0,0,0,1,0,
                        0,0,0,0,1};
            case "Trianomalie":
                return new float[]{
                        0.967f,0.033f,0,0,0,
                        0,0.733f,0.267f,0,0,
                        0,0.183f,0.817f,0,0,
                        0,0,0,1,0,
                        0,0,0,0,1};
            case "Trianopie":
                return new float[]{
                        0.95f,0.05f,0,0,0,
                        0,0.433f,0.567f,0,0,
                        0,0.475f,0.525f,0,0,
                        0,0,0,1,0,
                        0,0,0,0,1};
            case "Achromatopsie":
                return new float[]{
                        0.299f,0.587f,0.114f,0,0,
                        0.299f,0.587f,0.114f,0,0,
                        0.299f,0.587f,0.114f,0,0,
                        0,0,0,1,0,
                        0,0,0,0,1};
            case "Achromatomalie":
                return new float[]{
                        0.618f,0.320f,0.062f,0,0,
                        0.163f,0.775f,0.062f,0,0,
                        0.163f,0.320f,0.516f,0,0,
                        0,0,0,1,0,
                        0,0,0,0,1};
            default:
                return new float[]{
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0,
                        0,0,0,0,0};
        }
    }

    //Cette fonction convertit l'image original en image daltonizée.
    public void convertImage(Uri imageUri, String typeDeDaltonisme) throws IOException {

        Bitmap sourceBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

        float[] colorTransform = colorTransform(typeDeDaltonisme);

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f);
        colorMatrix.set(colorTransform);

        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);


        resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(),
                sourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(resultBitmap);

        canvas.drawBitmap(sourceBitmap, 0, 0, paint);

        result.setImageBitmap(resultBitmap);
    }

    //la fonction appelée lors de l'appuie du bouton "enregistrer"
    public void enregistrerImage(View view){
        if(isStoragePermissionGranted()){       //On test demande d'abord les permissions à l'utilisateur
            SaveImage(resultBitmap);        //On appel la fonction qui sauvegarde effectivement l'image.
        }
    }

    //Permet de demander à l'utilisateur des permission pour écrire dans la mémoire. Renvoie la réponse de l'utilisateur.
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

    //Cette fonction permet de sauvegarder le fichier
    private void SaveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();     //Permet de déterminer le chemin de dossier de stockage
        File myDir = new File(root + "/Daltonizator");      //On se place dans un sous-dossier propre à l'application
        myDir.mkdirs();

        //On génère un nombre aléatoire pour le nom du fichier.
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);

        String fname = "Image-"+ n +"-"+typeDeDaltonisme+"-"+ ".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            MediaScannerConnection.scanFile(this, new String[] { file.getPath() }, new String[] { "image/jpeg" }, null); //Cela permet d'ajouter directement l'image à la galerie.

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Fonction appelée lors de l'appuie du bouton "partager"
    public void share(View view){
        if (isStoragePermissionGranted()) {
            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpeg");
            String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), resultBitmap, "titre", null);
            Uri bmpUri = Uri.parse(pathofBmp);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            startActivity(Intent.createChooser(shareIntent, "Partager l'image avec"));
        }
    }
}
