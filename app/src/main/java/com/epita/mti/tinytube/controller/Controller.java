package com.epita.mti.tinytube.controller;

import com.epita.mti.tinytube.model.Model;

/**
 * Created by _Gary_ on 28/11/2014.
 * Abstract Controller
 */
public abstract class Controller<T extends Model> {
    /**
     * The TAG for logs
     */
    private static final String TAG = Controller.class.getSimpleName();
}
