package com.samskrut.vrvideo.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.samskrut.vrvideo.R;
import com.samskrut.vrvideo.listener.CardBoardTouchEventListener;
import com.samskrut.vrvideo.listener.RenderChangedCheckListener;
import com.samskrut.vrvideo.renderer.VideoRenderer;
import com.samskrut.vrvideo.view.MyCardboardView;

import java.util.concurrent.TimeUnit;


public class MainActivity extends CardboardActivity implements RenderChangedCheckListener, CardBoardTouchEventListener{

    private MyCardboardView view;
    private VideoRenderer renderer;
    private final static String TAG = "MainActivity";
    private boolean vrCheck = false;
    private boolean opCheck = false;
    private int finalTime = 0;
    private int startTime = 0;

    private Button operateBtn, vrModeBtn;
    private SeekBar seekBar;
    private TextView startTx, finalTx;

    private View mediaController;
    private Handler handler;
    private boolean touchCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new MyCardboardView(MainActivity.this);
        setContentView(view);
        view.setVRModeEnabled(false);
        setCardboardView(view);
        renderer = new VideoRenderer(MainActivity.this, getIntent().getExtras().getString("fpath"));
        view.setRenderer(renderer);
        view.setSurfaceRenderer(renderer);
        view.setSettingsButtonEnabled(false);

        mediaControllerInit();
    }

    private void mediaControllerInit() {
        initMediaControllerLayout();

        initViews();

        //to get currentPosition & duration
        renderer.setListener(this);

        //to get touchEvent
        view.setListener(this);

        //to update currentPosition on seekbar
        handler = new Handler();
        handler.postDelayed(UpdateTime, 100);

        initOnclickListener();
    }

    private void initMediaControllerLayout() {
        mediaController = View.inflate(this, R.layout.media_controller, null);
        addContentView(mediaController, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void initViews() {
        operateBtn = (Button) findViewById(R.id.operateBtn);
        vrModeBtn = (Button) findViewById(R.id.vrModeBtn);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        finalTx = (TextView) findViewById(R.id.finalTime);
        startTx = (TextView) findViewById(R.id.startTime);
    }

    private void initOnclickListener() {
        operateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (opCheck == false) {
                    operateBtn.setText("시작");
                    renderer.mpPause();
                    opCheck = true;

                } else {
                    operateBtn.setText("중지");
                    renderer.mpResume();
                    opCheck = false;
                }
            }
        });

        vrModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vrCheck == false) {
                    view.setVRModeEnabled(true);
                    vrCheck = true;
                } else {
                    view.setVRModeEnabled(false);
                    vrCheck = false;
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    renderer.seekTo(progress);
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
            int startMin = (int) TimeUnit.MILLISECONDS.toMinutes((long) startTime);
            int startSec = (int) TimeUnit.MILLISECONDS.toSeconds((long) startTime);

            int finalMin = (int) TimeUnit.MILLISECONDS.toMinutes((long) finalTime);
            int finalSec = (int) TimeUnit.MILLISECONDS.toSeconds((long) finalTime);

            seekBar.setProgress(startTime);
            startTx.setText(startMin + " min " + (startSec - startMin) + " sec ");
            finalTx.setText(finalMin + " min " + (finalSec - finalMin) + " sec ");

            handler.postDelayed(this, 100);
        }
    };

    @Override
    public void onTime(int time) {
        Log.i(TAG, String.valueOf(time));
        startTime = time;
    }

    @Override
    public void setFinalTime(int time) {
        finalTime = time;
        seekBar.setMax(finalTime);
    }

    @Override
    public void onTouch() {
        if(touchCheck == false){
            mediaController.setVisibility(View.INVISIBLE);
            touchCheck = true;
        }
        else{
            mediaController.setVisibility(View.VISIBLE);
            touchCheck = false;
        }
    }
}