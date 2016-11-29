package com.bajaj.agam.simply;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class ContactFMS extends Fragment {


    FloatingActionButton callB,emailB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_contact_fm, container, false);
        callB = (FloatingActionButton)view.findViewById(R.id.makeCallFMS);
        emailB = (FloatingActionButton)view.findViewById(R.id.makeEmailFMS);

        callB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:+919868878433" ;
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse(uri));
                startActivity(i);
            }
        });

        emailB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support-fms@iiitd.ac.in"});
                startActivity(Intent.createChooser(intent,"Send Email"));
            }
        });

        return view;
    }
}
