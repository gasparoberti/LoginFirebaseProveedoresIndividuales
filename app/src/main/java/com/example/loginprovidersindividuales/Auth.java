package com.example.loginprovidersindividuales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Auth extends AppCompatActivity {
    EditText editTextCorreo, editTextContrasena;
    Button buttonIniciarSesion, buttonRegistrarse, buttonGoogle, buttonFacebook;
    FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager = CallbackManager.Factory.create();

    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextCorreo = findViewById(R.id.editTextCorreo);
        editTextContrasena = findViewById(R.id.editTextContrasena);

        buttonRegistrarse = findViewById(R.id.buttonRegistrarse);
        buttonIniciarSesion = findViewById(R.id.buttonIniciarSesion);
        buttonGoogle = findViewById(R.id.buttonGoogle);
        buttonFacebook = findViewById(R.id.buttonFacebook);

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

        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                googleSignInClient = GoogleSignIn.getClient(Auth.this, gso);

                googleSignInClient.signOut();
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 100);


            }
        });

        buttonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(Auth.this, Arrays.asList("email"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("facebook login", "facebook:onSuccess:" + loginResult);
                        firebaseAuthWithFacebook(loginResult.getAccessToken());

                    }

                    @Override
                    public void onCancel() {
                        Log.d("facebook login", "facebook:onCancel");
                        // ...
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("facebook login", "facebook:onError", error);
                        // ...
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("onActivityResult", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("onActivityResult", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AuthWithGoogle", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AuthWithGoogle", "signInWithCredential:failure", task.getException());
                            Toast.makeText(Auth.this, "Authentication Failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void firebaseAuthWithFacebook(AccessToken idToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(idToken.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AuthWithFacebook", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AuthWithFacebook", "signInWithCredential:failure", task.getException());
                            Toast.makeText(Auth.this, "Authentication Failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        startActivity(new Intent(Auth.this, MainActivity.class));
        finish();
    }
}

