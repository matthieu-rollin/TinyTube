package com.epita.mti.tinytube.design;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by mrollin on 29/11/14.
 */
public class ListenableVideoView extends VideoView {

    private PlayPauseListener mListener;

    public ListenableVideoView(Context context) {
        super(context);
    }

    public ListenableVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListenableVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setPlayPauseListener(PlayPauseListener listener) {
        mListener = listener;
    }

    @Override
    public void pause() {
        super.pause();
        if (mListener != null) {
            mListener.onPause();
        }
    }

    @Override
    public void start() {
        super.start();
        if (mListener != null) {
            mListener.onPlay();
        }
    }

    public static interface PlayPauseListener {
        void onPlay();
        void onPause();
    }

}
