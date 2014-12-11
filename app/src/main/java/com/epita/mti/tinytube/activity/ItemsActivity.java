package com.epita.mti.tinytube.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.epita.mti.tinytube.R;
import com.epita.mti.tinytube.adapter.ScreenSlidePagerAdapter;
import com.epita.mti.tinytube.model.Items;
import com.epita.mti.tinytube.model.TinyTubeModel.Item;
import com.epita.mti.tinytube.request.JacksonRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class ItemsActivity extends ActionBarActivity {

    private static final String TAG = ItemsActivity.class.getSimpleName();

    public static final String EXTRAS_ITEM_PLACE = "item_place";
    public static final String EXTRAS_ITEMS = "items";

    private int mItemPlace;
    public String mItemsString;
    private ArrayList<Item> mItems;
    public HashMap<String, Items> mItemsByChannel;


    public ArrayList<Item> getItems() {
        return mItems;
    }

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    public ItemsActivity() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRAS_ITEM_PLACE, mItemPlace);
        outState.putString(EXTRAS_ITEMS, mItemsString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItemsByChannel = new HashMap<>();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (savedInstanceState == null) {
            mItemPlace = getIntent().getIntExtra(EXTRAS_ITEM_PLACE, -1);
            mItemsString = getIntent().getStringExtra(EXTRAS_ITEMS);
        } else {
            mItemPlace = savedInstanceState.getInt(EXTRAS_ITEM_PLACE);
            mItemsString = savedInstanceState.getString(EXTRAS_ITEMS);
        }

        try {
            mItems = new ArrayList<>(JacksonRequest.getObjectMapper().readValue(mItemsString, Items.class).getItems());

            for (Item i : mItems) {
                if (!mItemsByChannel.containsKey(i.getSnippet().getChannelTitle())) {
                    Items items = new Items();
                    items.setItems(new ArrayList<Item>());
                    mItemsByChannel.put(i.getSnippet().getChannelTitle(), items);
                }

                mItemsByChannel.get(i.getSnippet().getChannelTitle()).getItems().add(i);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        if (mItemPlace < -1) {
            Log.e(TAG, "There is no item_id added as extra. Unable to launch activity.");
            this.finish();
        }

        setContentView(R.layout.activity_items);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.items_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager(), mItems);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(mItemPlace);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        } else if (id == R.id.action_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);

            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.share_text));
            sb.append(" : ");
            sb.append(getString(R.string.base_uri));
            sb.append(mItems.get(mPager.getCurrentItem()).getId().getVideoId());

            sendIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

