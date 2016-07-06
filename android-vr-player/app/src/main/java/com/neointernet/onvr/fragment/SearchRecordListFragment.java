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
import com.neointernet.onvr.adapter.SearchWordsAdapter;
import com.neointernet.onvr.listener.OnDeleteButtonClickListener;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchRecordListFragment extends Fragment implements OnDeleteButtonClickListener {


    private RecyclerView.Adapter adapter;
    private ArrayList<String> strings;
    private final static String TAG = "SearchListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_record_list, container, false);
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("search", Context.MODE_PRIVATE);
//        strings = sharedPreferences.getString("strings", new HashSet<String>());

        getSavedSearchWords();

        Log.d(TAG, "strings: " + strings);

        setListViewWithSearchWords(view);
        return view;
    }

    private void setListViewWithSearchWords(View view) {
        adapter = new SearchWordsAdapter(strings, this, getContext());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.search_words_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void getSavedSearchWords() {
        String arrayString = getActivity().getSharedPreferences("search", Context.MODE_PRIVATE).getString("array", "[]");
        if (!arrayString.equals("[]"))
            strings = new ArrayList<>(Arrays.asList(arrayString.substring(1, arrayString.length() - 1).split(", ")));
        else
            strings = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
//        realm.beginTransaction();
    }


    @Override
    public void onPause() {
        super.onPause();
//        realm.commitTransaction();
    }

    @Override
    public void delete(int position) {
        strings.remove(position);
        getActivity().getSharedPreferences("search", Context.MODE_PRIVATE).edit().putString("array", strings.toString()).commit();
        adapter.notifyDataSetChanged();
    }
}
