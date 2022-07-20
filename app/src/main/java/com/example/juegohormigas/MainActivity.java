package com.example.juegohormigas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegistro = findViewById(R.id.btnRegistro);

        btnLogin.setOnClickListener( (event) -> {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        });

        btnRegistro.setOnClickListener( (event) -> {
            Intent intent = new Intent(this, Registro.class);
            startActivity(intent);
        });


        Typeface typeface = Typeface.createFromAsset(MainActivity.this.getAssets(), "fuentes/zombie.TTF");
        btnLogin.setTypeface(typeface);
        btnRegistro.setTypeface(typeface);

    }
}