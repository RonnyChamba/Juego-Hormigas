package com.example.juegohormigas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    TextView txtTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        txtTitle = findViewById(R.id.txTitulo);

        Typeface typeface = Typeface.createFromAsset(Splash.this.getAssets(), "fuentes/zombie.TTF");
        txtTitle.setTypeface(typeface);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent  = new Intent(Splash.this, Menu.class);
                startActivity(intent);
            }
        }, 1500);
    }
}