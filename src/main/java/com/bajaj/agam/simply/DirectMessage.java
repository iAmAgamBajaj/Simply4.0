package com.bajaj.agam.simply;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class DirectMessage extends Fragment {

    Button dm;
    EditText body,title;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_direct_message, container, false);
        dm=(Button)view.findViewById(R.id.button_dm);
        body=(EditText)view.findViewById(R.id.dm_body);
        title=(EditText)view.findViewById(R.id.dm_title);

        dm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String myTitle = title.getText().toString();
                final String myBody = body.getText().toString();
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
                        String res[]=s.split(" ",2);
                        String ret="cID#"+res[0]+" "+res[1];
//                        Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected String doInBackground(Void... v) {
                        HashMap<String,String> params = new HashMap<>();
                        params.put("u_email",StaticClass.user_email_id);
                        params.put("title",myTitle);
                        params.put("body",myBody);
                        params.put("status","1");
                        DbHandler handler = new DbHandler();
                        String res = handler.sendPostRequest(Config.URL_REGISTER_COMPLAINT, params);
                        return res;
                    }
                }

                AddComplaint ac = new AddComplaint();
                ac.execute();
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragFrame, StaticClass.complainFragment);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

}
