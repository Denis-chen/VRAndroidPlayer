package com.neointernet.onvr.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.neointernet.onvr.listener.AsyncTaskListener;
import com.neointernet.onvr.model.Video;

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
public class VideoJsonManager {

    private String jsonData;

    private JSONArray jsonArray;
    private static final String TAG_RESULTS = "result";

    private static final String TAG = "VideoJsonManager";

    private ArrayList<HashMap<String, String>> jsonList;
    private ArrayList<Video> videoArrayList;
    private String attId = "video_id", attTitle = "video_title", attDes = "video_des", attType = "video_type",
            attFilename = "video_filename", attThumb = "video_thumbnail", attDuration = "video_duration",
            attSize = "video_size", attHits = "video_hits", attDate = "video_date", attValid = "video_valid", attMemNickName = "mem_nickname";
    private AsyncTaskListener asyncTaskListener;
    private ProgressDialog progressDialog;

    public VideoJsonManager(AsyncTaskListener asyncTaskListener, Context context) {
        this.asyncTaskListener = asyncTaskListener;
        jsonList = new ArrayList<>();
        videoArrayList = new ArrayList<>();
        progressDialog = new ProgressDialog(context);
    }

    public void makeJsonData(final int start, final int quantity) {

        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Loading");
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                String uri = "http://lifejeju99.cafe24.com/video_list.php?start=" + start + "&quantitiy=" + quantity;

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
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        }

        GetDataJSON getDataJSON = new GetDataJSON();
        getDataJSON.execute();
    }

    private void makeCollectionJson() {
        try {
            JSONObject jsonObj = new JSONObject(jsonData);
            jsonArray = jsonObj.getJSONArray(TAG_RESULTS);


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                HashMap<String, String> jsonData = new HashMap<String, String>();

                jsonData.put(attId, c.getString(attId));
                jsonData.put(attTitle, c.getString(attTitle));
                jsonData.put(attDes, c.getString(attDes));
                jsonData.put(attType, c.getString(attType));
                jsonData.put(attFilename, c.getString(attFilename));
                jsonData.put(attThumb, c.getString(attThumb));
                jsonData.put(attDuration, c.getString(attDuration));
                jsonData.put(attSize, c.getString(attSize));
                jsonData.put(attHits, c.getString(attHits));
                jsonData.put(attDate, c.getString(attDate));
                jsonData.put(attValid, c.getString(attValid));
                jsonData.put(attMemNickName, c.getString(attMemNickName));
                Log.d(TAG, i + "번째 : " + c.getString(attTitle));
                Log.d(TAG, i + "번째 : " + c.getString(attType));
                Log.d(TAG, i + "번째 : " + c.getString(attFilename));
                jsonList.add(jsonData);
            }

            for (int i = 0; i < jsonList.size(); i++) {

                Video video = new Video();
                video.setVideoId(Long.valueOf(jsonList.get(i).get(attId)));
                video.setVideoTitle(jsonList.get(i).get(attTitle));
                video.setVideoDes(jsonList.get(i).get(attDes));
                video.setVideoType(jsonList.get(i).get(attType));
                video.setVideoFilename(jsonList.get(i).get(attFilename));
                video.setVideoThumbnail(jsonList.get(i).get(attThumb));
                video.setVideoDuration(Double.valueOf(jsonList.get(i).get(attDuration)));
                video.setVideoSize(jsonList.get(i).get(attSize));
                video.setVideoHits(Integer.valueOf(jsonList.get(i).get(attHits)));
                video.setVideoDate(jsonList.get(i).get(attDate));
                video.setVideoValid(Integer.valueOf(jsonList.get(i).get(attValid)));
                video.setMemId(jsonList.get(i).get(attMemNickName));
                Log.d(TAG, i + "번째 : " + jsonList.get(i).get(attTitle));
                Log.d(TAG, i + "번째 : " + jsonList.get(i).get(attType));
                Log.d(TAG, i + "번째 : " + jsonList.get(i).get(attFilename));
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
