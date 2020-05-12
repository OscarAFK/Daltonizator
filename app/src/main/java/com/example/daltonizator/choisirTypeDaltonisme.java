package com.example.daltonizator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

public class choisirTypeDaltonisme extends AppCompatActivity {

    Uri imageUri;
    Spinner spinner;
    ImageView imageHUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choisir_type_daltonisme);

        spinner = findViewById(R.id.dropdownTypeDaltonisme);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                changeHUE();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        imageHUE = findViewById(R.id.hue);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            imageUri = Uri.parse(bundle.getString("imageUri"));
        }
    }

    public void closeActivity(View view){
        finish();
    }

    public void daltonizer(View view){
        Intent intent = new Intent(this,daltonizedResult.class);

        intent.putExtra("imageUri",imageUri.toString());
        intent.putExtra("valeurSpinner",spinner.getSelectedItem().toString());

        startActivity(intent);
    }

    private void changeHUE(){
        String typeDeDaltonisme = spinner.getSelectedItem().toString();
        switch (typeDeDaltonisme){
            case "Pas de daltonisme":
                imageHUE.setImageResource(R.drawable.full_hue);
                break;
            case "Deutéranomalie":
                imageHUE.setImageResource(R.drawable.full_hue_deuteranomalie);
                break;
            case "Deutéranopie":
                imageHUE.setImageResource(R.drawable.full_hue_deuteranopie);
                break;
            case "Protanomalie":
                imageHUE.setImageResource(R.drawable.full_hue_protanomalie);
                break;
            case "Protanopie":
                imageHUE.setImageResource(R.drawable.full_hue_protanopie);
                break;
            case "Trianomalie":
                imageHUE.setImageResource(R.drawable.full_hue_trianomalie);
                break;
            case "Trianopie":
                imageHUE.setImageResource(R.drawable.full_hue_trianopie);
                break;
            case "Achromatopsie":
                imageHUE.setImageResource(R.drawable.full_hue_achromatopsie);
                break;
            case "Achromatomalie":
                imageHUE.setImageResource(R.drawable.full_hue_achromatomalie);
                break;
            default:
                break;
        }
    }
}
