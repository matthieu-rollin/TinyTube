package com.epita.mti.tinytube.request;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpStatus;

import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by _Gary_ on 28/11/2014.
 * A Jackson request, based on the Volley request
 */
public class JacksonRequest<T> extends Request<T> {
    /**
     * The TAG for logs
     */
    private static final String TAG = JacksonRequest.class.getSimpleName();

    /**
     * Jackson Object mapper
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * The params of the request
     */
    private Map<String, String> mParams;

    /**
     * The callback of the request
     */
    private final JacksonRequestCallback<T> mCallback;

    /**
     * Constructor without request params
     * @param method The request method
     * @param url The request url without the endpoint
     * @param callback the request callback
     */
    public JacksonRequest(final int method, final String url, final JacksonRequestCallback<T> callback) {
        this(method, url, null, callback);
    }

    /**
     * Constructor with request params
     * @param method The request method
     * @param url The request url without the endpoint
     * @param params The request params
     * @param callback the request callback
     */
    public JacksonRequest(final int method, final String url, final Map<String, String> params, final JacksonRequestCallback<T> callback) {
        super(method, buildUrl(method, url, params), null);

        mCallback = callback;

        if (method == Method.POST || method == Method.PUT)
            mParams = params;
    }

    /**
     * Get the Jackson ObjectMapper instance
     * @return the object mapper
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * Build the url properly
     * @param method the type of the method (GET, POST, PUT, DELETE)
     * @param url the base url
     * @param params the params of the url if it's GET method (?foo=bar&fooo=barr)
     * @return the url well formed
     */
    private static String buildUrl(final int method, final String url, final Map<String, String> params) {
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() == null || entry.getValue().equals("null")) {
                    entry.setValue("");
                }
            }
        }

        if (method == Method.GET && params != null && !params.isEmpty()) {
            final StringBuilder result = new StringBuilder(RequestConfig.ENDPOINT + url);
            final int startLength = result.length();

            for (String key : params.keySet()) {
                try {
                    final String encodedKey = URLEncoder.encode(key, "UTF-8");
                    final String encodedValue = URLEncoder.encode(params.get(key), "UTF-8");

                    if (result.length() > startLength)
                        result.append("&");
                    else
                        result.append("?");

                    result.append(encodedKey);
                    result.append("=");
                    result.append(encodedValue);
                } catch (Exception e) { }
            }
            return result.toString();
        }

        return RequestConfig.ENDPOINT + url;
    }

    /**
     * Deliver the response using the callback
     * @param response The request response already parsed by Jackson
     */
    @Override
    protected void deliverResponse(final T response) {
        mCallback.onResponse(response, HttpStatus.SC_OK, null);
    }

    /**
     * Deliver an error if one occur
     * @param error The error
     */
    @Override
    public void deliverError(final VolleyError error) {
        int statusCode;
        if (error != null && error.networkResponse != null) {
            statusCode = error.networkResponse.statusCode;
        } else {
            statusCode = 0;
        }

        mCallback.onResponse(null, statusCode, error);
    }

    /**
     * Converts the JSON into an object using Jackson
     * @param response The network response
     * @return The response with the type asked
     */
    @Override
    protected Response<T> parseNetworkResponse(final NetworkResponse response) {
        JavaType returnType = mCallback.getReturnType();
        T returnData = null;

        if (returnType != null) {
            try {
                if (response.data != null) {
                    returnData = getObjectMapper().readValue(response.data, returnType);
                } else if (response instanceof JacksonNetworkResponse) {
                    returnData = getObjectMapper().readValue(((JacksonNetworkResponse) response).inputStream, returnType);
                }
            } catch (Exception e) {
                VolleyLog.e(e, "An error occurred while parsing network response:");
                return Response.error(new ParseError(response));
            }
        }
        return mCallback.onParseResponseComplete(Response.success(returnData, HttpHeaderParser.parseCacheHeaders(response)));
    }

    /**
     * Sets the params of the request
     * @return The request params
     */
    @Override
    public Map<String, String> getParams() {
        return mParams;
    }
}
