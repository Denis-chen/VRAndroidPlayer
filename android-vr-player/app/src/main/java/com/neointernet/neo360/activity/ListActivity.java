package com.neointernet.neo360.activity;

/**
 * Created by neo-202 on 2016-03-22.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.neointernet.neo360.R;
import com.neointernet.neo360.adapter.VideoFileAdapter;

import java.io.File;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {

    private String folderPath = Environment.getExternalStorageDirectory() + File.separator + "360Videos";
    private File folder;
    private ArrayList<File> fileArrayList;
    private FileObserver fileObserver;

    //기존 ListView보다 유연하고 성능이 향상된 위젯!
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //폴더를 생성해주는 부분
        folderPath = Environment.getExternalStorageDirectory() + File.separator + "360Videos";
        folder = new File(folderPath);
        Log.i("Folder", folder.toString());
        Log.i("Folder", folder.getAbsolutePath().toString());
        Log.i("Folder", folder.listFiles().toString());

        fileArrayList = new ArrayList<>();
        setFileArray();
        adapter = new VideoFileAdapter(this, fileArrayList);
        recyclerView = (RecyclerView) findViewById(R.id.video_recyler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //디렉토리를 감시용
        fileObserver = new FileObserver(folderPath) {
            @Override
            public void onEvent(int event, String path) {
                Log.i("EVENT", Integer.toString(event));
                switch (event) {
                    //디렉토리에 서브 디렉토리 및 파일이 생성되었을 때
                    case FileObserver.CREATE:
                        Log.i("FILE OBSERVER", "CREATE");
                        break;
                    //디렉토리에 서브 디렉토리 및 파일이 삭제되었을 때
                    case FileObserver.DELETE:
                        Log.i("FILE OBSERVER", "DELETE");
                        break;
                }

            }
        };
        //뒤로가기
    }

    public void setFileArray() {
        File[] newFiles = folder.listFiles();
        fileArrayList.clear();
        for (File file : newFiles) {
            File File = (File) file;
            fileArrayList.add(File);
        }
    }

    @Override
    public void onClick(View v) {
        File newFile = (File)v.getTag();
        Intent intent = new Intent(ListActivity.this, VideoActivity.class);
        intent.putExtra("videopath", newFile.getPath());
        startActivity(intent);
    }
}