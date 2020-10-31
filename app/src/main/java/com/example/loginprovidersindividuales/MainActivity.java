package com.example.loginprovidersindividuales;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends AppCompatActivity {
    Button buttonCerrarSesion;
    FirebaseAuth firebaseAuth;

    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();

                userInfo = profile;

                Toast.makeText(this, userInfo.getProviderId(), Toast.LENGTH_SHORT).show();
            }
        }

        buttonCerrarSesion = findViewById(R.id.buttonCerrarSesion);
        buttonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfo.getProviderId() == "facebook.com") {
                    LoginManager.getInstance().logOut();
                }

                firebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, Auth.class));
                finish();
            }
        });
    }
}
