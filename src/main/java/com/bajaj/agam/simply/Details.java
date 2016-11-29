package com.bajaj.agam.simply;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;

import static com.bajaj.agam.simply.R.id.toolbar;

public class Details extends AppCompatActivity {

    public Toolbar toolbar;
    private EditText emailF,fnameF,lnameF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent myIntent = getIntent();
        Bundle extras=myIntent.getExtras();
        String u_email=extras.getString("email_id");
        String fname=extras.getString("fname");
        String lname=extras.getString("lname");
        setContentView(R.layout.activity_details);

        emailF=(EditText)findViewById(R.id.email_form);
        fnameF=(EditText)findViewById(R.id.fname_form);
        lnameF=(EditText)findViewById(R.id.lname_form);
        assert emailF!=null;
        emailF.setText(u_email);
        emailF.setEnabled(false);
        assert fnameF!=null;
        fnameF.setText(fname);
        fnameF.setEnabled(false);
        assert lnameF!=null;
        lnameF.setText(lname);
        lnameF.setEnabled(false);
    }

    public String AddDetails(View view) {
        final EditText fname = (EditText) findViewById(R.id.fname_form);
        EditText lname = (EditText) findViewById(R.id.lname_form);
        EditText email = (EditText) findViewById(R.id.email_form);
        EditText phone = (EditText) findViewById(R.id.phone_form);
        EditText room = (EditText) findViewById(R.id.room_form);
        RadioButton bh = (RadioButton) findViewById(R.id.bh_form);
        final String u_hostel;

        if (bh.isSelected()) {
            u_hostel = "boys";
        } else {
            u_hostel = "girls";
        }

        final String u_fname=fname.getText().toString();
        final String u_lname=lname.getText().toString();
        final String u_email=email.getText().toString();
        final String u_phone=phone.getText().toString();
        final String u_room=room.getText().toString();

        class AddEmployee extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Details.this, "Adding...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Details.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put("fname",u_fname);
                params.put("lname",u_lname);
                params.put("email",u_email);
                params.put("phone",u_phone);
                params.put("room",u_room);
                params.put("hostel",u_hostel);
                DbHandler rh = new DbHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_USER, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();
        return u_email;
    }

    public void EnterApp(View view)
    {
        String user_email_id=AddDetails(view);
        Intent jumptomain = new Intent(view.getContext(), MainActivity.class);
        jumptomain.putExtra("email_id",user_email_id);
        finish();
        startActivity(jumptomain);
    }

}
