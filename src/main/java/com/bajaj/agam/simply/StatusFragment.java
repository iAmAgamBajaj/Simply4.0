package com.bajaj.agam.simply;

import android.app.ProgressDialog;
import android.content.Intent;
import android.opengl.EGLDisplay;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class StatusFragment extends Fragment {

    String title;
    String body;
    int status=1,cid;
    Button complain;
    EditText text;
    FloatingActionButton refreshB;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("space",true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v= inflater.inflate(R.layout.fragment_status, container, false);
        text = (EditText)v.findViewById(R.id.testField);
        refreshB=(FloatingActionButton)v.findViewById(R.id.refresh);
        complain=(Button)v.findViewById(R.id.complainButton);
        if(savedInstanceState!=null && savedInstanceState.getBoolean("space")){
            Toast.makeText(getContext(),"hadd ho gyi",Toast.LENGTH_LONG).show();
            refreshB.setVisibility(View.VISIBLE);
            complain.setVisibility(View.GONE);
        }
        else{
            complain.setVisibility(View.VISIBLE);
            refreshB.setVisibility(View.VISIBLE);
        }

        ImageView equipment = (ImageView)v.findViewById(R.id.equipment);
        String contents = getArguments().getString("content");
        title=contents;
        body= "Dear Sir,\nPlease register complaint for "+title+" in room number "+StaticClass.user_room_no
                +" of the "+StaticClass.user_hostel+" from userID "+StaticClass.user_email_id;

        assert equipment!=null;
        assert contents!=null;
        if(title.equals("AC")) {
            equipment.setBackgroundResource(R.drawable.air_conditioner);
        }
        else if(title.equals("ELECTRIC")) {
            equipment.setBackgroundResource(R.drawable.lightout);
        }
        else if(title.equals("CLEAN")) {
            equipment.setBackgroundResource(R.drawable.cleaning);
        }

        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                class AddComplaint extends AsyncTask<Void,Void,String> {
                    ProgressDialog loading;
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        loading = ProgressDialog.show(getActivity(),"Adding...","Wait...",false,false);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        loading.dismiss();
                        EditText e= (EditText)v.findViewById(R.id.testField);
                        String res[]=s.split(" ",2);
                        cid=Integer.valueOf(res[0]);
                        String ret="cID#"+res[0]+" "+res[1];
                        e.setText(ret);
                        ProgressBar pb = (ProgressBar)v.findViewById(R.id.progressBar);
                        pb.setProgress(1);
                        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected String doInBackground(Void... v) {
                        HashMap<String,String> params = new HashMap<>();
                        params.put("u_email",StaticClass.user_email_id);
                        params.put("title",title);
                        params.put("body",body);
                        params.put("status","1");
                        DbHandler handler = new DbHandler();

                        String res = handler.sendPostRequest(Config.URL_REGISTER_COMPLAINT, params);
                        return res;
                    }
                }

                AddComplaint ac = new AddComplaint();
                ac.execute();
                complain.setVisibility(View.GONE);

            }
        });

        refreshB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                class AddComplaint extends AsyncTask<Void,Void,String> {
                    ProgressDialog loading;
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        loading = ProgressDialog.show(getActivity(),"Adding...","Wait...",false,false);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        loading.dismiss();
                        JSONObject jsonObject = null;
                        ProgressBar pb = (ProgressBar)v.findViewById(R.id.progressBar);
                        try {
                            jsonObject = new JSONObject(s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1));
                            JSONArray result = jsonObject.getJSONArray("result");
                            String c = result.getJSONObject(0).getString("u_email");
                            if(!c.toString().equals("null")){
                                JSONObject obj = result.getJSONObject(0);
                                pb.setProgress(Integer.valueOf(obj.getString("status")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected String doInBackground(Void... v) {
                        DbHandler handler = new DbHandler();
                        String res = handler.sendGetRequestParam(Config.URL_GET_COMPLAINT,String.valueOf(cid));
                        return res;
                    }
                }

                AddComplaint ac = new AddComplaint();
                ac.execute();
            }
        });

        return v;
    }

    

}
