package com.epita.mti.tinytube.request;

import com.android.volley.NetworkResponse;

import org.apache.http.HttpStatus;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by _Gary_ on 28/11/2014.
 * A Jackson response, based on the Volley network response
 */
public class JacksonNetworkResponse extends NetworkResponse {
    /**
     * The TAG for logs
     */
    private static final String TAG = JacksonNetworkResponse.class.getSimpleName();

    /**
     * The response stream
     */
    public InputStream inputStream;

    /**
     * Sets the network response values and its stream
     * @param statusCode The status code
     * @param dataIfCached The data
     * @param inputStream The stream
     * @param headers The headers
     */
    public JacksonNetworkResponse(final int statusCode, final byte[] dataIfCached, final InputStream inputStream, final Map<String, String> headers) {
        super(statusCode, dataIfCached, headers, statusCode == HttpStatus.SC_NOT_MODIFIED);
        this.inputStream = inputStream;
    }
}
