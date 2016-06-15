//package com.neointernet.onvr.adapter;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.media.MediaPlayer;
//import android.media.ThumbnailUtils;
//import android.provider.MediaStore;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.neointernet.onvr.R;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.concurrent.TimeUnit;
//
//public class VideoFileAdapter extends RecyclerView.Adapter<VideoFileAdapter.ViewHolder> {
//
//    private ArrayList<File> videos;
//    private View.OnClickListener listener;
//    private MediaPlayer mediaPlayer;
//    private static final String TAG = "VideoFileAdapter";
//
//    public VideoFileAdapter(View.OnClickListener listener, Collection<File> videoModels) {
//        this.listener = listener;
//        videos = new ArrayList<>();
//        videos.addAll(videoModels);
//    }
//
//    public VideoFileAdapter(Context applicationContext, ArrayList<String> filenames, ArrayList<String> filepaths, ArrayList<Bitmap> al) {
//    }
//
//    @Override
//    public VideoFileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_row, parent, false);
//        ViewHolder vh = new ViewHolder(v);
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.cardView.setTag(getFile(position));
//        holder.imageView.setImageBitmap(ThumbnailUtils.createVideoThumbnail(getFile(position).getPath(), MediaStore.Video.Thumbnails.MINI_KIND));
//        holder.nameText.setText(getFile(position).getName());
//        holder.lengthText.setText(getVideoLength(position));
//        holder.cardView.setOnClickListener(listener);
//    }
//
//    private String getVideoLength(int position) {
//        try {
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setDataSource(getFilePath(position));
//            mediaPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e(TAG, e.toString());
//        }
//
//        int duration = mediaPlayer.getDuration();
//        int min = (int) TimeUnit.MILLISECONDS.toMinutes(duration);
//        int sec = (int) TimeUnit.MILLISECONDS.toSeconds(duration);
//        String time = "영상 길이 : " + min + "분 " + (sec - min * 60) + "초 ";
//
//        mediaPlayer.release();
//
//        return time;
//    }
//
//    @Override
//    public int getItemCount() {
//        return videos.size();
//    }
//
//    public File getFile(int position) {
//        return videos.get(position);
//    }
//
//    public String getFilePath(int position) {
//        return videos.get(position).getPath();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//
//        public CardView cardView;
//        public TextView nameText, lengthText;
//        public ImageView imageView;
//
//        public ViewHolder(View v) {
//            super(v);
//            cardView = (CardView) v.findViewById(R.id.card_view);
//            imageView = (ImageView) v.findViewById(R.id.video_image);
//            nameText = (TextView) v.findViewById(R.id.video_title_text);
//            lengthText = (TextView) v.findViewById(R.id.video_length_text);
//        }
//    }
//}