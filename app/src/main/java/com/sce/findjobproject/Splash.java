package com.sce.findjobproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class Splash extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    TextView textBottom,textTop;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        textBottom=findViewById(R.id.bottomTxt);
        textTop=findViewById(R.id.topTxt);
        lottieAnimationView=findViewById(R.id.lottie);



        lottieAnimationView.animate().translationY(-1600).setDuration(2000).setStartDelay(2500);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(() -> {
            Intent intent= new Intent(Splash.this,SignIn.class);
            startActivity(intent);
            finish();

        },2500);
    }
}