package com.bajaj.agam.simply;



import android.annotation.SuppressLint;
        import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ClipData;
        import android.content.Context;
        import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
        import android.support.v4.app.Fragment;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.helper.ItemTouchHelper;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.ObjectInputStream;
        import java.io.ObjectOutputStream;
        import java.io.StreamCorruptedException;
        import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyRepairs extends Fragment implements RecyclerAdapter.CallBack {

    public static RecyclerView rec_view;
    public static RecyclerAdapter adapter;
    public ArrayList list;

    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_QUOTE = "EXTRA_QUOTE";
    private static final String EXTRA_ATTR = "EXTRA_ATTR";
    private static final String EXTRA_ID = "EXTRA_ID";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_my_repairs, container, false);
        rec_view = (RecyclerView)v.findViewById(R.id.recyclerViewHistory);
        rec_view.setLayoutManager(new LinearLayoutManager(getContext()));
        MyRepairs.rec_view.setAdapter(MyRepairs.adapter);
        viewAllComplaints(v);
        return v;
    }


//        Button add = (Button)findViewById(R.id.btn_add_item);
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addItem();
//            }
//        });



    // @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        switch (item.getItemId()) {
//
//            case R.id.action_add_contact:
//                startActivity(new Intent(this, Items.class));
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }



//    public void read(){
//        ObjectInputStream input;
//        String filename = "YO";
//
//        try {
//            input = new ObjectInputStream(new FileInputStream(new File(new File(getFilesDir(),"")+File.separator+filename)));
//            Items.data  = (ArrayList<ItemData>)input.readObject();
//            //Log.v("serialization","Person a="+myPersonObject.getA());
//            input.close();
//        } catch (StreamCorruptedException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }
//    String FILENAME ="YO";
//    public void WriteObjectToFile(Object serObj) {
//
//
//        try {
//            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
//            //FileOutputStream fileOut = new FileOutputStream(filepath);
//
//
//            ObjectOutputStream objectOut = new ObjectOutputStream(fos);
//            objectOut.writeObject(serObj);
//            objectOut.close();
//            fos.close();
//            System.out.println("The Object  was succesfully written to a file");
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }


    private void viewAllComplaints(View view){
        class ViewComplaints extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getContext(),"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showComplaints(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                DbHandler rh = new DbHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_ALL_USER_COMPLAINTS,StaticClass.user_email_id);
                return s;
            }
        }
        ViewComplaints vc = new ViewComplaints();
        vc.execute();
    }

    private void showComplaints(String json){
        ArrayList<ComplaintSchema> list = new ArrayList<>();
        JSONObject jsonObject;
        ArrayList<HashMap<String,String>> list2 = new ArrayList<HashMap<String, String>>();
        try{
        jsonObject = new JSONObject(json);
        JSONArray result = jsonObject.getJSONArray("result");
//            Toast.makeText(getContext(),"dekho"+result.length(),Toast.LENGTH_LONG).show();
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String c_id = jo.getString("u_id");
                String title = jo.getString("title");
                String body = jo.getString("body");
                String status= jo.getString("status");

                HashMap<String,String> complaints = new HashMap<>();
                complaints.put("u_id",c_id);
                complaints.put("title",title);
                complaints.put("body",body);
                complaints.put("status",status);
                list2.add(complaints);
//                ComplaintSchema complaintsFromUser= new ComplaintSchema(title,body,"",status,c_id,StaticClass.user_email_id);
//                list.add(complaintsFromUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Toast.makeText(getContext(),list.get(1).getTitle(),Toast.LENGTH_LONG).show();
        list.add(new ComplaintSchema("a","b","c","3","e","f"));
        adapter=new RecyclerAdapter(list2,getContext());
    }




    @Override
    public void onClick(int i) {

        ComplaintSchema itemData = (ComplaintSchema) list.get(i);

        Intent intent = new Intent(getContext(), OnClickComplaintActivity.class);
        startActivity(intent);


//        Bundle extras = new Bundle();
//        extras.putString(EXTRA_QUOTE, itemData.getTitle());
//        extras.putString(EXTRA_ATTR, itemData.getSec_title());
//        extras.putSerializable(EXTRA_ID,itemData.getId());
//        intent.putExtra(BUNDLE_EXTRAS, extras);




    }

    @Override
    public void onSeciconClick(int i) {

        ComplaintSchema itemData = (ComplaintSchema) list.get(i);


        //adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

//    public void addItem()
//    {
//        Intent intent = new Intent(this, Items.class);
//        startActivity(intent);
//
//
//    }

    public void delete( final int index)
    {
        //
        // ReadJson.datalist.remove(index);
        //WriteObjectToFile(Items.data);
        adapter.notifyDataSetChanged();
    }


}