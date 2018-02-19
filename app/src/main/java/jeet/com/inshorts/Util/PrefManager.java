package jeet.com.inshorts.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Map;

/**
 * Created by jeet on 10/9/17.
 */

public class PrefManager {
    private SharedPreferences sharedPreferences;
    private Context context;
    private static final String pref_name="InShorts";

    public PrefManager(@NonNull Context context)
    {
        this.context=context;
        sharedPreferences=context.getSharedPreferences(pref_name,Context.MODE_PRIVATE);
    }

    public void setFilter1(String json)
    {
        sharedPreferences.edit().putString("FILTER",json).commit();
    }
    public String getFilter1()
    {
        return sharedPreferences.getString("FILTER","");
    }

    public void setFilter2(String json)
    {
        sharedPreferences.edit().putString("FILTER2",json).commit();
    }
    public String getFilter2()
    {
        return sharedPreferences.getString("FILTER2","");
    }

}
