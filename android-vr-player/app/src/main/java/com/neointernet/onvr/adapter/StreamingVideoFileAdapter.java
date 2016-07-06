package com.neointernet.onvr.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.neointernet.onvr.R;
import com.neointernet.onvr.activity.BaseActivity;
import com.neointernet.onvr.fragment.OnListFragmentInteractionListener;
import com.neointernet.onvr.model.Header;
import com.neointernet.onvr.model.Section;
import com.neointernet.onvr.model.Video;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StreamingVideoFileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Video> videos;
    private ArrayList<Section> sections;
    private OnListFragmentInteractionListener listener;
    private static final String TAG = "StreamingAdapter";
    private static final String URL = "http://lifejeju99.cafe24.com/";

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NOMAL = 1;
    private static final int TYPE_SECTION = 2;

    private Header header;

    public StreamingVideoFileAdapter(Context context, ArrayList<Video> videos, Header header, ArrayList<Section> sections) {
        BaseActivity activity = (BaseActivity) context;
        listener = activity;
        this.videos = videos;
        this.header = header;
        this.sections = sections;
    }

    public StreamingVideoFileAdapter(Context context, ArrayList<Video> videos) {
        BaseActivity activity = (BaseActivity) context;
        listener = activity;
        this.videos = videos;
        this.sections = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_row_header, parent, false);
            Log.d(TAG, "TYPE_HEADER");
            return new ViewHeaderHolder(v);
        } else if (viewType == TYPE_NOMAL) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_row, parent, false);
            Log.d(TAG, "TYPE_NOMAL");
            return new ViewHolder(v);
        } else if (viewType == TYPE_SECTION) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_row_section, parent, false);
            Log.d(TAG, "TYPE_SECTION");
            return new ViewSectionHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            final Video video = getVideo(position);
            viewHolder.cardView.setTag(video);
            viewHolder.titleText.setText(video.getVideoTitle());
            viewHolder.hitsText.setText(getFormatedHits(video));
            viewHolder.memIdText.setText(video.getMemId());
            viewHolder.dateText.setText(getFormatedDate(video));
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onListFragmentInteraction(video);
                }
            });
            String path = URL + video.getVideoThumbnail();
            Log.d(TAG, path);
            Picasso.with(viewHolder.imageView.getContext()).load(path).into(viewHolder.imageView);
            viewHolder.durationText.setText(getFormatDuration(video));
        } else if (holder instanceof ViewHeaderHolder) {
            ViewHeaderHolder viewHeaderHolder = (ViewHeaderHolder) holder;
            final Video video = getVideoForHeader(header.getPath());
            Picasso.with(viewHeaderHolder.headerImageView.getContext()).load("http://lifejeju99.cafe24.com/" + header.getPath()).into(viewHeaderHolder.headerImageView);
            viewHeaderHolder.titleText.setText(header.getHeader());
            viewHeaderHolder.memIdText.setText(video.getMemId());
            viewHeaderHolder.hitsText.setText(getFormatedHits(video));
            viewHeaderHolder.dateText.setText(getFormatedDate(video));
            viewHeaderHolder.durationText.setText(getFormatDuration(video));
            viewHeaderHolder.headerImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onListFragmentInteraction(video);
                }
            });

        } else if (holder instanceof ViewSectionHolder) {
            ViewSectionHolder viewSectionHolder = (ViewSectionHolder) holder;
            viewSectionHolder.sectionTextView.setText(getSection(position).getKorTitle());
        }
    }

    private String getFormatedHits(Video video) {
        Integer hits = video.getVideoHits();
        String result = "";
        if (hits >= 10000)
            result += hits / 10000 + "만";
        else
            result = String.format("%,d", hits);
        return result;
    }

    private String getFormatedDate(Video video) {
        String result = "";

        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = format.parse(video.getVideoDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "date : " + date);
        Long gap = (System.currentTimeMillis() - date.getTime()) / 1000;
        Log.d(TAG, "gap : " + gap);


        if (gap <= 60)
            result = "방금전";
        else if (gap <= 3600)
            result = gap / 60 + 1 + "분전";
        else if (gap <= 86400)
            result = gap / 3600 + 1 + "시간전";
        else if (gap <= 604800)
            result = gap / 86400 + 1 + "일전";
        else if (gap <= 2592000)
            result = gap / 604800 + 1 + "주전";
        else if (gap <= 31536000)
            result = gap / 2592000 + 1 + "달전";
        else
            result = gap / 31536000 + 1 + "년전";
        return result;
    }

    private String getFormatDuration(Video video) {

        Integer time = video.getVideoDuration().intValue();
        Log.d(TAG, "time : " + time);
        int minuite = time / 60;
        int second = time - minuite * 60;
        String divide = ":";
        if (second < 10)
            divide += "0";

        String formattedTime = minuite + divide + second;
        Log.d(TAG, formattedTime);
        return formattedTime;
    }

    private Video getVideoForHeader(String path) {
        for (Video video : videos) {
            Log.d("Thumbnail : ", video.getVideoThumbnail());
            Log.d("path : ", path);
            if (video.getVideoThumbnail().equals(path))
                return video;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        int type = TYPE_NOMAL;
        Log.d(TAG, "position " + position);
        if (position == 0 && header != null) {
            type = TYPE_HEADER;
            Log.d(TAG, "type " + type);
        } else if (position % 3 == 1 && sections.size() != 0) {
            type = TYPE_SECTION;
            Log.d(TAG, "type " + type);
        }
        Log.d(TAG, "type number : " + type);
        return type;
    }

    public Video getVideo(int position) { // 적당한 포지션 값이 들어오면 그것에 알맞은 비디오를 뿌려주면됨
        Log.d("TAG", "getVideo position : " + position);
        if (sections.size() == 0)  // noSectionRecycleView
            return videos.get(position);

        for (int i = sections.size() - 1; i >= 0; i--) { //sectionRecycleView
            int sectionPosition = sections.get(i).getPosition(); // 섹션 포지션을 받아옴
            Log.d("TAG", "getVideo sectionPosition : " + sectionPosition);
            if (sectionPosition < position) { //섹션 포지션과 입력받은 포지션값을 비교 처음으로 position이 커지면 현재 위치를 통해 video를 찾음
                for (Video video : videos) {
                    if (video.getViewPosition() != null) {
                        Log.d("TAG", "getVideo getViewPosition : " + video.getViewPosition());
                        if (position == video.getViewPosition())
                            return video;
                    }

                }
                for (int j = 0; j < videos.size(); j++) {
                    if (sections.get(i).getTitle().equals(videos.get(j).getVideoType()) && videos.get(j).getViewPosition() == null) {
                        videos.get(j).setViewPosition(position);
                        return videos.get(j);
                    }
                }
            }
        }
        Log.d(TAG, "*************************fail***************************");
        return videos.get(0);
    }

    private Section getSection(int position) {
        for (int i = sections.size() - 1; i >= 0; i--) {
            int sectionPosition = sections.get(i).getPosition(); // 섹션 포지션을 받아옴
            if (sectionPosition == position) {
                return sections.get(i);
            }
        }
        return sections.get(0);
    }


    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView titleText, memIdText, hitsText, dateText, durationText;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_view);
            imageView = (ImageView) v.findViewById(R.id.video_image);
            titleText = (TextView) v.findViewById(R.id.video_title_text);
            memIdText = (TextView) v.findViewById(R.id.video_uploader_text);
            hitsText = (TextView) v.findViewById(R.id.video_hits_text);
            dateText = (TextView) v.findViewById(R.id.video_date_text);
            durationText = (TextView) v.findViewById(R.id.video_durations);
        }
    }

    public class ViewHeaderHolder extends RecyclerView.ViewHolder {

        public ImageView headerImageView;
        public TextView titleText, memIdText, hitsText, dateText, durationText;

        public ViewHeaderHolder(View v) {
            super(v);
            headerImageView = (ImageView) v.findViewById(R.id.header_image_view);
            titleText = (TextView) v.findViewById(R.id.header_text_view);
            memIdText = (TextView) v.findViewById(R.id.header_uploader_text);
            hitsText = (TextView) v.findViewById(R.id.header_hits_text);
            dateText = (TextView) v.findViewById(R.id.header_date_text);
            durationText = (TextView) v.findViewById(R.id.video_durations_header);
        }
    }

    public class ViewSectionHolder extends RecyclerView.ViewHolder {

        public TextView sectionTextView;

        public ViewSectionHolder(View v) {
            super(v);
            sectionTextView = (TextView) v.findViewById(R.id.section_text_view);
        }
    }
}