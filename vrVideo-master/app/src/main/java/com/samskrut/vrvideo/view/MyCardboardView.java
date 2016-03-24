package com.samskrut.vrvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.samskrut.vrvideo.listener.CardBoardTouchEventListener;

import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.IRajawaliSurfaceRenderer;

/**
 * Created by HSH on 16. 3. 24..
 */
public class MyCardboardView extends CardboardView implements IRajawaliSurface {

    private final static String TAG = "MyCardboardView";
    private CardBoardTouchEventListener cardBoardTouchEventListener;

    public MyCardboardView(Context context) {
        super(context);
    }

    public MyCardboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setListener(CardBoardTouchEventListener CBListner) {
        this.cardBoardTouchEventListener = CBListner;
    }

    private IRajawaliSurfaceRenderer renderer;


    @Override
    public void onPause() {
        super.onPause();
        renderer.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        renderer.onResume();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            onPause();
        } else {
            onResume();
        }
        super.onVisibilityChanged(changedView, visibility);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onResume();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        renderer.onRenderSurfaceDestroyed(null);
    }

    @Override
    public void setFrameRate(double rate) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void setAntiAliasingMode(ANTI_ALIASING_CONFIG config) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void setSampleCount(int count) {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void setSurfaceRenderer(IRajawaliSurfaceRenderer renderer) throws IllegalStateException {

        if (this.renderer != null)
            throw new IllegalStateException("A renderer has already been set for this view.");

        renderer.setRenderSurface(this);

        this.renderer = renderer;

        onPause(); // We want to halt the surface view until we are ready
    }

    @Override
    public void requestRenderUpdate() {
        requestRender();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.
        if(e.getAction() == MotionEvent.ACTION_DOWN){
            Log.e(TAG, "RajawliCardboardView onTouchEvent");
            cardBoardTouchEventListener.onTouch();
        }
        return true;
    }
}
