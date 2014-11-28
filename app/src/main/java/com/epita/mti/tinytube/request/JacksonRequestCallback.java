package com.epita.mti.tinytube.request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fasterxml.jackson.databind.JavaType;

/**
 * Created by _Gary_ on 28/11/2014.
 * A request callback for Jackson request
 */
public abstract class JacksonRequestCallback<T> {
    /**
     * The TAG for logs
     */
    private static final String TAG = JacksonRequestCallback.class.getSimpleName();

    /**
     * Called when the network call has returned and the result has been parsed
     *
     * @param response The parsed response, or null if an error occurred
     * @param statusCode The status code of the response
     * @param error The error that occurred, or null if successful
     */
    public abstract void onResponse(final T response, final int statusCode, final VolleyError error);

    /**
     * Called by the library to get the {@link com.fasterxml.jackson.databind.JavaType}
     * used to parse the network response. For simple POJOs, return a
     * {@link com.fasterxml.jackson.databind.type.SimpleType}. For lists and arrays,
     * return one of the values constructed using {@link com.fasterxml.jackson.databind.type.TypeFactory}
     *
     * @return The type that the network response should be parsed into.
     */
    public abstract JavaType getReturnType();

    /**
     * Optional method that is called on the networking thread used to further process
     * responses before delivering them to the UI thread.
     */
    public Response<T> onParseResponseComplete(final Response<T> response) {
        return response;
    }
}
