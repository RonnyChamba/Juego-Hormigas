package com.example.juegohormigas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EscenarioJuego extends AppCompatActivity {

    String nombre, email, uId, zombie;
    TextView tvContador, tvNombre, tvTiempo;
    Random aleatorio;
    ImageView imgZombie;
    int anchoPantalla, altoPantalla;
    int contador = 0;
    boolean gameOver = true;
    Dialog miDialog;

    private FirebaseAuth auth;
    private FirebaseDatabase dataBase;
    private DatabaseReference dbReference;
    private FirebaseUser user;


    ImageView iniciarJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escenario_juego);

        tvContador = findViewById(R.id.txtContadorEsc);
        tvNombre = findViewById(R.id.txtNombreEs);
        tvTiempo = findViewById(R.id.txtTiempoEsc);
        imgZombie = findViewById(R.id.imgJuego);

        iniciarJuego = findViewById(R.id.img_iniciarJuego);

        initFirebase();
        Bundle intent = getIntent().getExtras();


        uId = intent.getString("uId");
        nombre = intent.getString("nombres");
        email = intent.getString("email");
        zombie = intent.getString("zombie");
        tvNombre.setText(nombre);
        tvContador.setText(zombie);
        miDialog = new Dialog(EscenarioJuego.this);
        Typeface fuente = Typeface.createFromAsset(EscenarioJuego.this.getAssets(), "fuentes/zombie.TTF");

        tvNombre.setTypeface(fuente);
        tvContador.setTypeface(fuente);
        tvTiempo.setTypeface(fuente);
        pantalla();

        imgZombie.setOnClickListener((event) -> {
            if (!gameOver) {
                contador++;
                tvContador.setText(String.valueOf(contador));
                imgZombie.setImageResource(R.drawable.foto_2);
                new Handler().postDelayed((() -> {
                    movimiento();
                    imgZombie.setImageResource(R.drawable.foto_1);
                }), 500);
            }
        });

        iniciarJuego.setOnClickListener( (event) ->{
            Toast.makeText(this, "Juego iniciado", Toast.LENGTH_SHORT).show();
            gameOver = false;
            cuentaAtras();

        });

    }

    private void  initFirebase(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        dataBase = FirebaseDatabase.getInstance();
        dbReference = dataBase.getReference(Constantes.NAME_BD);
    }

    private void cuentaAtras() {

        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long segundosRestantes = millisUntilFinished / 1000;
                tvTiempo.setText(segundosRestantes + " s");
            }

            @Override
            public void onFinish() {
                tvTiempo.setText("0s");
                gameOver = true;
                updateDataPlayer();
                dialogSms();

            }
        }.start();
    }


    private  void updateDataPlayer(){

        Map<String, Object> data =  new HashMap<>();
        data.put("Zombies", contador);
        dbReference.child(user.getUid()).updateChildren(data).addOnCompleteListener( (task) ->{
            Toast.makeText(this, "El puntaje ha sido actualizado correctamente", Toast.LENGTH_SHORT).show();
        });

    }
    private void pantalla() {

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        altoPantalla = point.y;
        anchoPantalla = point.x;
        aleatorio = new Random();
    }

    private void movimiento() {

        int min = 0;

        int maxX = anchoPantalla - imgZombie.getWidth();
        int maxY = anchoPantalla - imgZombie.getHeight();
        int randomX = aleatorio.nextInt(((maxX - min) + 1) + min);
        int randomY = aleatorio.nextInt(((maxY - min) + 1) + min);
        imgZombie.setX(randomX);
        imgZombie.setY(randomY);
    }
    private void dialogSms(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment  dialog = new DialogFragment(String.valueOf(contador));
        dialog.setCancelable(false);

        contador = 0;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.add(android.R.id.content, dialog)
                .addToBackStack(null).commit();
    }
}