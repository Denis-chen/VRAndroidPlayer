package com.neointernet.neo360.util;

import android.os.AsyncTask;
import android.util.Log;

import com.neointernet.neo360.listener.AsyncTaskListener;
import com.neointernet.neo360.model.Video;

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

/**
 * Created by HSH on 16. 5. 4..
 */
public class GetJsonManager {

    private String jsonData;

    private JSONArray jsonArray;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_SIZE = "size";

    private static final String TAG = "GetJsonManager";

    private ArrayList<HashMap<String, String>> jsonList;
    private AsyncTaskListener asyncTaskListener;
    private ArrayList<Video> videoArrayList;

    public GetJsonManager(AsyncTaskListener asyncTaskListener) {
        jsonList = new ArrayList<>();
        videoArrayList = new ArrayList<>();
        this.asyncTaskListener = asyncTaskListener;
    }

    public void getJSONData(String url) {

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
                    java.net.URL url = new URL(uri);
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
                makeCollectionJson();
                asyncTaskListener.asynkTaskFinished();
            }
        }

        GetDataJSON getDataJSON = new GetDataJSON();
        getDataJSON.execute(url);
    }

    private void makeCollectionJson() {
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

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Video> getVideoArrayList() {
        return videoArrayList;
    }
}
