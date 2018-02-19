package jeet.com.inshorts.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import jeet.com.inshorts.Activities.NewsDisplay;
import jeet.com.inshorts.Activities.WebViewActivity;
import jeet.com.inshorts.Models.NewsModel;
import jeet.com.inshorts.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by jeet on 10/9/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private List<NewsModel> newsDTOs,filterDTOs;
    private Context context;
    Boolean[] chkArr;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,CheckboxListener{
        public TextView title, url, publisher,category,hostname,timestamp;
        public CustomSwitchListener myCustomSwitchListener;
        private CheckBox fav;
        public MyViewHolder(View view,CustomSwitchListener checkboxListener) {
            super(view);
            title=(TextView) view.findViewById(R.id.news_title);
            url=(TextView) view.findViewById(R.id.url);
            publisher=(TextView) view.findViewById(R.id.publisher);
            category=(TextView) view.findViewById(R.id.category);
            hostname=(TextView) view.findViewById(R.id.hostname);
            timestamp=(TextView) view.findViewById(R.id.timestamp);
            fav=(CheckBox) view.findViewById(R.id.chk);
            url.setOnClickListener(this);
            title.setOnClickListener(this);
            myCustomSwitchListener=checkboxListener;
            fav.setOnCheckedChangeListener(myCustomSwitchListener);

        }

        @Override
        public void onClick(View v) {
            Intent i=new Intent(context, WebViewActivity.class);
            String url=filterDTOs.get(getPosition()).getURL();
            i.putExtra("url",url);
            context.startActivity(i);

        }
        @Override
        public  void updateCheck(int pos,boolean val)
        {
            if(val==true)
            {
                fav.setChecked(true);

            }
            else{
                fav.setChecked(false);
            }
        }
    }

    public NewsAdapter(List<NewsModel> newsDTOs, Context context)
    {
        this.newsDTOs=newsDTOs;
        this.context=context;
        this.filterDTOs = new ArrayList<NewsModel>();
        this.filterDTOs.addAll(this.newsDTOs);
        chkArr=new Boolean[1000];
        for(int i=0;i<chkArr.length;i++)
            chkArr[i]=false;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_card, parent, false);
        context=itemView.getContext();

        return new MyViewHolder(itemView,new CustomSwitchListener());
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        NewsModel newsModel=filterDTOs.get(position);
        holder.title.setText(newsModel.getTITLE());
        holder.category.setText("Category: "+newsModel.getCATEGORY());
        holder.hostname.setText(newsModel.getHOSTNAME());
        holder.publisher.setText(newsModel.getPUBLISHER());
        holder.url.setText(newsModel.getURL());
        holder.timestamp.setText(newsModel.getTIMESTAMP());

        holder.myCustomSwitchListener.updatePosition(position,holder);
        holder.fav.setChecked(chkArr[position]);
    }

    public void addAll(List<NewsModel> list) {
        filterDTOs.addAll(list);
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return (null != filterDTOs ? filterDTOs.size() : 0);
    }

    public void filter(final ArrayList<String> filter1, final ArrayList<String> filter2) {

        filterDTOs.clear();

                // If there is no search value, then add all original list items to filter list
                if (filter1.isEmpty() && filter2.isEmpty()) {

                    filterDTOs.addAll(newsDTOs);

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (NewsModel item : newsDTOs) {


                        if(filter1.isEmpty())
                        {
                            if(filter2.contains(item.getPUBLISHER()))
                                filterDTOs.add(item);
                        }
                        else if(filter2.isEmpty())
                        {
                            if(filter1.contains(item.getCATEGORY()))
                                filterDTOs.add(item);
                        }
                        else
                        {
                            if(filter1.contains(item.getCATEGORY()))
                            {
                                if(filter2.contains(item.getPUBLISHER()))
                                    filterDTOs.add(item);
                            }
                        }

                    }

                }


                        if(filterDTOs.isEmpty())
                        {
                            NewsDisplay.empty.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            NewsDisplay.empty.setVisibility(View.GONE);
                        }

                        notifyDataSetChanged();


    }
    public void ascend()
    {
        Collections.sort(filterDTOs, new Comparator<NewsModel>() {
            public int compare(NewsModel m1, NewsModel m2) {
                return m1.getTIMESTAMP().compareTo(m2.getTIMESTAMP());
            }
        });
        notifyDataSetChanged();
    }
    public void descend()
    {
        Collections.sort(filterDTOs, new Comparator<NewsModel>() {
            public int compare(NewsModel m1, NewsModel m2) {
                return m2.getTIMESTAMP().compareTo(m1.getTIMESTAMP());
            }
        });
        notifyDataSetChanged();
    }
    public interface CheckboxListener{
        void updateCheck(int pos,boolean val);
    }

    private class CustomSwitchListener implements CompoundButton.OnCheckedChangeListener {
        private int position;
        CheckboxListener checkboxListener;


        /**
         * Updates the position according to onBindViewHolder
         *
         * @param position - position of the focused item
         */
        public void updatePosition(int position,MyViewHolder holder) {
            this.position = position;
            checkboxListener=(CheckboxListener) holder;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            chkArr[position]=isChecked;
            if(isChecked)
            {
                checkboxListener.updateCheck(position,true);
            }
            else
            {
                checkboxListener.updateCheck(position,false);
            }
        }
    }

}
