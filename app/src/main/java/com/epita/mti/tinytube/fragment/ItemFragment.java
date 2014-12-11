package com.epita.mti.tinytube.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epita.mti.tinytube.R;
import com.epita.mti.tinytube.activity.ChannelActivity;
import com.epita.mti.tinytube.activity.HomeActivity;
import com.epita.mti.tinytube.activity.ItemsActivity;
import com.epita.mti.tinytube.design.PaletteTransformation;
import com.epita.mti.tinytube.model.TinyTubeModel.Item;
import com.epita.mti.tinytube.request.JacksonRequest;
import com.epita.mti.tinytube.request.RequestConfig;
import com.epita.mti.tinytube.util.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * A fragment representing an item.
 */
public class ItemFragment extends Fragment {
    private static final String TAG = ItemFragment.class.getSimpleName();

    public static final String EXTRAS_ITEM_ID = "item_id";
    public static final String EXTRAS_ITEMS = "items";

    private int mItemId;
    private Item mItem;

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
        outState.putInt(EXTRAS_ITEM_ID, mItemId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() == null) {
            Log.e(TAG, "getArguments() is null");
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        if (savedInstanceState == null)
            mItemId = getArguments().getInt(EXTRAS_ITEM_ID);
        else
            mItemId = savedInstanceState.getInt(EXTRAS_ITEM_ID);

        if (mItemId > -1 && getActivity() instanceof ItemsActivity)
            mItem = ((ItemsActivity)getActivity()).getItems().get(mItemId);

        // Fill data
        ((TextView)view.findViewById(R.id.item_title)).setText(mItem.getSnippet().getTitle());
        final TextView itemChannel = (TextView)view.findViewById(R.id.item_channel);
        itemChannel.setText(mItem.getSnippet().getChannelTitle());
        itemChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChannelActivity.class);
                try {
                    intent.putExtra(ItemFragment.EXTRAS_ITEMS,
                            JacksonRequest.getObjectMapper().writeValueAsString(
                                    ((ItemsActivity)getActivity()).mItemsByChannel.get(itemChannel.getText())));
                    startActivity(intent);
                } catch (JsonProcessingException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });


        ((TextView)view.findViewById(R.id.item_date)).setText(DateUtil.timeAgoInWords(mItem.getSnippet().getPublishedAt()));
        ((TextView)view.findViewById(R.id.item_description)).setText(mItem.getSnippet().getDescription());

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

        ivThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(getActivity()).equals(YouTubeInitializationResult.SUCCESS)) {
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent(
                            getActivity(),
                            RequestConfig.YOUTUBE_API_KEY,
                            mItem.getId().getVideoId(),
                            0,
                            true,
                            getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);

                    getActivity().startActivity(intent);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(getString(R.string.base_uri));
                    sb.append(mItem.getId().getVideoId());

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString())));
                }
            }
        });

        return view;
    }
}
