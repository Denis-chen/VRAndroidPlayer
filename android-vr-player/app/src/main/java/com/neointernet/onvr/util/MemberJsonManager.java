package com.neointernet.onvr.util;

import android.os.AsyncTask;
import android.util.Log;

import com.neointernet.onvr.listener.AsyncTaskListener;
import com.neointernet.onvr.model.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by HSH on 16. 5. 24..
 */
public class MemberJsonManager implements JsonManager {

    private String jsonData;

    private JSONArray jsonArray;
    private static final String TAG_RESULTS = "result";

    private static final String TAG = "MemberJsonManager";

    private ArrayList<HashMap<String, String>> jsonList;
    private ArrayList<Member> memberArrayList;

    private String mem_id, mem_nickname;
    private String attributeMem_id = "mem_id", attributeMem_nickname = "mem_nickname";

    public MemberJsonManager() {
        jsonList = new ArrayList<>();
        memberArrayList = new ArrayList<>();
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
                HttpURLConnection con = null;
                try {
                    java.net.URL url = new URL(uri);
                    con = (HttpURLConnection) url.openConnection();
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
                    if (con != null)
                        try {
                            con.disconnect();
                        } catch (Exception e) {
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
                String id = c.getString(attributeMem_id);
                String nickname = c.getString(attributeMem_nickname);
                jsonData.put(attributeMem_id, id);
                jsonData.put(attributeMem_nickname, nickname);
                jsonList.add(jsonData);
            }

            for (int i = 0; i < jsonList.size(); i++) {
                mem_id = jsonList.get(i).get(attributeMem_id);
                mem_nickname = jsonList.get(i).get(attributeMem_nickname);
                Member member = new Member();
                member.setMemId(mem_id);
                member.setMemNickname(mem_nickname);
                Log.i(TAG, "memberArrayList ADD id : " + mem_id + " Nick Name : " + mem_nickname);
                memberArrayList.add(member);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Member> getMemberArrayList() {
        return memberArrayList;
    }

    public void sendJson(final String url, final String mem_id, final String mem_nickname) {
        this.mem_id = mem_id;
        this.mem_nickname = mem_nickname;
        class CreateNewProduct extends AsyncTask<String, String, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            protected String doInBackground(String... args) {

                URL url;
                InputStream is = null;
                ByteArrayOutputStream baos = null;
                OutputStream os = null;
                // check for success tag
                try {

                    url = new URL(args[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject job = new JSONObject();
                    job.put(attributeMem_id, mem_id);
                    job.put(attributeMem_nickname, mem_nickname);

                    os = conn.getOutputStream();
                    os.write(job.toString().getBytes());
                    os.flush();

                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {

                        is = conn.getInputStream();
                        baos = new ByteArrayOutputStream();
                        byte[] byteBuffer = new byte[1024];
                        byte[] byteData = null;
                        int nLength = 0;
                        while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                            baos.write(byteBuffer, 0, nLength);
                        }
                        byteData = baos.toByteArray();

                        String response = new String(byteData);

                        Log.i(TAG, "DATA response = " + response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (is != null)
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    if (baos != null)
                        try {
                            baos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    if (os != null)
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }

                return null;
            }
        }

        CreateNewProduct createNewProduct = new CreateNewProduct();
        createNewProduct.execute(url);
    }

}
