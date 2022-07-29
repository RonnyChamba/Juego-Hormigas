package com.example.juegohormigas;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetalleJugador extends AppCompatActivity  implements View.OnClickListener {

    private CircleImageView imgPerfil;
    private TextView txtTituloDetalle;
    private TextView txtNombreDetalle;
    private TextView txtEmailDetalle;
    private TextView txtZombieDetalle;
    private TextView txtFechaDetalle;
    private TextView txtEdadDetalle;
    private TextView txtPaisDetalle;
    private Button btnHomeDetalle;
    private Jugador jugador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detalle_jugador);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Detalle Jugador");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        initApp();
    }

    private void initApp() {
        findByWidget();
        setTypeFont();
        setDataPlayer();
    }
    private void findByWidget(){

        imgPerfil = findViewById(R.id.imgPerfilDetalle);
        txtTituloDetalle = findViewById(R.id.txtTituloDetalle);
        txtNombreDetalle = findViewById(R.id.txtNombreDetalle);
        txtEmailDetalle = findViewById(R.id.txtEmailDetalle);
        txtEdadDetalle = findViewById(R.id.txtEdadDetalle);
        txtFechaDetalle = findViewById(R.id.txtFechaDetalle);
        txtPaisDetalle = findViewById(R.id.txtPaisDetalle);
        txtZombieDetalle = findViewById(R.id.txtZombieDetalle);
        btnHomeDetalle = findViewById(R.id.btnHomeDetalle);


        btnHomeDetalle.setOnClickListener(this);
    }

    private void setTypeFont() {

        Typeface typeface = Typeface.createFromAsset(this.getApplicationContext().getAssets(), "fuentes/zombie.TTF");
        txtTituloDetalle.setTypeface(typeface);
        txtNombreDetalle.setTypeface(typeface);
        txtEmailDetalle.setTypeface(typeface);
        txtEdadDetalle.setTypeface(typeface);
        txtFechaDetalle.setTypeface(typeface);
        txtZombieDetalle.setTypeface(typeface);
        txtPaisDetalle.setTypeface(typeface);
        btnHomeDetalle.setTypeface(typeface);
    }

    private void setDataPlayer(){
        Bundle bundle = getIntent().getExtras();
        jugador = (Jugador) bundle.get("jugadorClick");
        txtNombreDetalle.setText(jugador.getNombres());
        txtEmailDetalle.setText(jugador.getEmail());
        txtEdadDetalle.setText(jugador.getEdad());
        txtPaisDetalle.setText(jugador.getPais());
        txtZombieDetalle.setText(String.valueOf(jugador.getPuntaje()));
        txtFechaDetalle.setText(jugador.getFecha());

        if (!jugador.getImagen().equals("")){
            Picasso.get().load(jugador.getImagen()).into(imgPerfil);
        }else Picasso.get().load(R.drawable.default_perfil).into(imgPerfil);
    }
    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnHomeDetalle.getId()){
            startActivity( new Intent( this, Menu.class));
        }

    }
}