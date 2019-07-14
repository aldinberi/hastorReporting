package com.parse.starter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.parse.Parse.getApplicationContext;

public class CustomListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Report> reportsList;
    private ArrayList<Report> arraylist;

    public CustomListViewAdapter(Context context, List<Report> reports){
        this.context = context;
        this.reportsList=reports;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(reportsList);
    }

    @Override
    public int getCount() {
        return reportsList.size();
    }

    @Override
    public Object getItem(int position) {
        return reportsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reportsList.indexOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.custom_list_view_item, parent, false);

        Report report = reportsList.get(position);
        ImageView image = convertView.findViewById(R.id.report_icon);
        TextView title = convertView.findViewById(R.id.report_title);
        TextView sub_title = convertView.findViewById(R.id.report_subTitle);

        image.setImageBitmap(report.getImageUrl());
        title.setText(report.getDate());
        sub_title.setText(report.getLocation());
        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        reportsList.clear();
        if (charText.length() == 0) {
           reportsList.addAll(arraylist);
        }
        else
        {
            for (Report wp : arraylist) {
                if (wp.getDate().toLowerCase(Locale.getDefault()).contains(charText)) {
                    reportsList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
