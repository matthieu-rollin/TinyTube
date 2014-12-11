package com.epita.mti.tinytube.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.epita.mti.tinytube.R;
import com.epita.mti.tinytube.fragment.ChannelFragment;
import com.epita.mti.tinytube.fragment.ItemFragment;

public class ChannelActivity extends ActionBarActivity {

    String mItemsString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mItemsString = getIntent().getStringExtra(ItemFragment.EXTRAS_ITEMS);

        setContentView(R.layout.activity_channel);

        if (savedInstanceState == null) {
            ChannelFragment fragment = new ChannelFragment();
            Bundle b = new Bundle();
            b.putString(ItemFragment.EXTRAS_ITEMS, mItemsString);
            fragment.setArguments(b);
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_channel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

