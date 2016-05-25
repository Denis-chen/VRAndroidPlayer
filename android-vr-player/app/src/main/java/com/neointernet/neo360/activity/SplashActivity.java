//package com.neointernet.neo360.activity;
//
//import android.Manifest;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.neointernet.neo360.R;
//import com.neointernet.neo360.util.MyDownloadManager;
//
//import java.io.File;
//
//public class SplashActivity extends AppCompatActivity {
//
//    private static final String TAG = "SplashActivity";
//    private static final int MY_PERMISSION_REQUEST_STORAGE = 100;
//    private String PATH_TAG = "filepathLog";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//        checkPermission();
//    }
//
//    private void writeFile() {
//        String path = MyDownloadManager.getStoragePath();
//        File folder = new File(path);
//        Log.i(PATH_TAG, "SPLASH Activity : " + path);
//        //folder 객체가 존재한다면
//        if (folder.exists()) {
//
//            //폴더 안에 파일들이 있다면
//            if (folder.listFiles().length > 0) {
//                Handler h = new Handler();
//                Runnable r =    new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent next = new Intent(SplashActivity.this, MainActivity.class);
//                        startActivity(next);
//                        finish();
//                    }
//                };
//                h.postDelayed(r, 3000);
//
//                //폴더 안에 파일들이 없다면
//            } else {
//
//                final AlertDialog ad = new AlertDialog.Builder(this).create();
//                ad.setTitle("Files Not Found ");
//                ad.setMessage("Please Copy Files to '360Videos' Folder in the SDCARD");
//                ad.setIcon(R.mipmap.ic_launcher);
//                ad.setCancelable(false);
//                ad.setButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        ad.dismiss();
//                        finish();
//                    }
//                });
//                ad.show();
//
//            }
//            //폴더가없다면
//        } else {
//            Log.i("folder No", "There is no Folder!!!!!!!!!!!!");
//            Log.i(TAG, folder.toString());
//            folder.mkdir();
//
//            final AlertDialog ad = new AlertDialog.Builder(this).create();
//            ad.setTitle("Folder not found");
//            ad.setMessage("Please Copy files to '360Videos' folder in the SDCARD");
//            ad.setIcon(R.mipmap.ic_launcher);
//            ad.setCancelable(false);
//            ad.setButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    ad.dismiss();
//                }
//            });
//            ad.show();
//        }
//    }
//
//    private void checkPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED
//                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//
//                // Should we show an explanation?
//                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    // Explain to the user why we need to write the permission.
//                    Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
//                }
//
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        MY_PERMISSION_REQUEST_STORAGE);
//
//                // MY_PERMISSION_REQUEST_STORAGE is an
//                // app-defined int constant
//
//            }
//        }
//        writeFile();
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSION_REQUEST_STORAGE:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
//                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//
//                    writeFile();
//
//                } else {
//
//                    Log.d(TAG, "Permission always deny");
//                }
//                break;
//        }
//    }
//
//
//}
