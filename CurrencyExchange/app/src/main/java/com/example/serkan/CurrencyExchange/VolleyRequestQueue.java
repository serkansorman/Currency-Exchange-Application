package com.example.serkan.CurrencyExchange;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyRequestQueue {

    private RequestQueue mRequestQueue;

    /**
     * Creates a request queue using Volley
     * @param context current activity context
     */
    public VolleyRequestQueue(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Add given request to queue
     * @param request request that added to queue
     * @param <T> Generic request type (JSON object or JSOn array)
     */
    public <T> void addToRequestQueue(Request<T> request) {
        mRequestQueue.add(request);
    }

}