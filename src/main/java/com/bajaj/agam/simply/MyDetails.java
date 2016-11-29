package com.bajaj.agam.simply;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyDetails extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_details, container, false);
        Bundle extras = this.getArguments();
        if(extras!=null)
        {
            String email=extras.getString("email_id");
            StaticClass.findUser(email);
            showUser(view);
        }
        return view;
    }

    private void showUser(View view) {
            EditText Fname = (EditText) view.findViewById(R.id.fname);
            EditText Lname = (EditText) view.findViewById(R.id.lname);
            EditText Email = (EditText) view.findViewById(R.id.email);
            EditText Mob = (EditText) view.findViewById(R.id.mob);
            EditText Room = (EditText) view.findViewById(R.id.roomNo);

            RadioButton boys = (RadioButton) view.findViewById(R.id.bh);
            RadioButton girls = (RadioButton) view.findViewById(R.id.gh);

            Fname.setText(StaticClass.user_first_name);
            Lname.setText(StaticClass.user_last_name);
            Email.setText(StaticClass.user_email_id);
            Mob.setText(StaticClass.user_phone_no);
            Room.setText(StaticClass.user_room_no);

            if (StaticClass.user_hostel.equals("boys")) {
                boys.setChecked(true);
                girls.setChecked(false);
            } else {
                boys.setChecked(false);
                girls.setChecked(true);
            }

            Fname.setEnabled(false);
            Lname.setEnabled(false);
            Email.setEnabled(false);
            Mob.setEnabled(false);
            Room.setEnabled(false);
            boys.setEnabled(false);
            girls.setEnabled(false);

    }
}
