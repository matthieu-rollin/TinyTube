package com.epita.mti.tinytube.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epita.mti.tinytube.R;
import com.epita.mti.tinytube.activity.ChannelActivity;
import com.epita.mti.tinytube.activity.ItemsActivity;
import com.epita.mti.tinytube.adapter.ItemAdapter;
import com.epita.mti.tinytube.controller.ControllerCallback;
import com.epita.mti.tinytube.controller.TinyTubeController;
import com.epita.mti.tinytube.model.Items;
import com.epita.mti.tinytube.model.TinyTubeModel;
import com.epita.mti.tinytube.model.TinyTubeModel.Item;
import com.epita.mti.tinytube.request.JacksonRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 */
public class ChannelFragment extends Fragment {
    private static final String TAG = ChannelFragment.class.getSimpleName();

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ItemAdapter mAdapter;


    private Items mItems;
    private ArrayList<Item> mItemsArray;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChannelFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() == null) {
            Log.e(TAG, "getArguments() is null");
            getActivity().finish();
        }

        mAdapter = new ItemAdapter(getActivity(), 0, new ArrayList<Item>());
        mItems = new Items();
        mItemsArray = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_channel, container, false);

        try {
            mItems = JacksonRequest.getObjectMapper().readValue(getArguments().getString(ItemFragment.EXTRAS_ITEMS), Items.class);
            mItemsArray = new ArrayList<>(mItems.getItems());

            ((ChannelActivity)getActivity()).getSupportActionBar().setTitle(mItemsArray.get(0).getSnippet().getChannelTitle());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        mAdapter.addAll(mItemsArray);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ItemsActivity.class);
                intent.putExtra(ItemsActivity.EXTRAS_ITEM_PLACE, position);

                try {
                    intent.putExtra(ItemsActivity.EXTRAS_ITEMS, JacksonRequest.getObjectMapper().writeValueAsString(mItems));
                    startActivity(intent);
                } catch (JsonProcessingException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        return view;
    }
}
