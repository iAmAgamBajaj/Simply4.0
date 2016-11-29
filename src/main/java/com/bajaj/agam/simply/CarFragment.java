package com.bajaj.agam.simply;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class CarFragment extends Fragment {

    TextView Fname,Lname,Plate;
    String emailID, phoneNo;
    Intent callIntent;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE=1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car,container,false);
        View v = inflater.inflate(R.layout.car_find, null, false);

        final EditText plateID = (EditText)v.findViewById(R.id.plate_no_entered);
        Fname = (TextView)view.findViewById(R.id.showfname);
        Lname = (TextView)view.findViewById(R.id.showlname);
        Plate = (TextView)view.findViewById(R.id.showPlateNo);
        new AlertDialog.Builder(getContext())
                .setView(v)
                .setTitle("Enter Plate Number")
                .setPositiveButton("Enter",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String plateNo=plateID.getText().toString();
                                findOwner(plateNo.toUpperCase());
                            }

                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        }).show();
        final FloatingActionButton email = (FloatingActionButton)view.findViewById(R.id.makeEmail);
        FloatingActionButton call = (FloatingActionButton)view.findViewById(R.id.makeCall);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ emailID});
                startActivity(Intent.createChooser(intent,"Send Email"));
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + phoneNo.trim() ;
                callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse(uri));
                startActivity(callIntent);
            }
        });

        return view;
    }


    private void findOwner(final String plateNo)
    {
        class FindOwner extends AsyncTask<Void, Void, String>{

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray("result");
                    JSONObject c = result.getJSONObject(0);
                    Fname.setText(c.getString("fname"));
                    Lname.setText(c.getString("lname"));
                    Plate.setText(c.getString("plate_no"));
                    emailID=c.getString("email_id");
                    phoneNo=c.getString("phone_no");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                DbHandler handler = new DbHandler();
                String s= handler.sendGetRequestParam(Config.URL_GET_CAR,plateNo);
                return s;
            }
        }
        FindOwner fo = new FindOwner();
        fo.execute();
    }

}
