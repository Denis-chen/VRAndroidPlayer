package com.neointernet.neo360.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.neointernet.neo360.R;
import com.neointernet.neo360.adapter.StreamingVideoFileAdapter;
import com.neointernet.neo360.model.Video;
import com.neointernet.neo360.util.MyDownloadManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class StreamingActivity extends AppCompatActivity implements View.OnClickListener {

    private StreamingVideoFileAdapter adapter;
    private ArrayList<Video> videoArrayList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private ProgressDialog loadingProgressDialog;


    private static final String jsonURL = "http://lifejeju99.cafe24.com/test2.php";
    private static final String URL = "http://lifejeju99.cafe24.com/";

    private String jsonData;
    private JSONArray jsonArray;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_SIZE = "size";

    private ArrayList<HashMap<String, String>> jsonList;
    private static final String TAG = "StreamingActivity";

    private MyDownloadManager myDownloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);
        myDownloadManager = new MyDownloadManager(getApplicationContext());
        loadingProgressDialog = ProgressDialog.show(StreamingActivity.this, "", "잠시만 기다려주세요.", true);
        jsonList = new ArrayList<HashMap<String, String>>();
        videoArrayList = new ArrayList<>();
        getJSONData(jsonURL);
    }

    private void getJSONData(String url) {

        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bufferedReader != null)
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
                return null;

            }

            @Override
            protected void onPostExecute(String result) {
                jsonData = result;
                showList();

            }
        }

        GetDataJSON getDataJSON = new GetDataJSON();
        getDataJSON.execute(url);
    }

    private void showList() {
        try {
            JSONObject jsonObj = new JSONObject(jsonData);
            jsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);
                String size = c.getString(TAG_SIZE);

                HashMap<String, String> jsonData = new HashMap<String, String>();

                jsonData.put(TAG_ID, id);
                jsonData.put(TAG_NAME, name);
                jsonData.put(TAG_SIZE, size);

                jsonList.add(jsonData);
            }

            for (int i = 0; i < jsonList.size(); i++) {
                Long id = Long.valueOf(jsonList.get(i).get(TAG_ID));
                String name = jsonList.get(i).get(TAG_NAME);
                String size = jsonList.get(i).get(TAG_SIZE);
                Log.i(TAG, "videoArrayList ADD id : " + id + " name : " + name + " size : " + size);
                Video video = new Video();
                video.setId(id);
                video.setName(name);
                video.setSize(size);

                videoArrayList.add(video);
            }
            adapter = new StreamingVideoFileAdapter(this, videoArrayList);
            recyclerView = (RecyclerView) findViewById(R.id.video_recyler_view);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            loadingProgressDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        Video video = (Video) view.getTag();
        Intent intent = new Intent(StreamingActivity.this, VideoActivity.class);
        String path;
        if (myDownloadManager.checkExistFile(video.getName()))
            path = myDownloadManager.getStoragePath();
        else
            path = URL;
        intent.putExtra("videopath", path + video.getName());
        startActivity(intent);
    }

}
