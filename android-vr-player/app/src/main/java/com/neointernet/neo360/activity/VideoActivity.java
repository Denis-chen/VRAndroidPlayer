package com.neointernet.neo360.activity;

import android.content.Intent;
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

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.neointernet.neo360.R;
import com.neointernet.neo360.listener.CardboardEventListener;
import com.neointernet.neo360.listener.VideoTimeListener;
import com.neointernet.neo360.renderer.VideoRenderer;
import com.neointernet.neo360.view.MyCardboardView;

import java.util.concurrent.TimeUnit;

/**
 * Created by neo-202 on 2016-03-22.
 */
public class VideoActivity extends CardboardActivity implements VideoTimeListener, CardboardEventListener {

    private MyCardboardView view;
    private VideoRenderer renderer;
    private View barLayout;
    private ImageButton vrButton, playButton;
    private Button backButton;
    private SeekBar videoSeekBar;
    private TextView currentTimeTextView, finalTimeTextView;

    private final static String TAG = "VideoActivity";

    private Handler handler;

    private int currentTime, finalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new MyCardboardView(VideoActivity.this);
        view.setSettingsButtonEnabled(false);
        view.setVRModeEnabled(true);
        view.setDistortionCorrectionEnabled(false);
        view.setAlignmentMarkerEnabled(false);
        setContentView(view);
        setCardboardView(view);

        Intent intent = getIntent();
        renderer = new VideoRenderer(VideoActivity.this, intent.getStringExtra("videopath"));
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
        vrButton = (ImageButton) findViewById(R.id.vrBtn);
        playButton = (ImageButton) findViewById(R.id.playBtn);
        videoSeekBar = (SeekBar) findViewById(R.id.videoSeekBar);
        currentTimeTextView = (TextView) findViewById(R.id.currentTimeTextView);
        finalTimeTextView = (TextView) findViewById(R.id.finalTimeTextView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        vrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(view.getVRMode()){
                    vrButton.setImageResource(R.drawable.nomal_mode);
                    view.setVRModeEnabled(false);
                }
                else{
                    vrButton.setImageResource(R.drawable.vr_mode);
                    view.setVRModeEnabled(true);
                }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (renderer.isPlaying()) {
                    Log.i(TAG, "video STOP!!");
                    playButton.setImageResource(R.drawable.play);
                    renderer.onPause();
                } else {
                    Log.i(TAG, "video START!!");
                    playButton.setImageResource(R.drawable.stop);
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
        if(e.getAction() == MotionEvent.ACTION_DOWN){
            visibility = barLayout.getVisibility();
            if(visibility == View.INVISIBLE){
                barLayout.setVisibility(View.VISIBLE);
            }
            else{
                barLayout.setVisibility(View.INVISIBLE);
            }
        }

    }

}