package com.samskrut.vrvideo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by HSH on 16. 3. 22..
 */
public class MyMediaController extends View {

    Context context;

    public MyMediaController(Context context) {
        super(context);
        this.context = context;
    }

    public MyMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyMediaController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyMediaController(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void makeController(){

        ViewGroup viewGroup = new ViewGroup(context) {
            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {

            }
        };
        Button operateBtn = new Button(context);
        Button cardboardBtn = new Button(context);

        operateBtn.setText("시작");
        cardboardBtn.setText("전환");

        viewGroup.addView(operateBtn);
        viewGroup.addView(cardboardBtn);

    }

}
