package com.bajaj.agam.simply;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String user_email_id=null;
    private boolean safeRotate;
    public void selectFrag(View view) {

        Button scanning = (Button)findViewById(R.id.bt_complain);
        Button historying=(Button)findViewById(R.id.bt_history);
        Fragment fr;

        if(view == findViewById(R.id.bt_complain)) {
            fr = StaticClass.complainFragment;
            historying.setAlpha((float) 0.4);
            scanning.setAlpha((float)1);

        }else {
            fr = StaticClass.repairsFragment;
            scanning.setAlpha((float)0.4);
            historying.setAlpha((float)1);
        }

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragFrame, fr);
        fragmentTransaction.commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Fragment viewer = (Fragment) getSupportFragmentManager().findFragmentById(R.id.fragFrame);
        outState.putString("email_id",user_email_id);
        if(viewer==StaticClass.complainFragment)
            outState.putInt("frag",1);
        else if(viewer==StaticClass.repairsFragment)
            outState.putInt("frag",2);
        else if(viewer==StaticClass.statusFragment)
            outState.putInt("frag",3);
        else if(viewer==StaticClass.carFragment)
            outState.putInt("frag",4);
        else if(viewer==StaticClass.contactFMSFrag)
            outState.putInt("frag",5);
        else if(viewer==StaticClass.directmsgFrag)
            outState.putInt("frag",6);
        else if(viewer==StaticClass.myDetailsFrag) {
            outState.putInt("frag",7);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras=getIntent().getExtras();
        if(extras!=null)
            user_email_id=extras.getString("email_id");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Fragment display = StaticClass.complainFragment;
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if(savedInstanceState!=null){
            user_email_id=savedInstanceState.getString("email_id");
            int controller = savedInstanceState.getInt("frag");
            switch (controller){
                case 1:{
                    display=StaticClass.complainFragment;
                    break;
                }
                case 2:{
                    display=StaticClass.repairsFragment;
                    break;
                }
                case 3:{
                    display=StaticClass.statusFragment;
                    break;
                }
                case 4:{
                    display=StaticClass.carFragment;
                    break;
                }
                case 5:{
                    display=StaticClass.contactFMSFrag;
                    break;
                }
                case 6:{
                    display=StaticClass.directmsgFrag;
                    break;
                }
                case 7:{
                    display=StaticClass.myDetailsFrag;
                    Bundle b=new Bundle();
                    b.putString("email_id",user_email_id);
                    display.setArguments(b);
                    break;
                }
                default:{
                    display=StaticClass.complainFragment;
                }
            }
            fragmentTransaction.replace(R.id.fragFrame,display);
            LinearLayout bar = (LinearLayout)findViewById(R.id.bottomBar);
            assert bar!=null;
            if(controller!=1)
                bar.setVisibility(View.GONE);
        }
        else
            fragmentTransaction.add(R.id.fragFrame,display);
        fragmentTransaction.commit();

        StaticClass.findUser(user_email_id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment viewer = null;
            try {
                viewer = (Fragment) getSupportFragmentManager().findFragmentById(R.id.fragFrame);
                if(viewer==StaticClass.complainFragment) {
                    super.onBackPressed();
                }
                else
                {
                    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragFrame, StaticClass.complainFragment);
                    fragmentTransaction.commit();
                    LinearLayout bar = (LinearLayout)findViewById(R.id.bottomBar);
                    assert bar!=null;
                    bar.setVisibility(View.VISIBLE);
                }
            } catch (ClassCastException e) {
                // not that fragment
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_exit) {
            finish();
        }
        else if (id == R.id.action_logout) {
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            finish();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Button scanning = (Button)findViewById(R.id.bt_complain);
        Button history_Butt=(Button)findViewById(R.id.bt_history);
        int id = item.getItemId();
        Fragment fr= StaticClass.complainFragment;
        LinearLayout bar = (LinearLayout)findViewById(R.id.bottomBar);
        if(id == R.id.nav_home){
            assert bar!=null;
            bar.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_profile) {
            fr= StaticClass.myDetailsFrag;
            Bundle extras = new Bundle();
            extras.putString("email_id",user_email_id);
            fr.setArguments(extras);
            assert bar != null;
            bar.setVisibility(View.GONE);
        } else if (id == R.id.nav_direct) {
            fr= StaticClass.directmsgFrag;
            assert bar != null;
            bar.setVisibility(View.GONE);
        } else if (id == R.id.nav_mycomp) {
            fr= StaticClass.repairsFragment;
            assert bar != null;
            bar.setVisibility(View.GONE);
        } else if (id==R.id.contactUs){
            fr = StaticClass.contactFMSFrag;
            assert bar != null;
            bar.setVisibility(View.GONE);
        }
        assert scanning!=null;
        scanning.setAlpha((float)1);
        assert history_Butt!=null;
        history_Butt.setAlpha((float)0.4);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragFrame, fr);
        fragmentTransaction.commitAllowingStateLoss();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    public void scanQR(View v) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException e) {
            showDialog(MainActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                Fragment f;
                if(contents.equals("CAR"))
                    f=StaticClass.carFragment;
                else if(contents.equals("CLEAN") || contents.equals("AC") || contents.equals("ELECTRIC"))
                    f=StaticClass.statusFragment;
                else
                {
                    Toast.makeText(MainActivity.this,"Sorry! We couldn't find that :(", Toast.LENGTH_LONG).show();
                    f=StaticClass.directmsgFrag;
                }

                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                Bundle args = new Bundle();
                args.putString("content",contents);
                f.setArguments(args);
                android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragFrame, f);
                fragmentTransaction.commitAllowingStateLoss();
            }
        }
    }

}
