package com.epita.mti.tinytube.request;

import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by _Gary_ on 28/11/2014.
 * Simple stack to use OkHttp with Volley
 */
public class OkHttpStack extends HurlStack {
    /**
     * The TAG for logs
     */
    private static final String TAG = OkHttpStack.class.getSimpleName();

    /**
     * The url factory
     */
    private final OkUrlFactory okUrlFactory;

    public OkHttpStack() {
        this(new OkUrlFactory(new OkHttpClient()));
    }

    public OkHttpStack(OkUrlFactory okUrlFactory) {
        if (okUrlFactory == null) {
            throw new NullPointerException("Client must not be null.");
        }
        this.okUrlFactory = okUrlFactory;
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        return okUrlFactory.open(url);
    }
}
