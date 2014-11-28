package com.epita.mti.tinytube.app;

import android.app.Application;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.epita.mti.tinytube.request.RequestConfig;
import com.epita.mti.tinytube.request.OkHttpStack;

/**
 * Created by _Gary_ on 28/11/2014.
 * Application global singleton
 */
public class AppController extends Application {
    /**
     * The TAG for logs
     */
    private static final String TAG = AppController.class.getSimpleName();

    /**
     * The static instance of the application
     */
    private static AppController mInstance;

    /**
     * Volley request queue
     */
    private RequestQueue mRequestQueue;

    /**
     * Get the instance of the Application
     * @return The instance of the application
     */
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    /**
     * Actions executed when the app starts
     */
    @Override
    public void onCreate() {
        super.onCreate();

        if (mInstance == null) {
            mInstance = this;
            mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new OkHttpStack());
            mRequestQueue.start();
        }
    }

    /**
     * Add a request to the queue with a specific tag
     * @param req The request
     * @param tag The tag to use
     * @param <T> The type of the request
     */
    public <T> void addToRequestQueue(final Request<T> req, final String tag) {
        req.setRetryPolicy(new DefaultRetryPolicy(
                RequestConfig.REQUEST_TIMEOUT,
                RequestConfig.REQUEST_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        req.setTag(tag);
        mRequestQueue.add(req);
    }

    /**
     * Add a request to the queue
     * @param req The request
     * @param <T> The type of the request
     */
    public <T> void addToRequestQueue(final Request<T> req) {
        addToRequestQueue(req, "main");
    }

    /**
     * Cancel all requests without tag
     */
    public void cancelAllRequests() {
        cancelRequest("main");
    }

    /**
     * Cancel all requests with the specified tag
     * @param tag The tag to use
     */
    public void cancelRequest(final String tag) {
        mRequestQueue.cancelAll(tag);
    }
}
