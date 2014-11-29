package com.epita.mti.tinytube.adapter;

/**
 * Created by mrollin on 28/11/14.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.epita.mti.tinytube.fragment.ItemFragment;
import com.epita.mti.tinytube.model.TinytubeModel.Item;

import java.util.ArrayList;

/**
 * A simple pager adapter that represents the ItemFragment objects, in
 * sequence.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Item> items;

    public ScreenSlidePagerAdapter(FragmentManager fm, ArrayList<Item> items) {
        super(fm);
        this.items = items;
    }

    @Override
    public Fragment getItem(int position) {
       return ItemFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
