package com.neointernet.neo360.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.neointernet.neo360.R;

import java.io.File;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "360Videos");
        //folder 객체가 존재한다면
        if (folder.exists()) {

            //폴더 안에 파일들이 있다면
            if (folder.listFiles().length > 0) {
                Handler h = new Handler();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        Intent next = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(next);
                        finish();
                    }
                };
                h.postDelayed(r, 3000);

                //폴더 안에 파일들이 없다면
            }else{

                final AlertDialog ad = new AlertDialog.Builder(this).create();
                ad.setTitle("Files Not Found ");
                ad.setMessage("Please Copy Files to '360Videos' Folder in the SDCARD");
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setCancelable(false);
                ad.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ad.dismiss();
                        finish();
                    }
                });
                ad.show();

            }
            //폴더가없다면
        } else {
            Log.i("folder No", "There is no Folder!!!!!!!!!!!!");
            folder.mkdir();

            final AlertDialog ad = new AlertDialog.Builder(this).create();
            ad.setTitle("Folder not found");
            ad.setMessage("Please Copy files to '360Videos' folder in the SDCARD");
            ad.setIcon(R.mipmap.ic_launcher);
            ad.setCancelable(false);
            ad.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ad.dismiss();
                }
            });
            ad.show();
        }

    }



}
