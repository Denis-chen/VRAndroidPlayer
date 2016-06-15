package com.neointernet.onvr.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.neointernet.onvr.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class UploadFragment extends Fragment {

    private static final String TAG = "UploadFragment";

    private EditText title, des, filename;

    private String url;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        url = "http://lifejeju99.cafe24.com/insert_video.php";

        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        title = (EditText) view.findViewById(R.id.title_edit);
        des = (EditText) view.findViewById(R.id.des_edit);
        filename = (EditText) view.findViewById(R.id.filename_edit);

        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendJson(url, title.getText().toString(), des.getText().toString(), filename.getText().toString());
            }
        });
        return view;
    }

    private void sendJson(final String url, String value1, String value2, String value3) {

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
                    job.put("video_title", args[1]);
                    job.put("video_des", args[2]);
                    job.put("video_type", "엔터테인먼트");
                    job.put("video_filename", args[3]); // 업로드를 먼저하고 이것을 넘겨야됨
                    job.put("mem_id", AccessToken.getCurrentAccessToken().getUserId());

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

            protected void onPostExecute(String file_url) {
                Toast.makeText(getActivity(), "전송되었습니다.", Toast.LENGTH_LONG).show();
            }
        }

        CreateNewProduct createNewProduct = new CreateNewProduct();
        createNewProduct.execute(url, value1, value2, value3);
    }
}
