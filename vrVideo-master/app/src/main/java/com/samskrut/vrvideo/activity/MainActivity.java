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
import com.samskrut.vrvideo.listener.RenderChangedCheckListener;
import com.samskrut.vrvideo.renderer.VideoRenderer;

import org.rajawali3d.cardboard.RajawaliCardboardView;

import java.util.concurrent.TimeUnit;


public class MainActivity extends CardboardActivity implements RenderChangedCheckListener{

    private RajawaliCardboardView view;
    private VideoRenderer renderer;
    private final static String TAG = "MainActivity";
    private boolean vrCheck = false;
    private boolean opCheck = false;
    private int finalTime = 0;
    private int startTime = 0;

    private Button operateBtn, vrModeBtn;
    private SeekBar seekBar;
    private TextView startTx, finalTx;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new RajawaliCardboardView(MainActivity.this);
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
        View mediaController = View.inflate(this, R.layout.media_controller, null);
        addContentView(mediaController, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        operateBtn = (Button) findViewById(R.id.operateBtn);
        vrModeBtn = (Button) findViewById(R.id.vrModeBtn);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        finalTx = (TextView) findViewById(R.id.finalTime);
        startTx = (TextView) findViewById(R.id.startTime);

        renderer.setListener(this);

//        mediaPlayer = renderer.getMp();

        handler = new Handler();
        handler.postDelayed(UpdateTime, 100);

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
//                    renderer.seekTo(progress);
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
            seekBar.setProgress(startTime);
            startTx.setText(TimeUnit.MILLISECONDS.toMinutes((long) startTime) + " min " +
                            (TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)) + " sec ")
            );
            finalTx.setText(TimeUnit.MILLISECONDS.toMinutes((long) finalTime) + " min " +
                            (TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)) + " sec ")
            );
            handler.postDelayed(this, 100);
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

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
}