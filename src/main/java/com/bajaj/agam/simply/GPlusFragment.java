package com.bajaj.agam.simply;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;


public class GPlusFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private String fname,lname;
    private static final String TAG = "GPlusFragent";
    private int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;
    private Button signOutButton;
    private Button next;
    private Button disconnectButton;
    private LinearLayout signOutView;
    private TextView mStatusTextView;
    private TextView emailt;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private ProgressDialog mProgressDialog;
    private ImageView imgProfilePic;

    public static void verifyStoragePermissions(GPlusFragment activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity.getActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        verifyStoragePermissions(this);
        // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


    }


    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gplus, parent, false);

        signInButton = (SignInButton) v.findViewById(R.id.sign_in_button);
        signOutButton = (Button) v.findViewById(R.id.sign_out_button);
        next= (Button) v.findViewById(R.id.next);
        imgProfilePic = (ImageView) v.findViewById(R.id.img_profile_pic);
        emailt=(TextView)v.findViewById(R.id.emailtext);
        mStatusTextView = (TextView) v.findViewById(R.id.status);

        //first_time.setVisibility(View.GONE);

        Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.user_default);
        imgProfilePic.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(),icon, 200, 200, 200, false, false, false, false));
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);






            }

        });



    signOutButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            updateUI(false);
                        }
                    });
        }

    });

    next.setOnClickListener(new View.OnClickListener()
        {
           @Override
            public void onClick(View v)
           {

               String emailID = emailt.getText().toString();
               findUser(emailID.substring(10),v);

           }

        });

       return v;
    }

    private void findUser(final String email, final View view){

        class FindUser extends AsyncTask<Void,Void,String>{

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                Toast.makeText(getContext(),email,Toast.LENGTH_LONG).show();
                showUser(s,view, email);
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

    private void showUser(String json, View view, String email){
        try {
            JSONObject jsonObject = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
            JSONArray result = jsonObject.getJSONArray("result");
            String c = result.getJSONObject(0).getString("email");
//            Toast.makeText(getActivity(),c.toString(),Toast.LENGTH_SHORT).show();
            if(!c.toString().equals("null"))
            {
//                StaticClass.user_email_id=email;
//                old user
//                MyDetails mdt = StaticClass.mdt;
//                ((TextView)mdt.getView().findViewById(R.id.fname)).setText(c.getString("fname"));
//                ((TextView)mdt.getView().findViewById(R.id.lname)).setText(c.getString("lname"));
//                ((TextView)mdt.getView().findViewById(R.id.email)).setText(c.getString("email"));
//                ((TextView)mdt.getView().findViewById(R.id.mob)).setText(c.getString("phone"));
//                ((TextView)mdt.getView().findViewById(R.id.roomNo)).setText(c.getString("room"));
//                if(c.getString("hostel").equals("boys"))
//                {
//                    ((RadioButton)mdt.getView().findViewById(R.id.bh)).setChecked(true);
//                    ((RadioButton)mdt.getView().findViewById(R.id.gh)).setChecked(false);
//                }
//                else
//                {
//                    ((RadioButton)mdt.getView().findViewById(R.id.gh)).setChecked(true);
//                    ((RadioButton)mdt.getView().findViewById(R.id.bh)).setChecked(false);
//                }
                Intent jumptomain = new Intent(view.getContext(), MainActivity.class);
                jumptomain.putExtra("email_id",email);
                getActivity().finish();
                startActivity(jumptomain);
            }
            else
            {
                Intent jumptoprofile =new Intent(view.getContext(), Details.class);
                jumptoprofile.putExtra("fname",fname);
                jumptoprofile.putExtra("lname",lname);
                jumptoprofile.putExtra("email_id",email);
                getActivity().finish();
                startActivity(jumptoprofile);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
           GoogleSignInAccount acct = result.getSignInAccount();
            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()) );
            String res[] = acct.getDisplayName().split(" ",2);
            fname=res[0];
            lname=res[1];
            //Similarly you can get the email and photourl using acct.getEmail() and  acct.getPhotoUrl()

            emailt.setText("Email ID: "+    acct.getEmail().toString());

            String sFileName="usercheck";
            try {
                File root = new File(Environment.getExternalStorageDirectory(), "usercheck.txt");
                root.createNewFile();
                FileOutputStream fOut = new FileOutputStream(root);
                OutputStreamWriter myOutWriter =
                        new OutputStreamWriter(fOut);
                myOutWriter.append(acct.getEmail().toString());
                myOutWriter.append("\n");
                myOutWriter.close();
                fOut.close();
//                Toast.makeText(getContext(),
//                        "Done writing SD 'usercheck.txt'",
//                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
//                Toast.makeText(getContext(), e.getMessage(),
//                        Toast.LENGTH_SHORT).show();
            }



            if(acct.getPhotoUrl() != null)
                new LoadProfileImage(imgProfilePic).execute(acct.getPhotoUrl().toString());

            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }




    private void updateUI(boolean signedIn) {

        if (signedIn) {
            signInButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
            emailt.setVisibility(View.VISIBLE);

        } else {
            mStatusTextView.setText(R.string.signed_out);
            Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.user_default);
            imgProfilePic.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(),icon, 200, 200, 200, false, false, false, false));
            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
           // first_time.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
            emailt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }

    }


    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... uri) {
            String url = uri[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            if (result != null) {


                Bitmap resized = Bitmap.createScaledBitmap(result,200,200, true);
                bmImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(),resized,250,200,200, false, false, false, false));

            }
        }
    }


}
