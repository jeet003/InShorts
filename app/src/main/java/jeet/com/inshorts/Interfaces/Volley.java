package jeet.com.inshorts.Interfaces;

import java.util.ArrayList;

import jeet.com.inshorts.Models.NewsModel;

/**
 * Created by jeet on 10/9/17.
 */

public interface Volley {
    interface GetNews{
        void onGetNewsDetail(String returnValue, ArrayList<NewsModel> newsModelArrayList);
    }
    interface GetNewsMorw{
        void onGetNewsDetailMore(String returnValue, ArrayList<NewsModel> newsModelArrayList);
    }
}
