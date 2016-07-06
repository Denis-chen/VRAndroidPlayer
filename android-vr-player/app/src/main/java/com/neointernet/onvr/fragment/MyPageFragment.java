package com.neointernet.onvr.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.neointernet.onvr.R;
import com.neointernet.onvr.activity.UploadVideoActivity;
import com.neointernet.onvr.adapter.StreamingVideoFileAdapter;
import com.neointernet.onvr.listener.AsyncTaskListener;
import com.neointernet.onvr.model.Video;
import com.neointernet.onvr.util.VideoJsonManager;

import java.util.ArrayList;

public class MyPageFragment extends Fragment implements AsyncTaskListener {

    private static final String TAG = "MyPageFragment";

    private StreamingVideoFileAdapter adapter;
    private ArrayList<Video> videoArrayList;
    private OnListFragmentInteractionListener mListener;
    private String jsonURL = "http://lifejeju99.cafe24.com/video_list.php";
    private VideoJsonManager videoJsonManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoArrayList = new ArrayList<>();
        videoJsonManager = new VideoJsonManager(this, getActivity());
        jsonURL += "/?mem_id=" + AccessToken.getCurrentAccessToken().getUserId();
        Log.d(TAG, jsonURL);
        videoJsonManager.makeJsonData(jsonURL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
        Context context = view.getContext();

        adapter = new StreamingVideoFileAdapter(getContext(), videoArrayList);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.mypage_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UploadVideoActivity.class));
            }
        });
        return view;
    }

    @Override
    public void asynkTaskFinished() {
        videoArrayList.addAll(videoJsonManager.getVideoArrayList());
        adapter.notifyDataSetChanged();
    }
}
