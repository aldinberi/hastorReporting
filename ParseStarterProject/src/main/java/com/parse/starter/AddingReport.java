package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class AddingReport extends AppCompatActivity {
    EditText location;
    EditText dateView;
    EditText goalView;
    EditText activitiesView;
    Button selectImage;
    Button selectLocation;
    Button saveReport;
    ImageView imageView;
    Bitmap image;
    String resultLocation;
    double latitude;
    double longitude;

    public void getPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    public void getLocation(){
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivityForResult(intent, 2);
    }

    public void updateReport(String id){

        if(location.getText().toString().replaceAll("\\s+","").equals("") ||
                dateView.getText().toString().replaceAll("\\s+","").equals("") ||
                goalView.getText().toString().replaceAll("\\s+","").equals("") ||
                activitiesView.getText().toString().replaceAll("\\s+","").equals("")){

            Toast.makeText(this, "You can not leave any empty fields", Toast.LENGTH_SHORT).show();

        }else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Report");

            if (image == null) {
                image = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.hastorlogo);
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            image.compress(Bitmap.CompressFormat.PNG, 100, stream);

            byte[] byteArray = stream.toByteArray();

            final ParseFile file = new ParseFile("image.png", byteArray);

            query.getInBackground(id, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject report, ParseException e) {
                    if (e == null && report != null) {


                        report.put("location", location.getText().toString());
                        report.put("latitude", latitude);
                        report.put("longitude", longitude);
                        report.put("date", dateView.getText().toString());
                        report.put("goal", goalView.getText().toString());
                        report.put("activities", activitiesView.getText().toString());
                        report.put("reportPhoto", file);
                        report.put("createdBy", ParseUser.getCurrentUser());

                        report.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(AddingReport.this, "Successfully updated report", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(AddingReport.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(AddingReport.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }


            });
        }
    }






    public void saveReport(){

        if(location.getText().toString().replaceAll("\\s+","").equals("") ||
                dateView.getText().toString().replaceAll("\\s+","").equals("") ||
                goalView.getText().toString().replaceAll("\\s+","").equals("") ||
                activitiesView.getText().toString().replaceAll("\\s+","").equals("")){

            Toast.makeText(this, "You can not leave any empty fields", Toast.LENGTH_SHORT).show();

        }else {

            final ParseObject report = new ParseObject("Report");

            if (image == null) {
                image = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.hastorlogo);
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            image.compress(Bitmap.CompressFormat.PNG, 100, stream);

            byte[] byteArray = stream.toByteArray();

            final ParseFile file = new ParseFile("image.png", byteArray);

            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        report.put("location", location.getText().toString());
                        report.put("latitude", latitude);
                        report.put("longitude", longitude);
                        report.put("date", dateView.getText().toString());
                        report.put("goal", goalView.getText().toString());
                        report.put("activities", activitiesView.getText().toString());
                        report.put("reportPhoto", file);
                        report.put("createdBy", ParseUser.getCurrentUser());

                        report.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(AddingReport.this, "Successfully added report", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(AddingReport.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(AddingReport.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    public void cancelReport(View view){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_report);

        location = findViewById(R.id.locationEditText);
        imageView = findViewById(R.id.picture);
        selectLocation = findViewById(R.id.selectLocationButton);
        selectImage = findViewById(R.id.selectImageButton);
        dateView = findViewById(R.id.dateEditText);
        goalView = findViewById(R.id.goalEditText);
        activitiesView = findViewById(R.id.activitiesEditText);
        saveReport = findViewById(R.id.saveReportButton);

        final Bundle extra = getIntent().getExtras();

        if(extra != null){

            final String id = extra.getString("id");

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Report");

            try {

                ParseObject report = query.get(id);

                ParseFile photo = report.getParseFile("reportPhoto");

                byte[] data = photo.getData();

                image = Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(data, 0, data.length), 1200, 675, false);
                imageView.setImageBitmap(image);
                dateView.setText(report.getString("date"));
                location.setText(report.getString("location"));
                goalView.setText(report.getString("goal"));
                activitiesView.setText(report.getString("activities"));
                latitude = report.getDouble("latitude");
                longitude = report.getDouble("longitude");


            } catch (ParseException e) {
                e.printStackTrace();
            }
            saveReport.setText("UPDATE");
            saveReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateReport(id);
                }
            });

        }else{
            saveReport.setText("SAVE");
            saveReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveReport();
                }
            });
        }

        selectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto();
            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImage = data.getData();

        if(requestCode == 1 && resultCode == RESULT_OK && data != null ){
            try{
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imageView.setImageBitmap(Bitmap.createScaledBitmap(image, 1200, 675, false));

            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }
        }

        if(requestCode == 2 && resultCode == RESULT_OK && data != null ){
            resultLocation = data.getStringExtra("address");
            latitude = data.getDoubleExtra("latitude",0);
            longitude = data.getDoubleExtra("longitude",0);
            location.setText(resultLocation);
        }


    }
}
