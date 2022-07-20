package com.example.juegohormigas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Menu extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    Button btnJugar, btnPuntuacion, btnAcercaDe, btnCerrarSesion;

    TextView tvTitulo , tvUid, tvZombie, tvSubTitulo, tvCorreo, tvNombre;

    FirebaseDatabase database;
    DatabaseReference jugadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        tvTitulo = findViewById(R.id.txtTituloMenu);
        tvUid = findViewById(R.id.txtUidMenu);
        tvSubTitulo = findViewById(R.id.txtSubtituloBotones);
        tvCorreo = findViewById(R.id.txtCorreoMenu);
        tvNombre = findViewById(R.id.txtNombreMenu);
        tvZombie = findViewById(R.id.txtZombies);

        btnJugar = findViewById(R.id.btnJugar);
        btnPuntuacion = findViewById(R.id.btnPuntaciones);
        btnAcercaDe = findViewById(R.id.btnHacerca);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        jugadores = database.getReference("MI DATA BASE JUGADORES");

        btnJugar.setOnClickListener((event) -> {
            Intent intent = new Intent(Menu.this, EscenarioJuego.class);

            String nombre = tvNombre.getText() +"";
            String uId = tvUid.getText() +"";
            String email = tvCorreo.getText() +"";
            String zombies = tvZombie.getText() +"";

            intent.putExtra("uId", uId);
            intent.putExtra("nombres", nombre);
            intent.putExtra("email", email);
            intent.putExtra("zombie", zombies);
            startActivity(intent);
        });
        btnPuntuacion.setOnClickListener((event) -> {
        });
        btnAcercaDe.setOnClickListener((event) -> {
        });

        Typeface fuentes = Typeface.createFromAsset(Menu.this.getAssets(), "fuentes/zombie.TTF");

        tvTitulo.setTypeface(fuentes);
        tvZombie.setTypeface(fuentes);
        tvSubTitulo.setTypeface(fuentes);
        tvCorreo.setTypeface(fuentes);
        tvNombre.setTypeface(fuentes);
        btnJugar.setTypeface(fuentes);
        btnPuntuacion.setTypeface(fuentes);
        btnAcercaDe.setTypeface(fuentes);
        btnCerrarSesion.setTypeface(fuentes);


        btnCerrarSesion.setOnClickListener((event) -> cerrarSesion());
    }
    @Override
    protected void onStart() {
        usuarioLogeado();
        super.onStart();
    }

    private void usuarioLogeado() {

        if (user != null) {
            consulta();
            Toast.makeText(this, "Jugador en linea", Toast.LENGTH_LONG).show();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void cerrarSesion() {
        auth.signOut();
        startActivity(new Intent(Menu.this, MainActivity.class));
        Toast.makeText(this, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show();

    }

    private void consulta() {
        Query query = jugadores.orderByChild("Email").equalTo(user.getEmail());

        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    String email = ds.child("Email").getValue() + "";
                    String uId = ds.child("Uid").getValue() + "";
                    String nombres = ds.child("Nombres").getValue() + "";
                    String zombies = ds.child("Zombies").getValue() + "";
                    tvCorreo.setText(email);
                    tvNombre.setText(nombres);
                    tvZombie.setText(zombies);
                    tvUid.setText(uId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}