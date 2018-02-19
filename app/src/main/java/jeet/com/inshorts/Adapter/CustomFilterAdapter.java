package jeet.com.inshorts.Adapter;

/**
 * Created by jeet on 11/9/17.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import jeet.com.inshorts.Models.DataModel;
import jeet.com.inshorts.R;

import java.util.List;

public class CustomFilterAdapter extends ArrayAdapter {

    private List<DataModel> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView txtName;
        CheckBox checkBox;
    }

    public CustomFilterAdapter(List<DataModel> data, Context context) {
        super(context, R.layout.filter_child, data);
        this.dataSet = data;
        this.mContext = context;

    }
    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public DataModel getItem(int position) {
        return dataSet.get(position);
    }


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_child, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.filter_child_text);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_filter);

            result=convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        DataModel item = getItem(position);


        viewHolder.txtName.setText(item.name);
        viewHolder.checkBox.setChecked(item.checked);


        return result;
    }
}
