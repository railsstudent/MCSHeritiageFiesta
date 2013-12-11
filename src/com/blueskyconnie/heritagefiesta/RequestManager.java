package com.blueskyconnie.heritagefiesta;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

// copy from https://github.com/rdrobinson3/VolleyImageCacheExample/blob/master/CaptechBuzz/src/com/captechconsulting/captechbuzz/model/RequestManager.java
public class RequestManager {
	 
    /**
     * the queue :-)
     */
    private static RequestQueue mRequestQueue;

    /**
     * Nothing to see here.
     */
    private RequestManager() {
     // no instances
    } 

    /**
     * @param context
     *                         application context
     */
    public static void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * @return
     *                 instance of the queue
     * @throws
     *                 IllegalStatException if init has not yet been called
     */
    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("Not initialized");
        }
    }
}
