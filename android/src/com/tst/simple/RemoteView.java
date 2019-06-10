package com.tst.simple;

import android.app.Activity;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.view.Gravity;

public class RemoteView {
    private final Activity mactivity;
    private final ViewGroup mviewGroup;
    private TextView textView;
    private static final int APP_STATE_CREATE = 0;
    private static final int APP_STATE_START = 1;
    private static final int APP_STATE_STOP = 2;
    private static final int APP_STATE_DESTROY = 3;


    public RemoteView(Activity instance){
        mactivity=instance;
        mviewGroup= (ViewGroup) mactivity.getWindow().getDecorView().getRootView();
    }

public void appStateChanged(int newState)
{
    switch(newState)
    {
        case APP_STATE_CREATE:
            createRemoteView();
            break;
        case APP_STATE_DESTROY:
            destroyRemoteView();
            break;
    }
}

    public void createRemoteView(){
        if (textView!=null)return;
        mactivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final FrameLayout.LayoutParams LayoutParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                textView=new TextView(mactivity);
                textView.setLayoutParams(LayoutParams);

                textView.setText("Simple Android");
                textView.setHeight(400);
                textView.setWidth(400);
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundColor(Color.GRAY);
                mviewGroup.addView(textView);
            }
        });
    }


    public void destroyRemoteView(){
        if (textView!=null)return;
        mactivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mviewGroup.removeView(textView);
                textView=null;
            }
        });
    }
}
