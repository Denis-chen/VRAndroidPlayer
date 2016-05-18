package com.neointernet.neo360.adapter;

import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.neointernet.neo360.R;
import com.neointernet.neo360.fragment.VideoListFragment;
import com.neointernet.neo360.model.Video;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class StreamingVideoFileAdapter extends RecyclerView.Adapter<StreamingVideoFileAdapter.ViewHolder> {

    private ArrayList<Video> videos;
    private VideoListFragment.OnListFragmentInteractionListener listener;
    private MediaPlayer mediaPlayer;
    private static final String TAG = "VideoFileAdapter";
    private static final String URL = "http://lifejeju99.cafe24.com/";


    public StreamingVideoFileAdapter(VideoListFragment.OnListFragmentInteractionListener listener, ArrayList<Video> videoModels) {
        this.listener = listener;
        videos = videoModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.video = getVideo(position);
        holder.cardView.setTag(getVideo(position));
        holder.imageView.setImageBitmap(ThumbnailUtils.createVideoThumbnail(getVideoPath(position), MediaStore.Video.Thumbnails.MINI_KIND));
        holder.nameText.setText(getVideo(position).getName());
        holder.lengthText.setText(getVideoLength(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onListFragmentInteraction(holder.video);
            }
        });
    }

    private String getVideoLength(int position) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getVideoPath(position));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

        int duration = mediaPlayer.getDuration();
        int min = (int) TimeUnit.MILLISECONDS.toMinutes(duration);
        int sec = (int) TimeUnit.MILLISECONDS.toSeconds(duration);
        String time = "영상 길이 : " +  min + "분 " + (sec - min * 60) + "초 ";

        mediaPlayer.release();

        return time;
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public Video getVideo(int position) {
        return videos.get(position);
    }

    public String getVideoPath(int position) {
        return URL + videos.get(position).getName();
    }

    public String getVideoSize(int position){
        return videos.get(position).getSize();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView nameText, lengthText;
        public ImageView imageView;
        public Video video;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_view);
            imageView = (ImageView) v.findViewById(R.id.video_image);
            nameText = (TextView) v.findViewById(R.id.video_name_text);
            lengthText = (TextView) v.findViewById(R.id.video_length_text);
        }
    }
}