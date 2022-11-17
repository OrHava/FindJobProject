package com.sce.findjobproject;
import static com.google.firebase.auth.FirebaseAuth.getInstance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {
    private ImageButton btnProfile,btnAbout;
    //to scan websites you need to Parsing competitive websites for more jobs. using google api
    //Data of citys from: https://www.science.co.il/municipal/Cities.php
    //To find hash go to Navigate (its on top bar), Write-> search EveryWhere-> write "gradlew" and enter-> then write click on gradlew ->
    // open in-> Terminal -> then write in Terminal- > ./gradlew signingReport, then if everything is fine you should see hash 1 code which is what we need.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnProfile=findViewById(R.id.btnProfile);
        btnAbout=findViewById(R.id.btnAbout);

        EnterButtons();

    }



    void EnterButtons(){

        btnProfile.setOnClickListener(view -> startActivity(new Intent(Home.this, Profile.class)));

       // btnAbout.setOnClickListener(view -> startActivity(new Intent(Home.this, About.class)));

    }




}