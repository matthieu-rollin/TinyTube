package com.epita.mti.tinytube.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import com.epita.mti.tinytube.model.TinyTubeModel.Item;

/**
 * Created by _Gary_ on 28/11/2014.
 * Mapping of the Items JSON for Jackson
 */
public class Items extends Model {
    /**
     * The TAG for logs
     */
    private static final String TAG = Items.class.getSimpleName();

    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
