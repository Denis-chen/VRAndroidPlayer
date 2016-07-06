package com.neointernet.onvr.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.neointernet.onvr.fragment.OnListFragmentInteractionListener;
import com.neointernet.onvr.model.Video;
import com.neointernet.onvr.util.MyDownloadManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class BaseActivity extends AppCompatActivity implements OnListFragmentInteractionListener {


    private MyDownloadManager myDownloadManager;
    private final static String URL = "http://lifejeju99.cafe24.com/videos/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDownloadManager = new MyDownloadManager(this);
    }

    @Override
    public void onListFragmentInteraction(Video item) {
        Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
        String path;
        if (myDownloadManager.checkExistFile(item.getVideoFilename()))
            path = myDownloadManager.getStoragePath();
        else
            path = URL;
        intent.putExtra("videopath", path + item.getVideoFilename());
        intent.putExtra("videoTitle", item.getVideoTitle());
        sendHits("http://lifejeju99.cafe24.com/video_hit_increment.php", item.getVideoId());
        startActivity(intent);
    }


    private void sendHits(String uri, long videoId) {

        class SendData extends AsyncTask<String, Void, String> {

            private String TAG = "SendDataClass";

            @Override
            protected String doInBackground(String... strings) {

                String uri = strings[0];
                String videoId = strings[1];

                HttpURLConnection conn = null;
                try {
                    java.net.URL url = new java.net.URL(uri + "?video_id=" + videoId);
                    conn = (HttpURLConnection) url.openConnection();

                    int responseCode = conn.getResponseCode();
                    Log.d(TAG, "Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    Log.d(TAG, response.toString());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null)
                        try {
                            conn.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
                return null;
            }
        }

        SendData sendData = new SendData();
        sendData.execute(uri, String.valueOf(videoId));

    }


}
