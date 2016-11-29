package com.bajaj.agam.simply;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Agam on 11/28/2016.
 */
public class StaticClass {
    public static String user_email_id=null;
    public static String user_first_name=null;
    public static String user_last_name=null;
    public static String user_phone_no=null;
    public static String user_room_no=null;
    public static String user_hostel=null;

    public static DefaultFragment complainFragment = new DefaultFragment();
    public static MyRepairs repairsFragment = new MyRepairs();
    public static StatusFragment statusFragment = new StatusFragment();
    public static CarFragment carFragment = new CarFragment();
    public static ContactFMS contactFMSFrag = new ContactFMS();
    public static DirectMessage directmsgFrag = new DirectMessage();
    public static MyDetails myDetailsFrag = new MyDetails();

    public static void findUser(final String email){
        class FindUser extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                showUser(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                DbHandler rh = new DbHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_USER,email);
                return s;
            }
        }
        FindUser fu = new FindUser();
        fu.execute();
    }

    private static void showUser(String json){
        try {
            JSONObject jsonObject = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
            JSONArray result = jsonObject.getJSONArray("result");
            String c = result.getJSONObject(0).getString("email");
            if(!c.toString().equals("null"))
            {
                JSONObject obj = result.getJSONObject(0);

                user_first_name=obj.getString("fname");
                user_last_name=obj.getString("lname");
                user_email_id=obj.getString("email");
                user_phone_no=obj.getString("phone");
                user_room_no=obj.getString("room");
                user_hostel=obj.getString("hostel");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
