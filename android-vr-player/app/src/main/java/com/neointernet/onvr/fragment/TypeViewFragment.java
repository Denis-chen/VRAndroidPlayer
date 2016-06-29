package com.neointernet.onvr.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neointernet.onvr.R;
import com.neointernet.onvr.adapter.StreamingVideoFileAdapter;
import com.neointernet.onvr.listener.AsyncTaskListener;
import com.neointernet.onvr.model.Video;
import com.neointernet.onvr.util.VideoJsonManager;

import java.util.ArrayList;

public class TypeViewFragment extends Fragment implements AsyncTaskListener {

    private StreamingVideoFileAdapter adapter;
    private ArrayList<Video> videoArrayList;
    private OnListFragmentInteractionListener mListener;
    private String jsonURL = "http://lifejeju99.cafe24.com/video_list.php";
    private VideoJsonManager videoJsonManager;
    private String TAG = "TypeViewFragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoArrayList = new ArrayList<>();
        videoJsonManager = new VideoJsonManager(this, getContext());
        Bundle args = getArguments();
        jsonURL += "?type=" + args.getString("type");
        Log.d(TAG, jsonURL);
        videoJsonManager.makeJsonData(jsonURL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type_view, container, false);
        Context context = view.getContext();

        adapter = new StreamingVideoFileAdapter(mListener, videoArrayList);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.mypage_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void asynkTaskFinished() {
        videoArrayList.addAll(videoJsonManager.getVideoArrayList());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach");
        if (context instanceof OnListFragmentInteractionListener) {
            Log.i(TAG, "context == OnListFramentInteractionListener");
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
