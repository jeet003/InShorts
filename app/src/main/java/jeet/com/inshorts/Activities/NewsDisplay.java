package jeet.com.inshorts.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import jeet.com.inshorts.Adapter.NewsAdapter;
import jeet.com.inshorts.BackgroundServices.GetNews;
import jeet.com.inshorts.BackgroundServices.GetNewsMore;
import jeet.com.inshorts.Interfaces.Volley;
import jeet.com.inshorts.Models.NewsModel;
import jeet.com.inshorts.R;
import jeet.com.inshorts.Util.EndlessRecyclerOnScrollListener;
import jeet.com.inshorts.Util.LocalDatabase;
import jeet.com.inshorts.Util.PrefManager;

/**
 * Created by jeet on 10/9/17.
 */

public class NewsDisplay extends AppCompatActivity implements Volley.GetNews,Volley.GetNewsMorw{
    private ArrayList<NewsModel> newsModelList;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayoutManager mLayoutManager;
    private NewsAdapter mAdapter;
    private Toolbar toolbar;
    private PrefManager sharedPreference;
    private ProgressDialog progressDialog;
    private String  Tag="News: ";
    private static final int SECOND_ACTIVITY_RESULT_CODE=0;
    private TextView filterButton,resetButton,offline,ascend,descend;
    ArrayList<String> filter1;
    ArrayList<String> filter2;
    private EndlessRecyclerOnScrollListener mScrollListener = null;
    public static TextView empty;
    private TextView Loading;
    LocalDatabase localDB;
    SQLiteDatabase sqliteDB;
    int value=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);


        init();
        setUpAcitonBar();

        sharedPreference.setFilter1("");
        sharedPreference.setFilter2("");

        progressDialog= ProgressDialog.show(this,"",getString(R.string.progress),true,false);
        GetNews getNews=new GetNews(this,this,value);
        getNews.loadNews();





        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refresh();
            }
        });

    }

    void refresh(){
        Loading.setVisibility(View.VISIBLE);
        GetNewsMore getNewsMore=new GetNewsMore(this,this,value);
        getNewsMore.loadNews();

    }


    public void onFilterClick(View view)
    {
        Intent intent=new Intent(NewsDisplay.this,NewsFilter.class);
        intent.putParcelableArrayListExtra("List",newsModelList);
        startActivityForResult(intent,SECOND_ACTIVITY_RESULT_CODE);

    }
    public void onResetClick(View view)
    {
        sharedPreference.setFilter1("");
        sharedPreference.setFilter2("");
        filter1=new ArrayList<>();
        filter2=new ArrayList<>();
        filterButton.setTextColor(Color.parseColor("#ffffff"));
        filterButton.setText(getString(R.string.filter));
        resetButton.setVisibility(View.INVISIBLE);
        mAdapter.filter(filter1,filter2);
        if(ascend.getCurrentTextColor()==Color.WHITE && descend.getCurrentTextColor()==Color.WHITE)
        {

        }else if(ascend.getCurrentTextColor()==Color.GREEN)
            mAdapter.ascend();
        else if(descend.getCurrentTextColor()==Color.GREEN)
            mAdapter.descend();
    }
    public void onAscendClick(View view)
    {
        ascend.setTextColor(Color.GREEN);
        descend.setTextColor(Color.WHITE);
        mAdapter.ascend();
    }
    public void onDescendClick(View view)
    {
        descend.setTextColor(Color.GREEN);
        ascend.setTextColor(Color.WHITE);
        mAdapter.descend();
    }

    public void init()
    {
        localDB = new LocalDatabase(this);
        sqliteDB = localDB.getDatabaseObject();
        sharedPreference=new PrefManager(this);
        toolbar=(Toolbar) findViewById(R.id.toolbar_news);
        recyclerView = (RecyclerView) findViewById(R.id.transaction_recycler_view);
        filterButton = (TextView) findViewById(R.id.filter_button);
        resetButton = (TextView) findViewById(R.id.reset_button);
        empty=(TextView) findViewById(R.id.empty_view);
        mSwipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        filter1=new ArrayList<>();
        filter2=new ArrayList<>();
        Loading=(TextView) findViewById(R.id.Loading);
        offline=(TextView) findViewById(R.id.offline);
        ascend=(TextView) findViewById(R.id.sort_ascending);
        ascend.setTextColor(Color.WHITE);
        descend=(TextView) findViewById(R.id.sort_descending);
        descend.setTextColor(Color.WHITE);

    }
    public void setUpAcitonBar()
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView toolbar_title=(TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getString(R.string.news));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        switch(id)
        {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==SECOND_ACTIVITY_RESULT_CODE){
            if(resultCode==RESULT_OK){
                String res=data.getStringExtra("status");
                if(res.equals("yes"))
                {
                    filter1=data.getStringArrayListExtra("filter1");
                    filter2=data.getStringArrayListExtra("filter2");

                    if(!filter1.isEmpty() || !filter2.isEmpty())
                    {
                        resetButton.setVisibility(View.VISIBLE);
                        filterButton.setTextColor(Color.GREEN);
                        filterButton.setText("Filter ("+String.valueOf(filter1.size()+filter2.size())+")");
                    }
                    else
                    {
                        resetButton.setVisibility(View.INVISIBLE);
                        filterButton.setTextColor(Color.parseColor("#ffffff"));
                        filterButton.setText(getString(R.string.filter));
                    }

                    mAdapter.filter(filter1,filter2);

                    if(ascend.getCurrentTextColor()==Color.WHITE && descend.getCurrentTextColor()==Color.WHITE)
                    {

                    }else if(ascend.getCurrentTextColor()==Color.GREEN)
                        mAdapter.ascend();
                    else if(descend.getCurrentTextColor()==Color.GREEN)
                        mAdapter.descend();


                }
                else
                {
                    //mAdapter.notifyDataSetChanged();
                }

            }
        }
    }

    @Override
    public void onGetNewsDetailMore(String returnValue, ArrayList<NewsModel> newsModelArrayList) {
        Loading.setVisibility(View.GONE);
        value=value+20;
        if (returnValue.equals("SUCCESS")) {
            // store data in local db or cache
            Log.d(Tag, "success");

            if(recyclerView.getChildCount()==0)
                refresh();

            if(newsModelArrayList!=null)
            {
                for(NewsModel newsModel:newsModelArrayList)
                {
                    localDB.insertDataIntoTable(
                            newsModel.getID(),
                            newsModel.getTITLE(),
                            newsModel.getURL(),
                            newsModel.getPUBLISHER(),
                            newsModel.getCATEGORY(),
                            newsModel.getHOSTNAME(),
                            newsModel.getTIMESTAMP()
                            ,sqliteDB
                    );
                }


                newsModelList.addAll(newsModelArrayList);
                mAdapter.addAll(newsModelArrayList);

                mAdapter.filter(filter1,filter2);

                if(ascend.getCurrentTextColor()==Color.WHITE && descend.getCurrentTextColor()==Color.WHITE)
                {

                }else if(ascend.getCurrentTextColor()==Color.GREEN)
                    mAdapter.ascend();
                else if(descend.getCurrentTextColor()==Color.GREEN)
                    mAdapter.descend();

                mSwipeRefreshLayout.setRefreshing(false);
                mScrollListener.setLoading(false);
            }
            else{
                mSwipeRefreshLayout.setRefreshing(false);

            }


        } else if (returnValue.contains("ERROR")) {
            // show result from local db
            mSwipeRefreshLayout.setRefreshing(false);

        } else {
            // show data from local db
            mSwipeRefreshLayout.setRefreshing(false);

        }
    }

    @Override
    public void onGetNewsDetail(String returnValue, ArrayList<NewsModel> newsModelArrayList) {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        value=value+20;
        if (returnValue.equals("SUCCESS")) {
            Log.d(Tag, "success");
            this.newsModelList = newsModelArrayList;
            localDB.deleteAllData(LocalDatabase.TableName, sqliteDB);
            //storing the data in db
            for(NewsModel newsModel:newsModelArrayList)
            {
                localDB.insertDataIntoTable(
                        newsModel.getID(),
                        newsModel.getTITLE(),
                        newsModel.getURL(),
                        newsModel.getPUBLISHER(),
                        newsModel.getCATEGORY(),
                        newsModel.getHOSTNAME(),
                        newsModel.getTIMESTAMP()
                        ,sqliteDB
                );
            }

            mAdapter = new NewsAdapter(newsModelList,this);
            mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            mScrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    refresh();

                }
            };
            recyclerView.addOnScrollListener(mScrollListener);
            mAdapter.notifyDataSetChanged();



        } else if (returnValue.contains("ERROR")) {
            // show result from local db
            offline.setVisibility(View.VISIBLE);
            this.newsModelList=localDB.getList(sqliteDB);
            mAdapter = new NewsAdapter(this.newsModelList,this);
            mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

        } else {
            // show data from local db
            offline.setVisibility(View.VISIBLE);
            this.newsModelList=localDB.getList(sqliteDB);
            mAdapter = new NewsAdapter(this.newsModelList,this);
            mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this,MainActivity.class);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

}
