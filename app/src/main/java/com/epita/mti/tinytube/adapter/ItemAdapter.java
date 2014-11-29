package com.epita.mti.tinytube.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epita.mti.tinytube.R;
import com.epita.mti.tinytube.design.PaletteTransformation;
import com.epita.mti.tinytube.model.TinytubeModel.Item;
import com.epita.mti.tinytube.util.DateUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrollin on 28/11/14.
 */
public class ItemAdapter extends ArrayAdapter<Item> {
    private static final String TAG = ItemAdapter.class.getSimpleName();

    private LayoutInflater mInflater;
    private ArrayList<Item> mItems;

    public ItemAdapter(Context context, int textViewResourceId, List<Item> items) {
        super(context, textViewResourceId, items);
        this.mItems = new ArrayList<>(items);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<Item> getItems() {
        return mItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final Holder holder;

        if (view == null) {
            // View doesn't exist so create it and create the holder
            view = mInflater.inflate(R.layout.item_preview, parent, false);

            holder = new Holder();
            holder.ivThumbnail = (ImageView) view.findViewById(R.id.item_thumbnail);
            holder.tvTitle = (TextView) view.findViewById(R.id.item_title);
            holder.tvDate = (TextView) view.findViewById(R.id.item_date);
            holder.tvChannel = (TextView) view.findViewById(R.id.item_channel);

            view.setTag(holder);
        } else {
            // Just get our existing holder
            holder = (Holder) view.getTag();
        }

        // Populate via the holder for speed
        Item item = getItem(position);

        // Populate the item contents
        holder.tvTitle.setText(item.getSnippet().getTitle());
        holder.tvDate.setText(DateUtil.timeAgoInWords(item.getSnippet().getPublishedAt()));
        holder.tvChannel.setText(item.getSnippet().getChannelTitle());

        // Load the image on a background thread
        final PaletteTransformation paletteTransformation = PaletteTransformation.instance();
        Picasso.with(holder.ivThumbnail.getContext()).load(item.getSnippet().getThumbnails().getDefault().getUrl())
                .fit().centerCrop()
                .transform(paletteTransformation)
                .into(holder.ivThumbnail, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) holder.ivThumbnail.getDrawable()).getBitmap();
                        Palette palette = PaletteTransformation.getPalette(bitmap);

                        holder.ivThumbnail.setBackgroundColor(palette.getVibrantColor(android.R.color.white));
                        holder.ivThumbnail.getBackground().setAlpha(getContext().getResources().getInteger(R.integer.picasso_alpha));
                    }
                });

        return view;
    }

    // Holder class used to efficiently recycle view positions
    private static final class Holder {
        public ImageView ivThumbnail;
        public TextView tvTitle;
        public TextView tvDate;
        public TextView tvChannel;
    }
}
