package jeet.com.inshorts.Util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;



public class VolleySingleton {
    private static VolleySingleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;
    private ImageLoader mImageLoader;

    private VolleySingleton(final Context context)
    {
        ctx=context;
        requestQueue=getRequestQueue();


    }
    public static synchronized VolleySingleton getInstance(Context context)
    {
        if(instance==null)
        {
            instance=new VolleySingleton(context);
        }
        return instance;
    }
    public RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
        {
            requestQueue=Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().add(req);
    }
    public static boolean isNumericLong(String str)
    {
        try
        {
            Long.parseLong(str);
            return true;
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
    }

}
