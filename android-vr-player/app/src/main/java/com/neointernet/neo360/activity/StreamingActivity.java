//package com.neointernet.neo360.activity;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import com.neointernet.neo360.R;
//import com.neointernet.neo360.adapter.StreamingVideoFileAdapter;
//import com.neointernet.neo360.model.Video;
//import com.neointernet.neo360.util.GetJsonManager;
//import com.neointernet.neo360.util.MyDownloadManager;
//
//import java.util.ArrayList;
//
//public class StreamingActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private StreamingVideoFileAdapter adapter;
//    private ArrayList<Video> videoArrayList;
//    private RecyclerView recyclerView;
//    private RecyclerView.LayoutManager layoutManager;
//
//    private ProgressDialog loadingProgressDialog;
//
//
//    private static final String jsonURL = "http://lifejeju99.cafe24.com/test2.php";
//    private static final String URL = "http://lifejeju99.cafe24.com/";
//
//    private MyDownloadManager myDownloadManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_streaming);
//        myDownloadManager = new MyDownloadManager(getApplicationContext());
//        loadingProgressDialog = ProgressDialog.show(StreamingActivity.this, "", "잠시만 기다려주세요.", true);
//        videoArrayList = new GetJsonManager().getVideoArrayList(jsonURL);
//        showList();
//    }
//
//    private void showList() {
//            adapter = new StreamingVideoFileAdapter(this, videoArrayList);
//            recyclerView = (RecyclerView) findViewById(R.id.video_recyler_view);
//            recyclerView.setHasFixedSize(true);
//            layoutManager = new LinearLayoutManager(this);
//            recyclerView.setLayoutManager(layoutManager);
//            recyclerView.setAdapter(adapter);
//            loadingProgressDialog.dismiss();
//    }
//
//    @Override
//    public void onClick(View view) {
//        Video video = (Video) view.getTag();
//        Intent intent = new Intent(StreamingActivity.this, VideoActivity.class);
//        String path;
//        if (myDownloadManager.checkExistFile(video.getName()))
//            path = myDownloadManager.getStoragePath();
//        else
//            path = URL;
//        intent.putExtra("videopath", path + video.getName());
//        startActivity(intent);
//    }
//
//}
