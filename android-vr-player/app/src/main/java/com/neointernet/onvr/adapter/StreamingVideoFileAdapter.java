package com.neointernet.onvr.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.neointernet.onvr.R;
import com.neointernet.onvr.fragment.VideoListFragment;
import com.neointernet.onvr.model.Header;
import com.neointernet.onvr.model.Section;
import com.neointernet.onvr.model.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StreamingVideoFileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Video> videos;
    private ArrayList<Section> sections;
    private VideoListFragment.OnListFragmentInteractionListener listener;
    private static final String TAG = "StreamingAdapter";
    private static final String URL = "http://lifejeju99.cafe24.com/";

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NOMAL = 1;
    private static final int TYPE_SECTION = 2;

    private Header header;

    public StreamingVideoFileAdapter(VideoListFragment.OnListFragmentInteractionListener listener, ArrayList<Video> videos, Header header, ArrayList<Section> sections) {
        this.listener = listener;
        this.videos = videos;
        this.header = header;
        this.sections = sections;
    }

    public StreamingVideoFileAdapter(VideoListFragment.OnListFragmentInteractionListener mListener, ArrayList<Video> videos) {
        this.listener = mListener;
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
            viewHolder.hitsText.setText(String.valueOf(video.getVideoHits()));
            viewHolder.memIdText.setText(video.getMemId());
            viewHolder.dateText.setText(video.getVideoSimpleDate());
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onListFragmentInteraction(video);
                }
            });
            String path = URL + video.getVideoThumbnail();
            Log.d(TAG, path);
            Picasso.with(viewHolder.imageView.getContext()).load(path).into(viewHolder.imageView);
        } else if (holder instanceof ViewHeaderHolder) {
            ViewHeaderHolder viewHeaderHolder = (ViewHeaderHolder) holder;
            Picasso.with(viewHeaderHolder.headerImageView.getContext()).load(header.getPath()).into(viewHeaderHolder.headerImageView);
            viewHeaderHolder.headerTextView.setText(header.getHeader());
            viewHeaderHolder.headerImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onListFragmentInteraction(getVideoForHeader(header.getPath()));
                }
            });

        } else if (holder instanceof ViewSectionHolder) {
            ViewSectionHolder viewSectionHolder = (ViewSectionHolder) holder;
            viewSectionHolder.sectionTextView.setText(getSection(position).getTitle());
        }
    }

    private Video getVideoForHeader(String path) {
        for (Video video : videos) {
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

        if (sections.size() == 0)  // noSectionRecycleView
            return videos.get(position);

        for (int i = sections.size() - 1; i >= 0; i--) { //sectionRecycleView
            int sectionPosition = sections.get(i).getPosition(); // 섹션 포지션을 받아옴
            if (sectionPosition < position) { //섹션 포지션과 입력받은 포지션값을 비교 처음으로 position이 커지면 현재 위치를 통해 video를 찾음
                for (Video video : videos) {
                    if (video.getViewPosition() != null)
                        if (position == video.getViewPosition())
                            return video;
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
        public TextView titleText, memIdText, hitsText, dateText;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_view);
            imageView = (ImageView) v.findViewById(R.id.video_image);
            titleText = (TextView) v.findViewById(R.id.video_title_text);
            memIdText = (TextView) v.findViewById(R.id.video_uploader_text);
            hitsText = (TextView) v.findViewById(R.id.video_hits_text);
            dateText = (TextView) v.findViewById(R.id.video_date_text);
        }
    }

    public class ViewHeaderHolder extends RecyclerView.ViewHolder {

        public ImageView headerImageView;
        public TextView headerTextView;

        public ViewHeaderHolder(View v) {
            super(v);
            headerImageView = (ImageView) v.findViewById(R.id.header_image_view);
            headerTextView = (TextView) v.findViewById(R.id.header_text_view);
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