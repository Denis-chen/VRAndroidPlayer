package com.neointernet.onvr.activity;

import android.support.v7.app.AppCompatActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.facebook.AccessToken;
import com.neointernet.onvr.R;
import com.neointernet.onvr.model.Video;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by neo-202 on 2016-05-17.
 */
public class UploadVideoActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText;
    private Spinner spinner;
    private ImageView imageView;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView = (ImageView) findViewById(R.id.video_image);
        titleEditText = (EditText) findViewById(R.id.title_edit_text);
        descriptionEditText = (EditText) findViewById(R.id.description_edit_text);
        spinner = (Spinner) findViewById(R.id.spinner);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(intent, 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_upload_video_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_upload:
                Video videoModel = new Video();
                videoModel.setVideoTitle(titleEditText.getText().toString());
                videoModel.setVideoDes(descriptionEditText.getText().toString());
                videoModel.setVideoType(spinner.getSelectedItem().toString());
                videoModel.setMemId(AccessToken.getCurrentAccessToken().getUserId());
                new VideoUploadTask(videoModel).execute();
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri vid = data.getData();
                    String videoPath = getRealPathFromURI(vid);
                    file = new File(videoPath);
                    imageView.setImageBitmap(ThumbnailUtils.createVideoThumbnail(file.getPath(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
                } else if (resultCode == RESULT_CANCELED) {
                    finish();
                }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private class VideoUploadTask extends AsyncTask<String, Integer, String> {
        private String title;
        private Video videoModel;
        private String fileName;
        private int serverResponseCode;

        private int notificationId = (int) System.currentTimeMillis();
        private NotificationManager notificationManager;
        private Notification.Builder notificationBuilder;

        public VideoUploadTask(Video videoModel) {
            this.videoModel = videoModel;
            title = videoModel.getVideoTitle();
            fileName = System.currentTimeMillis() + "_" + String.valueOf((int) (Math.random() * 1000) + 1) + ".mp4";

            notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationBuilder = new Notification.Builder(getBaseContext())
                    .setSmallIcon(R.drawable.ic_file_upload_white_24dp)
                    .setContentTitle("동영상 업로드 : " + title)
                    .setContentText("업로드 중 : " + title)
                    .setTicker("동영상 업로드 : " + title)
                    .setProgress(100, 0, false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            notificationManager.notify(notificationId, notificationBuilder.build());
        }

        @Override
        protected void onProgressUpdate(Integer... params) {
            notificationBuilder.setProgress(100, params[0], false);
            notificationManager.notify(notificationId, notificationBuilder.build());
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 5 * 1024 * 1024;
            int sentData = 0;
            try {
                String serverUrl = "http://lifejeju99.cafe24.com/upload.php";
                Log.i("Url", serverUrl);
                FileInputStream fileInputStream = new FileInputStream(file);
                System.out.println(fileInputStream);
                System.out.println(file);
                System.out.println(file.toString());
                int length = fileInputStream.available();
                URL url = new URL(serverUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data;");
                dos.writeBytes("name=\"uploaded_file\";");
                dos.writeBytes("filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    sentData = sentData + bytesRead;
                    int progress = (int) (((float) sentData / (float) length) * 100);
                    publishProgress(progress);
                }
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("title", videoModel.getVideoTitle());
                hashMap.put("des", videoModel.getVideoDes());
                hashMap.put("type", videoModel.getVideoType());
                hashMap.put("id", videoModel.getMemId());
                hashMap.put("uploaded_file", fileName);

                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.write(entry.getValue().getBytes());
                    dos.writeBytes(lineEnd);
                }

                notificationBuilder.setProgress(100, 100, false)
                        .setContentTitle("업로드 완료 대기 중 : " + title)
                        .setContentText("동영상 업로드가 완료 대기 중입니다.")
                        .setTicker("업로드 완료 대기 중 : " + title);
                notificationManager.notify(notificationId, notificationBuilder.build());

                Log.i("Upload Result", "HTTP Response is : " + serverResponseCode);

                serverResponseCode = conn.getResponseCode();

                InputStream is = conn.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                byte[] byteData = null;
                int nLength = 0;
                while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                    baos.write(byteBuffer, 0, nLength);
                }
                byteData = baos.toByteArray();

                String response = new String(byteData);
                Log.i("Response", "DATA response = " + response);

                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (Exception e) {

            }

            return String.valueOf(serverResponseCode);
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);
            if (result.equals("200")) {
                notificationBuilder.setContentTitle("업로드 완료 : " + title)
                        .setContentText("동영상 업로드가 완료되었습니다.")
                        .setTicker("업로드 완료 : " + title);
            } else {
                notificationBuilder.setContentTitle("업로드 실패 : " + title)
                        .setContentText("동영상 업로드 중 에러가 발생하였습니다.")
                        .setTicker("업로드 실패 : " + title);
            }
            notificationManager.notify(notificationId, notificationBuilder.build());
        }
    }

}