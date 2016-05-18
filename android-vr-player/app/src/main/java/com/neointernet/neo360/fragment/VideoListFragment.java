package com.neointernet.neo360.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neointernet.neo360.R;
import com.neointernet.neo360.adapter.StreamingVideoFileAdapter;
import com.neointernet.neo360.listener.AsyncTaskListener;
import com.neointernet.neo360.model.Video;
import com.neointernet.neo360.util.GetJsonManager;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class VideoListFragment extends Fragment implements AsyncTaskListener {

    private OnListFragmentInteractionListener mListener;
    private static final String jsonURL = "http://lifejeju99.cafe24.com/test2.php";

    private ArrayList<Video> videoArrayList;
    private static final String TAG = "VideoListFragment";

    private StreamingVideoFileAdapter adapter;
    private GetJsonManager getJsonManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoArrayList = new ArrayList<>();
        getJsonManager = new GetJsonManager(this);
        getJsonManager.getJSONData(jsonURL);
        Log.i(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        Log.i(TAG, "onCreateView");
        adapter = new StreamingVideoFileAdapter(mListener, videoArrayList);
        Log.i(TAG, "view == RecyclerView");
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        return view;
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

    @Override
    public void asynkTaskFinished() {
        videoArrayList.addAll(getJsonManager.getVideoArrayList());
        for(Video video : videoArrayList){
            Log.d(TAG, video.getName());
        }
        adapter.notifyDataSetChanged();
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Video item);
    }
}
