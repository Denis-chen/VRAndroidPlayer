package com.samskrut.vrvideo.renderer;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;

import com.samskrut.vrvideo.listener.RenderChangedCheckListener;

import org.rajawali3d.cardboard.RajawaliCardboardRenderer;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.StreamingTexture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;

import java.io.File;

public class VideoRenderer extends RajawaliCardboardRenderer{

    // Context mContext;
    Activity mainActivity;
    String videopath;
    private MediaPlayer mMediaPlayer;
    private StreamingTexture mVideoTexture;

    private float angleX;
    private float angleY;
    private int mScreenWidth;
    private int mScreenHeight;
    private static final String TAG = "VideoRenderer";

    private RenderChangedCheckListener renderChangedCheckListener;

    public VideoRenderer(Activity activity, String _path) {
        super(activity.getApplicationContext());

        videopath = _path;
        mainActivity = activity;
    }

    @Override
    protected void initScene() {

        //mMediaPlayer = MediaPlayer.create(getContext(),
        //       R.raw.video);
        File file = new File(videopath);
        Uri uri = Uri.fromFile(file);
        Log.d("bis", "uri= " + uri.toString());
        mMediaPlayer = MediaPlayer.create(getContext(), uri);

        mMediaPlayer.setLooping(true);

        mVideoTexture = new StreamingTexture("sintelTrailer", mMediaPlayer);
        Material material = new Material();
        material.setColorInfluence(0);
        try {
            material.addTexture(mVideoTexture);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }

        Sphere sphere = new Sphere(50, 64, 32);
        sphere.setScaleX(-1);
        sphere.setMaterial(material);

        getCurrentScene().addChild(sphere);

        getCurrentCamera().setPosition(Vector3.ZERO);

        getCurrentCamera().setFieldOfView(75);

        mMediaPlayer.start();

        //영상이 끝나면 액티비티 종료
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("bis", "video completed");
                mp.stop();
                mp.release();
                mainActivity.finish();
            }
        });
        //영상이 끝나도록 유도되면 액티비티 종료
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                Log.d("bis", "video seek completed");
                mp.stop();
                mp.release();
                mainActivity.finish();
            }
        });

    }

    public void setListener(RenderChangedCheckListener renderChangedCheckListener){
        this.renderChangedCheckListener = renderChangedCheckListener;
    }

    public void currentTime(int time){
        renderChangedCheckListener.onTime(time);
    }

    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime) {
        super.onRender(ellapsedRealtime, deltaTime);
        mVideoTexture.update();
        currentTime(mMediaPlayer.getCurrentPosition());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMediaPlayer != null)
            mMediaPlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMediaPlayer != null)
            mMediaPlayer.start();
    }

    public void mpPause(){
        mMediaPlayer.pause();
    }

    public void mpResume(){
        mMediaPlayer.start();
    }

    @Override
    public void onRenderSurfaceDestroyed(SurfaceTexture surfaceTexture) {
        super.onRenderSurfaceDestroyed(surfaceTexture);
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent");
    }

    @Override
    public float getScreenHeight() {
        return mScreenHeight;
    }

    @Override
    public float getAngleX() {
        return angleX;
    }

    @Override
    public float getAngleY() {
        return angleY;
    }

    @Override
    public void setAngleX(float x) {
        angleX = x;
    }

    @Override
    public void setAngleY(float y) {
        angleY = y;
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);
        mScreenHeight = height;
        mScreenWidth = width;
    }
}
