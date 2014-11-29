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
import com.epita.mti.tinytube.controller.TinytubeController;
import com.epita.mti.tinytube.model.TinytubeModel;
import com.epita.mti.tinytube.model.TinytubeModel.Item;
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
    private TinytubeModel mResponse;

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
                    intent.putExtra(ItemsActivity.EXTRAS_ITEMS, JacksonRequest.getObjectMapper().writeValueAsString(mResponse));
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
        TinytubeController controller = new TinytubeController();
        controller.search(new ControllerCallback<TinytubeModel>() {
            @Override
            public void onResponse(TinytubeModel response) {
                mResponse = response;
                if (response.getItems().size() == 0)
                    setEmptyText(getString(R.string.empty_text));
                else
                    mAdapter.addAll(response.getItems());
            }

            @Override
            public void onError(Exception error) {
                Log.e(TAG, error.getMessage());
                final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                        .title(getString(R.string.dialog_title_error))
                        .content(getString(R.string.dialog_content_error))
                        .callback(new MaterialDialog.SimpleCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                dialog.cancel();
                            }
                        })
                        .build();
                dialog.show();
            }
        });
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }
}
