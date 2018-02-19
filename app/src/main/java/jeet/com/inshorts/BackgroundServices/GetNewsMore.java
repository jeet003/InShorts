package jeet.com.inshorts.BackgroundServices;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jeet.com.inshorts.Interfaces.Volley;
import jeet.com.inshorts.Models.NewsModel;
import jeet.com.inshorts.Util.VolleySingleton;


/**
 * Created by jeet on 10/9/17.
 */
public class GetNewsMore {
    private Context context;
    private String url;
    private String rtrnValue;
    private String Tag="News";
    private Volley.GetNewsMorw GetNews;
    private int value;


    public GetNewsMore(Context context, Volley.GetNews listener,int value){
        this.context=context;
        url="http://starlord.hackerearth.com/newsjson";
        try {
            GetNews=(Volley.GetNewsMorw) listener;
        }
        catch (ClassCastException e){
            throw new ClassCastException();
        }
        this.value=value;

    }
    public void loadNews()
    {

        StringRequest newsRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response!=null){
                    Log.d("response",response);
                    try {
                        JSONArray newsDetails=new JSONArray(response);
                            rtrnValue="SUCCESS";
                            ArrayList<NewsModel> newsModels=new ArrayList<NewsModel>();

                            for(int i=value;i<newsDetails.length() && i<value+20;i++)
                            {
                                JSONObject responseObj=newsDetails.getJSONObject(i);

                                String ID = responseObj.getString("ID");
                                String TITLE = responseObj.getString("TITLE");
                                String URL = responseObj.getString("URL");
                                String PUBLISHER = responseObj.getString("PUBLISHER");
                                String CATEGORY = responseObj.getString("CATEGORY");
                                String HOSTNAME = responseObj.getString("HOSTNAME");
                                String TIMESTAMP = responseObj.getString("TIMESTAMP");
                                String timestamp=getDateCurrentTimeZone(Long.parseLong(TIMESTAMP));

                                NewsModel newsModel = new NewsModel();

                                newsModel.setID(ID);
                                newsModel.setTITLE(TITLE);
                                newsModel.setURL(URL);
                                newsModel.setPUBLISHER(PUBLISHER);
                                newsModel.setCATEGORY(CATEGORY);
                                newsModel.setHOSTNAME(HOSTNAME);
                                newsModel.setTIMESTAMP(timestamp);

                                newsModels.add(newsModel);
                            }
                            GetNews.onGetNewsDetailMore(rtrnValue,newsModels);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        rtrnValue="ERROR";
                        GetNews.onGetNewsDetailMore(rtrnValue,null);
                    }
                }
                else{
                    // response is null
                    rtrnValue="ERROR";
                    GetNews.onGetNewsDetailMore(rtrnValue,null);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley", "Error. HTTP Status Code:" + networkResponse.statusCode);
                }
                if (error instanceof TimeoutError) {
                    rtrnValue="HANDLED";
                    Toast.makeText(context, "Please try after some time. Slow net may be the problem", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    rtrnValue="HANDLED";
                    Toast.makeText(context, "Check your network connection", Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Log.e("Volley", "AuthFailureError");
                    rtrnValue="ERROR";
                } else if (error instanceof ServerError) {
                    Log.e("Volley", "ServerError");
                    rtrnValue="ERROR";
                } else if (error instanceof NetworkError) {
                    Log.e("Volley", "NetworkError");
                    rtrnValue="ERROR";
                } else if (error instanceof ParseError) {
                    Log.e("Volley", "Parse Error: " + error.getMessage());
                    rtrnValue="ERROR";
                }
                GetNews.onGetNewsDetailMore(rtrnValue,null);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header=new HashMap<String, String>();
                header.put("accept","application/json");
                return header;
            }
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(newsRequest);
    }
    private String getDateCurrentTimeZone(long timestamp) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa");
            return sdf.format(timestamp);
        }catch (Exception e) {
        }
        return "";
    }
}
