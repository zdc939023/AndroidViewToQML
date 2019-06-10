package com.tst.simple;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdSize;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.AdListener;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Rect;
import android.widget.FrameLayout;
import android.graphics.Color;
import android.widget.TextView;

public class ScreenControl {



    private final Activity mActivityInstance;
    private final ViewGroup mViewGroup;

    TextView textView=null;
    private boolean mBannerLoaded = false;
//    private BannerSize mBannerPixelsSize = new BannerSize();

    public ScreenControl(Activity ActivityInstance)
    {
        mViewGroup = (ViewGroup)ActivityInstance.getWindow().getDecorView().getRootView();
        mActivityInstance = ActivityInstance;
    }

    public void setType(final int type)
    {
        if (textView==null){
            return;
        }
        if (mActivityInstance==null)return;
        mActivityInstance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText("type:"+type);
            }
        });
    }

    public void setPos(final BannerPos pos)
    {
        if(textView == null)
        {
            return;
        }
        mActivityInstance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                                Rect VisibleFrame = new Rect();

                mActivityInstance.getWindow().getDecorView().getWindowVisibleDisplayFrame(VisibleFrame);
                textView.setX(pos.x);
                textView.setY(VisibleFrame.top + pos.y); // Add the height of the system status bar on top
            }
        });
    }

    public void setUnitId(final String unitId)
    {
        if(textView == null)
        {
            return;
        }
        mActivityInstance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText("--:"+unitId);
            }
        });
    }

    public void show()
    {
        if(textView == null)
        {
            return;
        }

        mActivityInstance.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {

                textView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hide()
    {
        if(textView == null)
        {
            return;
        }

        mActivityInstance.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                textView.setVisibility(View.GONE);
            }
        });
    }

    public void reload()
    {
        destroyBanner();
        createBanner();
    }

    public void appStateChanged(int newState)
    {
        switch(newState)
        {
            case APP_STATE_CREATE:
                createBanner();
                break;
            case APP_STATE_START:
                if(textView != null)
                {
                    mActivityInstance.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            textView.setText("start");
                        }
                    });
                }
                break;
            case APP_STATE_STOP:
                if(textView != null)
                {
                    mActivityInstance.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            textView.setText("pause");
                        }
                    });
                }
                break;
            case APP_STATE_DESTROY:
                destroyBanner();
                break;
        }
    }

    private void createBanner()
    {
        if(textView != null)
        {
            return;
        }
        mActivityInstance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final FrameLayout.LayoutParams LayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                textView = new TextView(mActivityInstance);
                textView.setLayoutParams(LayoutParams);
                textView.setBackgroundColor(Color.BLACK);
                textView.setVisibility(View.GONE);
                mViewGroup.addView(textView);
            }
        });
    }

    private void destroyBanner()
    {
        if(textView == null)
        {
            return;
        }
        mActivityInstance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mViewGroup.removeView(textView);
                textView=null;
            }
        });
    }


    public static class BannerPos
    {
        public int x = 0;
        public int y = 0;
    }

    public static class BannerSize
    {
        public int width = 0;
        public int height = 0;
    }

    private static final int ERROR_INTERNAL = 0;
    private static final int ERROR_NETWORK = 1;
    private static final int ERROR_INVALID_REQUEST = 2;
    private static final int ERROR_NO_FILL = 3;

    private static final int EVENT_LOADING = 0;
    private static final int EVENT_LOADED = 1;
    private static final int EVENT_CLOSED = 2;
    private static final int EVENT_CLICKED = 3;

    private static final int TYPE_BANNER = 0;
    private static final int TYPE_FULL_BANNER = 1;
    private static final int TYPE_LARGE_BANNER = 2;
    private static final int TYPE_MEDIUM_RECTANGLE = 3;
    private static final int TYPE_SMART_BANNER = 4;
    private static final int TYPE_WIDE_SKYSCRAPER = 5;

    private static final int APP_STATE_CREATE = 0;
    private static final int APP_STATE_START = 1;
    private static final int APP_STATE_STOP = 2;
    private static final int APP_STATE_DESTROY = 3;

    private static native void bannerEvent(int eventId);
    private static native void bannerError(int errorId);
}
