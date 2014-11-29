package com.epita.mti.tinytube.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.epita.mti.tinytube.R;
import com.epita.mti.tinytube.activity.ItemsActivity;
import com.epita.mti.tinytube.design.ListenableVideoView;
import com.epita.mti.tinytube.design.PaletteTransformation;
import com.epita.mti.tinytube.model.TinytubeModel.Item;
import com.epita.mti.tinytube.request.JacksonRequest;
import com.epita.mti.tinytube.util.DateUtil;
import com.epita.mti.tinytube.util.UrlUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * A fragment representing an item.
 */
public class ItemFragment extends Fragment {
    private static final String TAG = ItemFragment.class.getSimpleName();

    public static final String EXTRAS_ITEM_ID = "item_id";
    public static final String ITEM = "item";
    public static final String POSITION = "position";

    private int mItemId;
    private Item mItem;
    private String mVideoUrl;
    private int mPosition;
    private ListenableVideoView mVideoView;
    private View mThumbnail;
    private MediaController mMediaController;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param itemId Parameter the item id.
     * @return A new instance of fragment ItemFragment.
     */
    public static ItemFragment newInstance(int itemId) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRAS_ITEM_ID, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, mVideoView.getCurrentPosition());
        mVideoView.pause();
        try {
            outState.putString(ITEM, JacksonRequest.getObjectMapper().writeValueAsString(mItem));
        } catch (JsonProcessingException e) { Log.e(TAG, e.getMessage()); }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() == null) {
            Log.e(TAG, "getArguments() is null");
            getActivity().finish();
        }

        if (savedInstanceState == null) {
            mItemId = getArguments().getInt(EXTRAS_ITEM_ID);

            if (mItemId > -1 && getActivity() instanceof ItemsActivity)
                mItem = ((ItemsActivity)getActivity()).getItems().get(mItemId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt(POSITION);
            try {
                mItem = JacksonRequest.getObjectMapper().readValue(savedInstanceState.getString(ITEM), Item.class);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        } else
            mPosition = 0;

        // Fill data
        ((TextView)view.findViewById(R.id.item_title)).setText(mItem.getSnippet().getTitle());
        ((TextView)view.findViewById(R.id.item_channel)).setText(mItem.getSnippet().getChannelTitle());
        ((TextView)view.findViewById(R.id.item_date)).setText(DateUtil.timeAgoInWords(mItem.getSnippet().getPublishedAt()));
        ((TextView)view.findViewById(R.id.item_description)).setText(mItem.getSnippet().getDescription());

        mThumbnail = view.findViewById(R.id.layout_thumbnail);
        mVideoView = (ListenableVideoView)view.findViewById(R.id.item_video);

        mMediaController = new MediaController(getActivity());
        new VideoAsyncTask().execute();

        // Load the image on a background thread

        final ImageView ivThumbnail = (ImageView)view.findViewById(R.id.item_thumbnail);
        final PaletteTransformation paletteTransformation = PaletteTransformation.instance();
        Picasso.with(ivThumbnail.getContext()).load(mItem.getSnippet().getThumbnails().getHigh().getUrl())
                .fit().centerCrop()
                .transform(paletteTransformation)
                .into(ivThumbnail, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) ivThumbnail.getDrawable()).getBitmap();
                        Palette palette = PaletteTransformation.getPalette(bitmap);

                        ivThumbnail.setBackgroundColor(palette.getVibrantColor(android.R.color.white));

                        if (isAdded())
                            ivThumbnail.getBackground().setAlpha(getActivity().getResources().getInteger(R.integer.picasso_alpha));
                    }
                });

        return view;
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     */
    @Override
    public void onResume() {
        super.onResume();
        mVideoView.seekTo(mPosition);
    }

    private class VideoAsyncTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            try {
                String url = mItem.getId().getVideoId();
                mVideoUrl = UrlUtil.getUrlVideoRTSP(url);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if (!isAdded())
                return;

            mVideoView.setVideoURI(Uri.parse(mVideoUrl));
            mMediaController.setAnchorView(mVideoView);
            mMediaController.setMediaPlayer(mVideoView);
            mVideoView.setMediaController(mMediaController);
            mVideoView.requestFocus();
            mVideoView.seekTo(mPosition);

            mVideoView.setPlayPauseListener(new ListenableVideoView.PlayPauseListener() {
                @Override
                public void onPlay() {
                    mThumbnail.animate().alpha(0).start();
                }

                @Override
                public void onPause() {
                    mThumbnail.animate().alpha(255).start();
                }
            });
        }

    }
}
