package com.epita.mti.tinytube.controller;

import com.epita.mti.tinytube.model.Model;

/**
 * Created by _Gary_ on 28/11/2014.
 * Callback for controllers
 */
public abstract class ControllerCallback<T extends Model> {
    /**
     * Called when a model is received after an API request.
     * @param response The requested model response
     */
    public abstract void onResponse(final T response);

    /**
     * Called when an error is received after an API request
     * @param error The error received
     */
    public abstract void onError(final Exception error);
}
