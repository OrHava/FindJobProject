package com.sce.findjobproject;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {
    private Button signOut;
    private GoogleSignInClient mGoogleSignInClient;
    //to scan websites you need to Parsing competitive websites for more jobs. using google api
    //Data of citys from: https://www.science.co.il/municipal/Cities.php
    //To find hash go to Navigate (its on top bar), Write-> search EveryWhere-> write "gradlew" and enter-> then write click on gradlew ->
    // open in-> Terminal -> then write in Terminal- > ./gradlew signingReport, then if everything is fine you should see hash 1 code which is what we need.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
      //  signOut=findViewById(R.id.SignOut);

      //  exitUser();

    }


   // void EnterButtons(){ //added by or, this function will be relevant when we will have the layouts for the profile and the about

       // btnProfile.setOnClickListener(view -> startActivity(new Intent(Home.this, Profile.class)));

      //  btnAbout.setOnClickListener(view -> startActivity(new Intent(Home.this, About.class)));

   // }



    void exitUser(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null || user != null){


            signOut.setOnClickListener(view -> {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.Google_id))
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(Home.this, gso);
                mGoogleSignInClient.signOut().addOnCompleteListener(Home.this,
                        task -> {
                            // firebase sign out
                            getInstance().signOut();

                            Intent intent = new Intent(Home.this, SignIn.class);
                            startActivity(intent);
                            finish();

                        });

            });

        }


    }

}