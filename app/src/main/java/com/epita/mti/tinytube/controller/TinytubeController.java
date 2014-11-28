package com.epita.mti.tinytube.controller;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.epita.mti.tinytube.app.AppController;
import com.epita.mti.tinytube.model.TinytubeModel;
import com.epita.mti.tinytube.request.JacksonRequest;
import com.epita.mti.tinytube.request.JacksonRequestCallback;
import com.fasterxml.jackson.databind.JavaType;

/**
 * Created by _Gary_ on 28/11/2014.
 */
public class TinytubeController extends Controller<TinytubeModel> {

    public void search(final ControllerCallback callback) {
        AppController.getInstance().addToRequestQueue(new JacksonRequest<>(Request.Method.GET, "/youtube_api_search.json", new JacksonRequestCallback<TinytubeModel>() {
            @Override
            public void onResponse(TinytubeModel response, int statusCode, VolleyError error) {
                if (error != null)
                    callback.onError(error);
                else
                    callback.onResponse(response);
            }

            @Override
            public JavaType getReturnType() {
                return JacksonRequest.getObjectMapper().getTypeFactory().constructType(TinytubeModel.class);
            }
        }));
    }
}
