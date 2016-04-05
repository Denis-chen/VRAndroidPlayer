package com.neointernet.neo360.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.neointernet.neo360.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Intent next = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(next);
                finish();
            }
        };
        h.postDelayed(r, 2000);
    }
}
