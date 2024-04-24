package com.adanitownship.driver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.adanitownship.driver.utils.PreferenceManager;


public class SplashScreen extends AppCompatActivity {


    LinearLayout lin_GetStared;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        lin_GetStared = findViewById(R.id.lin_GetStared);
        preferenceManager = new PreferenceManager(SplashScreen.this);
        Animation lefttori = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_to_right);
        lin_GetStared.startAnimation(lefttori);


        lin_GetStared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(preferenceManager.getLoginSession()){
                    Intent intent = new Intent(SplashScreen.this, DashBoardActivity.class);
                    startActivity(intent);

                }else {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        });



    }
}