package com.neointernet.onvr.fragment;

import android.content.Context;
import android.os.Bundle;
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
import com.neointernet.onvr.model.Header;
import com.neointernet.onvr.model.Section;
import com.neointernet.onvr.model.SectionFactory;
import com.neointernet.onvr.model.Video;
import com.neointernet.onvr.util.VideoJsonManager;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class VideoListFragment extends Fragment implements AsyncTaskListener {

    private OnListFragmentInteractionListener mListener;
    private String jsonURL = "http://lifejeju99.cafe24.com/video_list.php";

    private ArrayList<Video> videoArrayList;
    private ArrayList<Section> sectionArrayList;
    private static final String TAG = "VideoListFragment";

    private StreamingVideoFileAdapter adapter;
    private VideoJsonManager videoJsonManager;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    private LinearLayoutManager layoutManager;

    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoArrayList = new ArrayList<>();
        sectionArrayList = new ArrayList<>();
        videoJsonManager = new VideoJsonManager(this, getActivity());
        videoJsonManager.makeJsonData("http://lifejeju99.cafe24.com/video_list.php?start=" + 0 + "&quantity=" + 20);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        sectionArrayList.addAll(new SectionFactory().getSectionArrayList());
        adapter = new StreamingVideoFileAdapter(mListener, videoArrayList, getHeader(), sectionArrayList);

        recyclerView = (RecyclerView) view;
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

//
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (dy > 0) {
//                    visibleItemCount = layoutManager.getChildCount();
//                    totalItemCount = layoutManager.getItemCount();
//                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
//
//                    if (loading) {
//                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                            loading = false;
//                            videoJsonManager.makeJsonData(totalItemCount, 2);
//                        }
//                    }
//                }
//            }
//        });

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


    public Header getHeader() {
        Header header = new Header();
        header.setHeader("인피니트 뮤직비디오");
        header.setPath("videos/infinite.png");
        return header;
    }

    @Override
    public void asynkTaskFinished() {
        videoArrayList.addAll(videoJsonManager.getVideoArrayList());
        adapter.notifyDataSetChanged();
    }
}
