package com.example.juegohormigas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    EditText tvEmail, tvPassword, tvNombre;
    TextView tvFecha, tvTitulo;
    Button btnRegistrar;
    FirebaseAuth auth;

    private TextView txtEdad;
    private Spinner txtPais;
    private ProgressDialog progressDialog;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        tvTitulo = findViewById(R.id.txtTituloRegistro);

        tvEmail = findViewById(R.id.txtCorreo);
        tvNombre = findViewById(R.id.txtNombre);
        tvPassword = findViewById(R.id.txtPassword);
        tvFecha = findViewById(R.id.txtFecha);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        txtEdad = findViewById(R.id.txtEdad);
        txtPais = findViewById(R.id.txtPais);

        auth = FirebaseAuth.getInstance();

        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat("d 'de' MMMM 'del'  yyyy");
        String stringFecha = fecha.format(date);
        tvFecha.setText(stringFecha);


        Typeface fuente = Typeface.createFromAsset(Registro.this.getAssets(), "fuentes/zombie.TTF");
        tvTitulo.setTypeface(fuente);
        tvEmail.setTypeface(fuente);
        tvPassword.setTypeface(fuente);
        tvNombre.setTypeface(fuente);
        tvFecha.setTypeface(fuente);
        btnRegistrar.setTypeface(fuente);

        btnRegistrar.setOnClickListener( (event)->{

            String email = tvEmail.getText().toString();
            String password = tvPassword.getText().toString();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tvEmail.setError("Email invalido");
                tvEmail.setFocusable(true);

            } else if (password.trim().length() < 6) {
                tvPassword.setError("Constraseña debe ser mayor a 6");
                tvPassword.setFocusable(true);
            }else {
                registrarJugador(email, password);
            }
        });

    }

    private void savePlayer() {

        progressDialog = new ProgressDialog(Registro.this);
        progressDialog.setTitle("Registrando Jugador, espere por favor");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
    private void registrarJugador(String email, String password){

        savePlayer();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    progressDialog.dismiss();
                    FirebaseUser user = auth.getCurrentUser();
                    int contador = 0;
                    assert  user !=null;
                    String uidString = user.getUid();
                    String nombreString = tvNombre.getText().toString();
                    String fechaString = tvFecha.getText().toString();
                    String emailString = tvEmail.getText().toString();
                    String passwordString = tvPassword.getText().toString();

                    String edad = txtEdad.getText().toString();
                    String pais = txtPais.getSelectedItem().toString();

                    Map<Object, Object> datosJugador = new HashMap<>();

                    datosJugador.put("Uid", uidString);
                    datosJugador.put("Email", emailString);
                    datosJugador.put("Password", passwordString);
                    datosJugador.put("Nombres", nombreString);
                    datosJugador.put("Fecha", fechaString);
                    datosJugador.put("Zombies", contador);

                    datosJugador.put("Edad", edad);
                    datosJugador.put("Imagen", "");
                    datosJugador.put("Pais", pais);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("MI DATA BASE JUGADORES");

                    reference.child(uidString).setValue(datosJugador);
                    startActivity(new Intent(Registro.this, Menu.class));

                    Toast.makeText(Registro.this, "Jugador creado", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(Registro.this, "Ha ocurrido un error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Registro.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}