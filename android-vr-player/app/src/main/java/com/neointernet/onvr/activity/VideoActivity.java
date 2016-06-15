package com.neointernet.onvr.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.neointernet.onvr.R;
import com.neointernet.onvr.listener.CardboardEventListener;
import com.neointernet.onvr.listener.VideoTimeListener;
import com.neointernet.onvr.renderer.VideoRenderer;
import com.neointernet.onvr.util.MyDownloadManager;
import com.neointernet.onvr.view.MyCardboardView;

import java.util.concurrent.TimeUnit;

/**
 * Created by neo-202 on 2016-03-22.
 */
public class VideoActivity extends CardboardActivity implements VideoTimeListener, CardboardEventListener, View.OnClickListener, MediaPlayer.OnCompletionListener {

    private MyCardboardView view;
    private VideoRenderer renderer;
    private View barLayout;
    private ImageButton vrButton, playButton;
    private Button backButton, saveVideoButton;
    private SeekBar videoSeekBar;
    private TextView currentTimeTextView, finalTimeTextView;

    private final static String TAG = "VideoActivity";

    private Handler handler;

    private int currentTime, finalTime;

    private String url;
    private BroadcastReceiver completeReceiver;

    private MyDownloadManager myDownloadManager;
    private String videoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDownloadManager = new MyDownloadManager(getApplicationContext());
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(completeReceiver, completeFilter);
    }

    private Runnable UpdateTime = new Runnable() {
        @Override
        public void run() {
            int startMin = (int) TimeUnit.MILLISECONDS.toMinutes((long) currentTime);
            int startSec = (int) TimeUnit.MILLISECONDS.toSeconds((long) currentTime);

            int finalMin = (int) TimeUnit.MILLISECONDS.toMinutes((long) finalTime);
            int finalSec = (int) TimeUnit.MILLISECONDS.toSeconds((long) finalTime);

            videoSeekBar.setProgress(currentTime);
            currentTimeTextView.setText(startMin + " : " + (startSec - startMin * 60));
            finalTimeTextView.setText(finalMin + " : " + (finalSec - finalMin * 60));
            handler.postDelayed(this, 100);
        }
    };

    @Override
    public void onVideoInit(int length) {
        finalTime = length;
        videoSeekBar.setMax(length);
    }


    @Override
    public void listenTime(int time) {
        currentTime = time;
        videoSeekBar.setProgress(time);
    }

    @Override
    public void onCardboardTouch(MotionEvent e) {
        int visibility;
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            visibility = barLayout.getVisibility();
            if (visibility == View.INVISIBLE) {
                barLayout.setVisibility(View.VISIBLE);
            } else {
                barLayout.setVisibility(View.INVISIBLE);
            }
        }

    }

    private void initView() {
        view = new MyCardboardView(VideoActivity.this);
        view.setSettingsButtonEnabled(false);
        view.setVRModeEnabled(true);
        view.setDistortionCorrectionEnabled(false);
        view.setAlignmentMarkerEnabled(false);
        setContentView(view);
        setCardboardView(view);

        Intent intent = getIntent();
        url = intent.getStringExtra("videopath");
        videoTitle = intent.getStringExtra("videoTitle");
        renderer = new VideoRenderer(VideoActivity.this, url);
        view.setRenderer(renderer);
        view.setSurfaceRenderer(renderer);
        renderer.setVideoTimeListener(this);

        view.addCardboardEventListener(renderer);
        view.addCardboardEventListener(this);

        handler = new Handler();
        handler.postDelayed(UpdateTime, 100);

        LayoutInflater layoutInflater = getLayoutInflater();
        barLayout = layoutInflater.inflate(R.layout.video_controller, null);
        addContentView(barLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        backButton = (Button) findViewById(R.id.backBtn);
        saveVideoButton = (Button) findViewById(R.id.saveVideoBtn);
        vrButton = (ImageButton) findViewById(R.id.vrBtn);
        playButton = (ImageButton) findViewById(R.id.playBtn);
        videoSeekBar = (SeekBar) findViewById(R.id.videoSeekBar);
        currentTimeTextView = (TextView) findViewById(R.id.currentTimeTextView);
        finalTimeTextView = (TextView) findViewById(R.id.finalTimeTextView);

        backButton.setOnClickListener(this);
        saveVideoButton.setOnClickListener(this);

        vrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.getVRMode()) {
                    vrButton.setImageResource(R.drawable.vr_mode);
                    view.setVRModeEnabled(false);
                } else {
                    vrButton.setImageResource(R.drawable.vr);
                    view.setVRModeEnabled(true);
                }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (renderer.isPlaying()) {
                    Log.i(TAG, "video STOP!!");
                    playButton.setImageResource(R.drawable.ic_play_arrow_48dp);
                    renderer.onPause();
                } else {
                    Log.i(TAG, "video START!!");
                    playButton.setImageResource(R.drawable.ic_pause_48dp);
                    renderer.onResume();
                }
            }
        });

        videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    renderer.setMediaPlayerSeekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        completeReceiver = myDownloadManager.createReceiver();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.saveVideoBtn:
                String des;
                myDownloadManager.setTitle(videoTitle);
                if (myDownloadManager.downloadFile(url)){
                    des = "다운로드가 시작됩니다.";
                }
                else{
                    des = "이미 다운로드가 되었습니다..";
                }
                Toast.makeText(VideoActivity.this, des, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        renderer.onRendererShutdown();
        unregisterReceiver(completeReceiver);
        this.finish();
    }
}