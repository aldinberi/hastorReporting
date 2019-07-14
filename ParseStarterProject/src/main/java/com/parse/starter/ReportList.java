package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;


import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.parse.Parse.getApplicationContext;


public class ReportList extends Fragment {

    private ListView reportsListView;

    private List<Report> reports = new ArrayList<>();

    private CustomListViewAdapter adapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_list, container, false);

        reportsListView = view.findViewById(R.id.reportListView);

        ParseQuery<ParseObject> reportQuery = ParseQuery.getQuery("Report");
        reportQuery.whereEqualTo("createdBy", ParseUser.getCurrentUser());

        try {
            List<ParseObject> objects = reportQuery.find();

            for (ParseObject one : objects){
                ParseFile photo = one.getParseFile("reportPhoto");

                byte[] data = photo.getData();

                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                String date = one.getString("date");
                String id = one.getObjectId();
                String location = one.getString("location");
                String goal = one.getString("goal");
                String activities = one.getString("activities");
                Double latitude  = one.getDouble("latitude");
                Double longitude =  one.getDouble("longitude");

                reports.add(new Report(id ,bitmap, date, location, goal, activities, latitude, longitude));

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        adapter = new CustomListViewAdapter(getApplicationContext(), reports);

        reportsListView.setAdapter(adapter);

        reportsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Report report = (Report) parent.getItemAtPosition(position);

                Intent intent = new Intent (getApplicationContext(), ReportDetailsActivity.class);


                intent.putExtra("id", report.getId());

                startActivity(intent);
            }
        });

        final  EditText search = view.findViewById(R.id.searchEditText);

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = search.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

        return  view;
    }
}
