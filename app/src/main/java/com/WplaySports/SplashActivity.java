package com.WplaySports;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    private ImageView imageViewLogo;
    private Animation fadeIn,fadeOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.arctivity_splash);
        AddView();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goHome();
            }
        }, 3000);
    }

    private void AddView() {
        fadeIn=  AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        fadeOut=  AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);

        imageViewLogo= findViewById(R.id.image_logo);
        imageViewLogo.setAnimation(fadeIn);
    }

    private void goHome(){
        startActivity(new Intent(this,MainActivity.class));
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        finish();
    }
}
