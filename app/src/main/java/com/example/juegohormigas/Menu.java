package com.example.juegohormigas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Menu extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    Button btnJugar, btnPuntuacion, btnAcercaDe, btnCerrarSesion;

    TextView tvTitulo, tvUid, tvZombie, tvSubTitulo, tvCorreo, tvNombre;

    FirebaseDatabase database;
    DatabaseReference jugadores;

    ImageView imagen;

    private TextView txtPerfilMenu;
    private TextView txtFechaMenu;
    private TextView txtEdadMenu;
    private TextView txtPaisMenu;
    private ImageView imgEditar;
    private CircleImageView imgPerfil;
    private Button btnEditar;
    private Button btnCambiasPass;

    /* Para cambiar foto perfil*/

    private StorageReference referenciaAlmacenamiento;
    private String rutaAlmecamiento = "FotosDePerfil/*";

    /*Permisos*/
    private static final int CODIGO_SOLICITUD_ALMACENAMIENTO = 200;
    private static final int CODIGO_SELECCION_IMAGEN = 300;

    /*MATRICES*/
    private String[] permisosAlmacenamiento;
    private Uri imagenUri;
    private String perfil; // nombre column

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        tvTitulo = findViewById(R.id.txtTituloMenu);
        tvUid = findViewById(R.id.txtUidMenu);
        tvSubTitulo = findViewById(R.id.txtSubtituloBotones);
        tvCorreo = findViewById(R.id.txtCorreoJugadorMenu);
        tvNombre = findViewById(R.id.txtNombreMenu);
        tvZombie = findViewById(R.id.txtZombies);

        txtPerfilMenu = findViewById(R.id.txtTitlePerfil);
        txtEdadMenu = findViewById(R.id.txtEdadMenu);
        txtPaisMenu = findViewById(R.id.txtPaisMenu);
        txtFechaMenu = findViewById(R.id.txtFechaMenu);
        imgEditar = findViewById(R.id.imgEditar);
        imgPerfil = findViewById(R.id.imgPerfil);

        btnJugar = findViewById(R.id.btnJugar);
        btnPuntuacion = findViewById(R.id.btnPuntaciones);
        btnAcercaDe = findViewById(R.id.btnHacerca);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        imagen = findViewById(R.id.imageGif);

        btnEditar = findViewById(R.id.btnEditar);
        btnCambiasPass = findViewById(R.id.btnCambiarPass);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        jugadores = database.getReference("MI DATA BASE JUGADORES");

        btnJugar.setOnClickListener((event) -> {
            Intent intent = new Intent(Menu.this, EscenarioJuego.class);

            String nombre = tvNombre.getText() + "";
            String uId = tvUid.getText() + "";
            String email = tvCorreo.getText() + "";
            String zombies = tvZombie.getText() + "";

            intent.putExtra("uId", uId);
            intent.putExtra("nombres", nombre);
            intent.putExtra("email", email);
            intent.putExtra("zombie", zombies);
            startActivity(intent);
        });
        btnPuntuacion.setOnClickListener((event) -> {

            // startActivity( new Intent(this, Puntajes.class));
        });
        btnAcercaDe.setOnClickListener((event) -> {
            //openDialogAbouth();
        });

        btnEditar.setOnClickListener((event) -> {
            //editarDatos();
        });

        imgEditar.setOnClickListener((event) -> {

            //actualizarFoto();
        });

        btnCambiasPass.setOnClickListener((event) -> {
            //startActivity( new Intent(Menu.this, CambioPassword.class));
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

        txtPerfilMenu.setTypeface(fuentes);
        txtEdadMenu.setTypeface(fuentes);
        txtFechaMenu.setTypeface(fuentes);
        txtPaisMenu.setTypeface(fuentes);
        btnEditar.setTypeface(fuentes);
        btnCambiasPass.setTypeface(fuentes);


        btnCerrarSesion.setOnClickListener((event) -> cerrarSesion());

        String url = "https://www.gifsanimados.org/data/media/183/hormiga-imagen-animada-0064.gif";
        Uri urlParse = Uri.parse(url);
        Glide.with(getApplicationContext()).load(urlParse).into(imagen);

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


                    String edad = ds.child("Edad").getValue() + "";
                    String fecha = ds.child("Fecha").getValue() + "";
                    String pais = ds.child("Pais").getValue() + "";
                    String imagen = ds.child("Imagen").getValue() + "";

                    tvCorreo.setText(email);
                    tvNombre.setText(nombres);
                    tvZombie.setText(zombies);
                    tvUid.setText(uId);

                    txtEdadMenu.setText(edad);
                    txtPaisMenu.setText(pais);
                    txtFechaMenu.setText(fecha);

                    if (!imagen.equals("")) {
                        Picasso.get().load(imagen).into(imgPerfil);
                    } else Picasso.get().load(R.drawable.default_perfil).into(imgPerfil);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}