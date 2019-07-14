package com.parse.starter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ReportDetailsActivity extends AppCompatActivity {

   private TextView date;
   private TextView location;
   private ImageView reportPicture;
   private TextView goal;
   private TextView activities;
   private Double latitude;
   private Double longitude;
   private String id;

   public void onClickDelete(View view){
       new AlertDialog.Builder(this)
               .setIcon(android.R.drawable.ic_dialog_alert)
               .setTitle("Are you sure!?")
               .setMessage("Do you definitely want to delete the report?")
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       ParseQuery<ParseObject> query = ParseQuery.getQuery("Report");

                       try {
                           ParseObject report = query.get(id);

                           report.deleteInBackground(new DeleteCallback() {
                               @Override
                               public void done(ParseException e) {
                                   Toast.makeText(getApplicationContext(), "The report is deleted", Toast.LENGTH_SHORT).show();
                                   finish();
                               }
                           });
                       } catch (ParseException e) {
                           e.printStackTrace();
                       }


                   }
               })
               .setNegativeButton("No", null)
               .show();

   }



    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

   public void onClickUpdate(View view){
       Intent intent = new Intent(getApplicationContext(), AddingReport.class);
       intent.putExtra("id", id);
       startActivityForResult(intent,0);
   }



   public void onClickShowLocation(View view){
       Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
       intent.putExtra("latitude", latitude);
       intent.putExtra("longitude",longitude);
       startActivity(intent);

   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        date = findViewById(R.id.dateTextView);
        location = findViewById(R.id.locationTextView);
        reportPicture = findViewById(R.id.reportImageView);
        goal = findViewById(R.id.goalTextView);
        activities = findViewById(R.id.activitiesTextView);

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();


        if(extras != null) {

            id = extras.getString("id");

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Report");

            try {

                ParseObject report = query.get(id);

                ParseFile photo = report.getParseFile("reportPhoto");

                byte[] data = photo.getData();

                reportPicture.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(data, 0, data.length), 1200, 675, false));
                date.setText(String.format("Date: %s",report.getString("date")));
                location.setText(String.format("Location: %s",report.getString("location")));
                goal.setText(String.format("Goal: %s",report.getString("goal")));
                activities.setText(String.format("Activities: %s",report.getString("activities")));
                latitude = report.getDouble("latitude");
                longitude = report.getDouble("longitude");


            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


    }
}
