package com.parse.starter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class UserSettings extends Fragment {

    private TextView nameView;
    private TextView emailView;
    private TextView usernameView;
    private ImageView profilePictureView;


    public void getUserInfo(){
        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        nameView.setText(user.getString("name"));
                        emailView.setText(user.getEmail());
                        usernameView.setText(user.getUsername());

                        ParseFile file = user.getParseFile("profilePicture");

                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null && data != null) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                    profilePictureView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 500, 500, false));
                                }
                            }
                        });

                    }
                });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_settings, container, false);

        nameView = view.findViewById(R.id.nameEditText);
        emailView = view.findViewById(R.id.emailEditTekst);
        usernameView = view.findViewById(R.id.usernameEditText);
        profilePictureView = view.findViewById(R.id.userPicture);




        getUserInfo();

        return view;
    }
}
