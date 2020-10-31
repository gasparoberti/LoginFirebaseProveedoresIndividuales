package com.example.loginprovidersindividuales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Auth extends AppCompatActivity {
    EditText editTextCorreo, editTextContrasena;
    Button buttonIniciarSesion, buttonRegistrarse;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextCorreo = findViewById(R.id.editTextCorreo);
        editTextContrasena = findViewById(R.id.editTextContrasena);

        buttonRegistrarse = findViewById(R.id.buttonRegistrarse);
        buttonIniciarSesion = findViewById(R.id.buttonIniciarSesion);

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(Auth.this, MainActivity.class));
            finish();
        }
        else {
            setup();
        }
    }

    private void setup() {
        buttonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextCorreo.getText().toString().isEmpty()) {
                    editTextCorreo.setError("Complete el email.");
                    editTextCorreo.requestFocus();
                }
                else if (editTextContrasena.getText().toString().isEmpty()) {
                    editTextContrasena.setError("Complete la contraseña.");
                    editTextContrasena.requestFocus();
                }
                else if (editTextCorreo.getText().toString().isEmpty() && editTextContrasena.getText().toString().isEmpty()) {
                    Toast.makeText(Auth.this, "Complete email y contraseña.", Toast.LENGTH_LONG).show();
                }
                else if (!(editTextCorreo.getText().toString().isEmpty() && editTextContrasena.getText().toString().isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(editTextCorreo.getText().toString(), editTextContrasena.getText().toString())
                            .addOnCompleteListener(Auth.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()) {
                                        Toast.makeText(Auth.this, "Intento de registro incorrecto. Intente de nuevo.", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        startActivity(new Intent(Auth.this, MainActivity.class));
                                        finish();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(Auth.this, "Ha ocurrido un error al querer registrarse.", Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextCorreo.getText().toString().isEmpty()) {
                    editTextCorreo.setError("Complete el email.");
                    editTextCorreo.requestFocus();
                } else if (editTextContrasena.getText().toString().isEmpty()) {
                    editTextContrasena.setError("Complete la contraseña.");
                    editTextContrasena.requestFocus();
                } else if (editTextCorreo.getText().toString().isEmpty() && editTextContrasena.getText().toString().isEmpty()) {
                    Toast.makeText(Auth.this, "Complete email y contraseña.", Toast.LENGTH_SHORT).show();
                } else if (!(editTextCorreo.getText().toString().isEmpty() && editTextContrasena.getText().toString().isEmpty())) {
                    firebaseAuth.signInWithEmailAndPassword(editTextCorreo.getText().toString(), editTextContrasena.getText().toString())
                            .addOnCompleteListener(Auth.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()) {
                                        Toast.makeText(Auth.this, "Intento de registro incorrecto. Intente de nuevo.", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        startActivity(new Intent(Auth.this, MainActivity.class));
                                        finish();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(Auth.this, "Ha ocurrido un error al querer registrarse.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

