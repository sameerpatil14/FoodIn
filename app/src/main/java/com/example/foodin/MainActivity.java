package com.example.foodin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int SPLASH_SCREEN = 2000;
    TextView foodin;
    ImageView logo;
    LinearLayout ll;
    Animation zoom_animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        foodin = findViewById(R.id.foodin);
        logo = findViewById(R.id.logo);
        ll = findViewById(R.id.ll);

        zoom_animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_animation);
        ll.setAnimation(zoom_animation);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(getApplicationContext(), SigninActivity.class);
            startActivity(i);
            finish();
        }, SPLASH_SCREEN);
    }
}