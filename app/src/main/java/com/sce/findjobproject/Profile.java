package com.sce.findjobproject;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {
    private ImageView signOut;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
         signOut=findViewById(R.id.profile_exit_user);
          exitUser();


    }


    void exitUser(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null || user != null){


            signOut.setOnClickListener(view -> {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.Google_id))
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(Profile.this, gso);
                mGoogleSignInClient.signOut().addOnCompleteListener(Profile.this,
                        task -> {
                            // firebase sign out
                            getInstance().signOut();

                            Intent intent = new Intent(Profile.this, SignIn.class);
                            startActivity(intent);
                            finish();

                        });

            });

        }


    }
}