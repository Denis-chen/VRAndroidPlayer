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
public class VideoJsonManager implements JsonManager {

    private String jsonData;

    private JSONArray jsonArray;
    private static final String TAG_RESULTS = "result";

    private static final String TAG = "VideoJsonManager";

    private ArrayList<HashMap<String, String>> jsonList;
    private ArrayList<Video> videoArrayList;
    private String attributeId = "video_id", attributeName = "video_filename", attributeSize = "video_size";

    public VideoJsonManager() {
        jsonList = new ArrayList<>();
        videoArrayList = new ArrayList<>();
    }

    @Override
    public void makeJsonData(String url, final AsyncTaskListener asyncTaskListener) {

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

            HashMap<String, String> jsonData = new HashMap<String, String>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                String id = c.getString(attributeId);
                String name = c.getString(attributeName);
                String size = c.getString(attributeSize);
                jsonData.put(attributeId, id);
                jsonData.put(attributeName, name);
                jsonData.put(attributeSize, size);
                jsonList.add(jsonData);
            }

            for (int i = 0; i < jsonList.size(); i++) {

                Long id = Long.valueOf(jsonList.get(i).get(attributeId));
                String name = jsonList.get(i).get(attributeName);
                String size = jsonList.get(i).get(attributeSize);
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
