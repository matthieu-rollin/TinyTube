package com.epita.mti.tinytube.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.epita.mti.tinytube.R;
import com.epita.mti.tinytube.activity.ItemsActivity;
import com.epita.mti.tinytube.adapter.ItemAdapter;
import com.epita.mti.tinytube.controller.ControllerCallback;
import com.epita.mti.tinytube.controller.TinyTubeController;
import com.epita.mti.tinytube.model.Items;
import com.epita.mti.tinytube.model.TinyTubeModel;
import com.epita.mti.tinytube.model.TinyTubeModel.Item;
import com.epita.mti.tinytube.request.JacksonRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 */
public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ItemAdapter mAdapter;

    /**
     * The response get after the data loading
     */
    private TinyTubeModel mResponse;


    private MaterialDialog mProgressDialog;

    private MaterialDialog mErrorDialog;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ItemAdapter(getActivity(), 0, new ArrayList<Item>());

        mProgressDialog = new MaterialDialog.Builder(getActivity())
                .cancelable(false)
                .customView(R.layout.dialog_progess_bar)
                .hideActions()
                .build();

        mErrorDialog = new MaterialDialog.Builder(getActivity())
                .cancelable(false)
                .title(getString(R.string.dialog_title_error))
                .content(getString(R.string.dialog_content_error))
                .negativeText(getString(R.string.dialog_quit))
                .positiveText(getString(R.string.dialog_retry))
                .callback(new MaterialDialog.Callback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        loadData();
                        materialDialog.cancel();
                    }

                    @Override
                    public void onNegative(MaterialDialog materialDialog) {
                        getActivity().finish();
                    }
                })
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ItemsActivity.class);
                intent.putExtra(ItemsActivity.EXTRAS_ITEM_PLACE, position);
                try {
                    Items items = new Items();
                    items.setItems(mResponse.getItems());
                    intent.putExtra(ItemsActivity.EXTRAS_ITEMS, JacksonRequest.getObjectMapper().writeValueAsString(items));
                    startActivity(intent);
                } catch (JsonProcessingException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });

        loadData();

        return view;
    }

    private void loadData() {
        mProgressDialog.show();

        TinyTubeController controller = new TinyTubeController();
        controller.search(new ControllerCallback<TinyTubeModel>() {
            @Override
            public void onResponse(TinyTubeModel response) {
                mResponse = response;
                if (response.getItems().size() == 0)
                    setEmptyText(getString(R.string.empty_text));
                else
                    mAdapter.addAll(response.getItems());
                mProgressDialog.dismiss();
            }

            @Override
            public void onError(Exception error) {
                if (!isAdded())
                    return;

                mErrorDialog.show();
                mProgressDialog.dismiss();
            }
        });
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }
}
