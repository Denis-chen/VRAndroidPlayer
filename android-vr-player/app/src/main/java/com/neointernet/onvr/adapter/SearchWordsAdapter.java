package com.neointernet.onvr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.neointernet.onvr.R;
import com.neointernet.onvr.activity.SearchActivity;
import com.neointernet.onvr.listener.OnDeleteButtonClickListener;
import com.neointernet.onvr.listener.SearchWordClickListener;

import java.util.ArrayList;

/**
 * Created by HSH on 16. 6. 29..
 */
public class SearchWordsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final static String TAG = "SearchWordsAdapter";

    private ArrayList<String> searchRecords;
    private OnDeleteButtonClickListener onDeleteButtonClickListener;
    private SearchWordClickListener searchWordClickListener;

    public SearchWordsAdapter(ArrayList<String> searchRecords, OnDeleteButtonClickListener onDeleteButtonClickListener, Context context) {
        this.searchRecords = searchRecords;
        this.onDeleteButtonClickListener = onDeleteButtonClickListener;
        this.searchWordClickListener = (SearchActivity) context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.searchText.setText(searchRecords.get(position));
        Log.d(TAG, "string : " + searchRecords.get(position));
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "word : " + searchRecords.get(position));
                searchWordClickListener.onClick(searchRecords.get(position));
            }
        });
        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "remove position : " + position);
                onDeleteButtonClickListener.delete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchRecords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView searchText;
        public View view;
        public ImageButton imageButton;

        public ViewHolder(View v) {
            super(v);
            view = v;
            searchText = (TextView) v.findViewById(R.id.search_textview);
            imageButton = (ImageButton) v.findViewById(R.id.search_delete_button);
        }
    }

}
