package com.example.daltonizator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Random;

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

        try {
            convertImage(imageUri,typeDeDaltonisme);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeActivity(View view){
        finish();
    }

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

    public void convertImage(Uri imageUri, String typeDeDaltonisme) throws IOException {

        Bitmap sourceBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);


        float[] colorTransform = colorTransform(typeDeDaltonisme);


        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f); //Remove Colour
        colorMatrix.set(colorTransform); //Apply the Red

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

    public void enregistrerImage(View view){
        if(isStoragePermissionGranted()){
            SaveImage(resultBitmap);
        }
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

    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Daltonizator");
        myDir.mkdirs();
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
            MediaScannerConnection.scanFile(this, new String[] { file.getPath() }, new String[] { "image/jpeg" }, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void share(View view){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), resultBitmap,"title", null);
        Uri bmpUri = Uri.parse(pathofBmp);
        sendIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        //sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        //sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("image/*");
        startActivity(sendIntent);
    }
}
