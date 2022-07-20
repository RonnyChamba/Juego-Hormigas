package com.example.juegohormigas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText tvEmail, tvPass;
    TextView tvTitulo;
    Button btnLogin;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvTitulo = findViewById(R.id.txtTituloLogin);
        tvEmail = findViewById(R.id.emailLogin);
        tvPass = findViewById(R.id.passLogin);
        btnLogin = findViewById(R.id.btnIngresarLogin);


        auth = FirebaseAuth.getInstance();

        Typeface fuente = Typeface.createFromAsset(Login.this.getAssets(), "fuentes/zombie.TTF");
        tvTitulo.setTypeface(fuente);
        btnLogin.setTypeface(fuente);
        tvPass.setTypeface(fuente);
        tvEmail.setTypeface(fuente);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = tvEmail.getText().toString();
                String password = tvPass.getText().toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    tvEmail.setError("Email invalido");
                    tvEmail.setFocusable(true);

                } else if (password.trim().length() < 6) {
                    tvPass.setError("Contraseña debe ser mayor a 6");
                    tvPass.setFocusable(true);
                } else {
                    LogeoDeJugador(email, password);
                }
            }
        });
    }
    private void LogeoDeJugador(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                    startActivity(new Intent(Login.this, Menu.class));
                    assert  user !=null;
                    Toast.makeText(Login.this, "BIENVENIDO(A) "+ user.getEmail(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Jugador no registrado", Toast.LENGTH_LONG).show();
            }
        });
    }
}