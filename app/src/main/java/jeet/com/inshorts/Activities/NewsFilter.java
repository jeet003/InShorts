package jeet.com.inshorts.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import jeet.com.inshorts.Models.DataModel;
import jeet.com.inshorts.Models.NewsModel;
import jeet.com.inshorts.R;
import jeet.com.inshorts.Adapter.CustomFilterAdapter;
import jeet.com.inshorts.Util.PrefManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeet on 10/9/17.
 */

public class NewsFilter extends AppCompatActivity {

    List<DataModel> dataModels1;
    List<DataModel> dataModels2;
    ListView listView1;
    ListView listView2;
    TextView filter_apply,filter_clear;
    private Toolbar toolbar;

    private CustomFilterAdapter adapter1;
    private CustomFilterAdapter adapter2;

    ArrayList<String> filter1;
    ArrayList<String> filter2;

    private ArrayList<NewsModel> newsModelArrayList;

    private PrefManager sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Intent i=getIntent();
        newsModelArrayList=i.getParcelableArrayListExtra("List");
        toolbar=(Toolbar) findViewById(R.id.toolbar_filter);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView toolbar_title=(TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getString(R.string.news_filter));




        listView1 = (ListView) findViewById(R.id.LV1);
        listView2 = (ListView) findViewById(R.id.LV2);
        filter_apply=(TextView) findViewById(R.id.apply_filter);
        filter_clear=(TextView) findViewById(R.id.clear_filter);

        sharedPreference=new PrefManager(this);

        String json=sharedPreference.getFilter1();
        String json1=sharedPreference.getFilter2();

        if(!json.equals("") && !json1.equals("")) {

            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<DataModel>>() {}.getType();
            ArrayList<DataModel> dataModels1Filter = gson.fromJson(json, type);
            ArrayList<DataModel> dataModels2Filter = gson.fromJson(json1, type);

            dataModels1=dataModels1Filter;
            dataModels2=dataModels2Filter;

        }
        else if(json.equals("") && !json1.equals(""))
        {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<DataModel>>() {}.getType();
            dataModels1 = new ArrayList<>();
            ArrayList<String> cat=new ArrayList<>();

            for(NewsModel newsModel:newsModelArrayList)
            {
                if(!cat.contains(newsModel.getCATEGORY()))
                    cat.add(newsModel.getCATEGORY());

            }
            for(String s:cat)
            {
                dataModels1.add(new DataModel(s,false));
            }

            ArrayList<DataModel> dataModels2Filter = gson.fromJson(json1, type);
            dataModels2=dataModels2Filter;

        }
        else if(!json.equals("") && json1.equals(""))
        {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<DataModel>>() {}.getType();
            ArrayList<DataModel> dataModels1Filter = gson.fromJson(json, type);
            dataModels2 = new ArrayList<>();
            ArrayList<String> pub=new ArrayList<>();
            for(NewsModel newsModel:newsModelArrayList)
            {
                if(pub.contains(newsModel.getPUBLISHER()))
                    pub.add(newsModel.getPUBLISHER());
            }
            for(String s:pub)
            {
                dataModels2.add(new DataModel(s,false));
            }

            dataModels1=dataModels1Filter;

        }
        else
        {
            dataModels1 = new ArrayList<>();
            dataModels2 = new ArrayList<>();
            ArrayList<String> cat=new ArrayList<>();

            for(NewsModel newsModel:newsModelArrayList)
            {
                if(!cat.contains(newsModel.getCATEGORY()))
                    cat.add(newsModel.getCATEGORY());

            }
            for(String s:cat)
            {
                dataModels1.add(new DataModel(s,false));
            }

            ArrayList<String> pub=new ArrayList<>();
            for(NewsModel newsModel:newsModelArrayList)
            {
                if(!pub.contains(newsModel.getPUBLISHER()))
                    pub.add(newsModel.getPUBLISHER());
            }
            for(String s:pub)
            {
                dataModels2.add(new DataModel(s,false));
            }

        }

        prepareListData();

        adapter1 = new CustomFilterAdapter(dataModels1, getApplicationContext());
        adapter2 = new CustomFilterAdapter(dataModels2, getApplicationContext());


        listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);

        setListViewHeightBasedOnChildren(listView1);
        setListViewHeightBasedOnChildren(listView2);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                DataModel dataModel= dataModels1.get(position);
                dataModel.checked = !dataModel.checked;
                adapter1.notifyDataSetChanged();
                if(dataModel.checked)
                {
                        filter1.add(dataModel.name);
                }
                else
                {
                    if(!filter1.isEmpty())
                        filter1.remove(dataModel.name);
                }


            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                DataModel dataModel= dataModels2.get(position);
                dataModel.checked = !dataModel.checked;
                adapter2.notifyDataSetChanged();
                if(dataModel.checked)
                {
                    filter2.add(dataModel.name);
                }
                else
                {
                    if(!filter2.isEmpty())
                        filter2.remove(dataModel.name);
                }



            }
        });

        filter_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.putStringArrayListExtra("filter1",(ArrayList<String>) filter1);
                intent.putStringArrayListExtra("filter2",(ArrayList<String>) filter2);
                intent.putExtra("status","yes");
                setResult(RESULT_OK,intent);

                Gson gson = new Gson();
                String json = gson.toJson(dataModels1);
                sharedPreference.setFilter1(json);
                String json1 = gson.toJson(dataModels2);
                sharedPreference.setFilter2(json1);


                finish();
            }
        });

        filter_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(DataModel dataModel:dataModels1)
                {
                    if(dataModel.checked)
                        dataModel.checked=false;
                }
                for(DataModel dataModel:dataModels2)
                {
                    if(dataModel.checked)
                        dataModel.checked=false;
                }
                if(!filter1.isEmpty())
                filter1.clear();
                if(!filter2.isEmpty())
                filter2.clear();
                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();


            }
        });

    }
    private void prepareListData()
    {
        filter1=new ArrayList<>();
        filter2=new ArrayList<>();

        for(DataModel dataModel:dataModels1)
        {
            if(dataModel.checked)
                filter1.add(dataModel.name);
        }
        for(DataModel dataModel:dataModels2)
        {
            if(dataModel.checked)
                filter2.add(dataModel.name);

        }

    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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
}
