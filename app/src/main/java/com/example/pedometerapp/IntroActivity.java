package com.example.pedometerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class IntroActivity extends AppCompatActivity {
    Handler handler = new Handler();

    Runnable r = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    ImageView image_logo;
    ImageView image_logo2;
    Animation alpha;
    Animation alpha_revers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        image_logo = findViewById(R.id.image_logo);
        image_logo2 = findViewById(R.id.image_logo2);

        alpha = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
        alpha_revers = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_revers);

        image_logo.startAnimation(alpha);
        image_logo2.startAnimation(alpha_revers);

        handler.postDelayed(r, 1500);
    }

}
