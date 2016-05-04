package com.neointernet.neo360.renderer;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;

import com.google.vrtoolkit.cardboard.Eye;
import com.neointernet.neo360.activity.VideoActivity;
import com.neointernet.neo360.listener.CardboardEventListener;
import com.neointernet.neo360.listener.VideoTimeListener;

import org.rajawali3d.cardboard.RajawaliCardboardRenderer;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.StreamingTexture;
import org.rajawali3d.math.Matrix4;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;

public class VideoRenderer extends RajawaliCardboardRenderer implements CardboardEventListener {


    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private VideoActivity videoActivity;
    private String videoPath;
    private int mode = NONE;
    private float previousX, posX, previousY, posY;
    private float distance1, distance2;
    private VideoTimeListener listener;
    private MediaPlayer mediaPlayer;
    private StreamingTexture streamingTexture;

    private long elapsedRealtime;
    private double deltaTime;
    private Matrix4 eyeMatrix = new Matrix4();
    private Quaternion eyeQuaternion = new Quaternion();
    private Quaternion rotateQuaternionX = new Quaternion();
    private Quaternion rotateQuaternionY = new Quaternion();

    private float angleX = 0;
    private float angleY = 0;
    private float angleZ = 0;
    private final static String TAG = "VideoRenderer.class";

    public VideoRenderer(Activity activity) {
        super(activity.getApplicationContext());
        videoActivity = (VideoActivity) activity;
    }

    public VideoRenderer(Activity activity, String videoPath) {
        super(activity.getApplicationContext());
        this.videoPath = videoPath;
        videoActivity = (VideoActivity) activity;
    }

    @Override
    protected void initScene() {
//        File file = new File(videoPath);
//        Uri uri = Uri.fromFile(file);
//        Log.i("URI", uri.toString());
        mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(videoPath));
        mediaPlayer.setLooping(true);

        streamingTexture = new StreamingTexture("texture", mediaPlayer);
        Material material = new Material();
        material.setColorInfluence(0);
        try {
            material.addTexture(streamingTexture);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }

        Sphere sphere = new Sphere(100, 128, 64);
        sphere.setScaleX(-1);
        sphere.setMaterial(material);

        getCurrentScene().addChild(sphere);
        getCurrentCamera().setPosition(Vector3.ZERO);
        getCurrentCamera().setFieldOfView(75);

        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i("Media Player Status", "Completed");
                mp.stop();
                mp.release();
                videoActivity.finish();
            }
        });

        notifyVideoInit(mediaPlayer.getDuration());
    }

    public void setVideoTimeListener(VideoTimeListener listener) {
        this.listener = listener;
    }

    public void notifyVideoInit(int length) {
        listener.onVideoInit(length);
    }

    public void notifyTime(int time) {
        listener.listenTime(time);
    }

    public void setMediaPlayerSeekTo(int progress) {
        mediaPlayer.seekTo(progress);
    }

    @Override
    protected void onRender(long elapsedRealTime, double deltaTime) {
        this.elapsedRealtime = elapsedRealTime;
        this.deltaTime = deltaTime;
        streamingTexture.update();
        notifyTime(mediaPlayer.getCurrentPosition());
    }

    @Override
    public void onPause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    public void onRenderSurfaceDestroyed(SurfaceTexture surfaceTexture) {
        super.onRenderSurfaceDestroyed(surfaceTexture);
        mediaPlayer.release();
    }

    @Override
    public void onCardboardTouch(MotionEvent e) {
        int action = e.getAction();
        double fieldOfView = getCurrentCamera().getFieldOfView();

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                previousX = e.getX();
                previousY = e.getY();
                mode = DRAG;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    posX = e.getX();
                    posY = e.getY();
//                    Log.i(TAG, "posX : " + String.valueOf(posX) + " posY" + String.valueOf(posY));

                    if (posX - previousX > 0) {
                        angleX = angleX + (posX - previousX) / 10;
                    } else if (previousX - posX > 0) {
                        angleX = angleX - (previousX - posX) / 10;
                    }
//                    Log.i(TAG, "angleX : " + String.valueOf(angleX));

                    if (posY - previousY > 0) {
                        angleY = angleY + (posY - previousY) / 10;

                    } else if (previousY - posY > 0) {
                        angleY = angleY - (previousY - posY) / 10;
                    }
//                    Log.i(TAG, "angleY : " + String.valueOf(angleY));

                    if (Math.abs(posX - previousX) > 15 || Math.abs(posY - previousY) > 15) {
                        previousX = posX;
                        previousY = posY;
                    }
//                    Log.i(TAG, "previousX : " + String.valueOf(previousX) + " previousY" + String.valueOf(previousX));
                } else if (mode == ZOOM) {
                    distance1 = calculateDistance(e);
                    if (distance1 - distance2 > 0) {
                        if (fieldOfView < 130) {
                            fieldOfView = fieldOfView + (distance1 - distance2) / 10;
                            getCurrentCamera().setFieldOfView(fieldOfView);
                        }
                        distance2 = distance1;
                    } else if (distance2 - distance1 > 0) {
                        if (fieldOfView > 20) {
                            fieldOfView = fieldOfView - (distance2 - distance1) / 10;
                            getCurrentCamera().setFieldOfView(fieldOfView);
                        }
                        distance2 = distance1;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                distance1 = calculateDistance(e);
                distance2 = calculateDistance(e);
                break;
            case MotionEvent.ACTION_CANCEL:
            default:
                break;
        }
    }

    private float calculateDistance(MotionEvent e) {
        float x = e.getX(0) - e.getX(1);
        float y = e.getY(0) - e.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }


    @Override
    public void onDrawEye(Eye eye) {
        eyeMatrix.setAll(eye.getEyeView());
        eyeQuaternion.fromMatrix(eyeMatrix);
//        rotateQuaternionX.fromAngleAxis(0, -2.0f, 0, angleX);
//        Log.i(TAG, "X : w : " + rotateQuaternionX.w + " x : " + rotateQuaternionX.x + " y : " + rotateQuaternionX.y + " z : " + rotateQuaternionX.z);
//        rotateQuaternionY.fromAngleAxis(-2.0f, 0, 0, angleY);
//        Log.i(TAG, "Y : w : " + rotateQuaternionY.w + " x : " + rotateQuaternionY.x + " y : " + rotateQuaternionY.y + " z : " + rotateQuaternionY.z);
        getCurrentCamera().setOrientation(eyeQuaternion);
//        Log.i(TAG, "E : w : " + eyeQuaternion.w + " x : " + eyeQuaternion.x + " y : " + eyeQuaternion.y + " z : " + eyeQuaternion.z);
//        getCurrentCamera().rotate(rotateQuaternionX);
//        getCurrentCamera().rotate(rotateQuaternionY);
        render(elapsedRealtime, deltaTime);
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }
}