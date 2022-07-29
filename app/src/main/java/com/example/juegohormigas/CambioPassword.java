package com.example.juegohormigas;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CambioPassword extends AppCompatActivity  implements View.OnClickListener{

    private TextView txtTitulo;
    private EditText txtNuevaPass;
    private EditText txtActualPass;
    private Button btnCambiar;

    private DatabaseReference dbReference;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_password);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Regresar Menu");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        initApp();
    }


    private void initApp() {
        findByWidget();
        setTypeFont();
        initFirebase();

    }

    private void findByWidget() {

        txtTitulo = findViewById(R.id.txtTitleCambio);
        txtActualPass = findViewById(R.id.txtPassdActualCambio);
        txtNuevaPass = findViewById(R.id.txtPassdNuevaCambio);
        btnCambiar = findViewById(R.id.btnCambiarPass);
        btnCambiar.setOnClickListener(this);

    }

    private void setTypeFont() {

        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fuentes/zombie.TTF");

        txtTitulo.setTypeface(typeface);
        txtNuevaPass.setTypeface(typeface);
        txtActualPass.setTypeface(typeface);
        btnCambiar.setTypeface(typeface);

    }

    private void initFirebase() {

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference(Constantes.NAME_BD);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == btnCambiar.getId()) changePassword();
    }

    private void changePassword() {

        String currentPass = txtActualPass.getText().toString();
        String newPass = txtNuevaPass.getText().toString();

        if (validPass(currentPass, newPass)) {
            saveNewPass(currentPass, newPass);
        }
    }
    private boolean validPass(String currentPass, String newPass) {

        if (currentPass.isEmpty()) {
            txtActualPass.setError("Ingrese su contraseña");
            txtActualPass.setFocusable(true);
            return false;
        }

        if (newPass.isEmpty()) {
            txtNuevaPass.setError("Ingrese su nueva contraseña");
            txtNuevaPass.setFocusable(true);
            return false;
        }

        if (currentPass.length() < 6) {
            txtActualPass.setError("Ingrese minimo 6 caracteres");
            txtActualPass.setFocusable(true);
            return false;
        }
        if (newPass.length() < 6) {
            txtNuevaPass.setError("Ingrese minimo 6 caracteres");
            txtNuevaPass.setFocusable(true);
            return false;
        }


        return true;
    }

    private void saveNewPass(String currentPass, String newPass) {


        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), currentPass);

        user.reauthenticate(authCredential)
                .addOnSuccessListener((unused) -> {
                    user.updatePassword(newPass)
                            .addOnSuccessListener((ok) -> {
                                String value = txtNuevaPass.getText().toString().trim();
                                Map<String, Object> result = new HashMap<>();
                                result.put("Password", value);
                                dbReference.child(user.getUid()).updateChildren(result)
                                        .addOnSuccessListener((okUpdate) -> {
                                            Toast.makeText(CambioPassword.this, "Constraseña actualizada con exito", Toast.LENGTH_SHORT).show();
                                        }).addOnFailureListener((errorUpdate) -> {
                                    Toast.makeText(CambioPassword.this, "Error al actualizar con exito " + errorUpdate.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                                auth.signOut();
                                startActivity(new Intent(CambioPassword.this, Login.class));
                                finish();
                            }).addOnFailureListener((error) -> {
                        Toast.makeText(CambioPassword.this, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }).addOnFailureListener((exception) -> {
            Toast.makeText(CambioPassword.this, "Contraseña actual incorrecta", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}