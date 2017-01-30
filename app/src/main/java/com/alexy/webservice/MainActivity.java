package com.alexy.webservice;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    CallbackManager callbackManager;
    LoginButton loginButton;
    String email,name,gender,birthday,currentCity;
    ProfilePictureView profilePictureView;
    Button connect;
    GoogleApiClient mGoogleApiClient;
    private static final int CONNECT = 1;
    private static final String TAG = "drive-quickstart";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);



        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.alexy.webservice",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {


        }



        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        connect = (Button) findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoggedIn()) {
                    Intent intent = new Intent(MainActivity.this, RoutesActivity.class);
                    startActivity(intent);
                }
                else {
                    mGoogleApiClient.connect();
                }
            }
        });
        profilePictureView = (ProfilePictureView) findViewById(R.id.image);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends","user_relationships","user_location"));
        callbackManager = CallbackManager.Factory.create();
        updateView();
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            updateView();
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (isLoggedIn()){
            startActivity(new Intent(MainActivity.this,RoutesActivity.class));
        }
        switch (requestCode) {

            case CONNECT:
                if (resultCode == Activity.RESULT_OK) {
                    mGoogleApiClient.connect();
                } else {
                }
                break;
        }


    }



    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public void updateView(){
        if (isLoggedIn()){

            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("LoginActivity", response.toString());

                            // Application code
                            try {
                                email = object.getString("email");
                                birthday = object.getString("birthday"); // 01/31/1980 format
                                gender = object.getString("gender");
                                name = object.getString("name");
                                currentCity = object.getJSONObject("location").getString("name");
                                profilePictureView.setProfileId(object.getString("id"));
                                profilePictureView.setVisibility(View.VISIBLE);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday,relationship_status,location");
            request.setParameters(parameters);
            request.executeAsync();
            Intent intent = new Intent(MainActivity.this, RoutesActivity.class);
            startActivity(intent);


        }
        else {
            profilePictureView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GPSTracker gps = new GPSTracker(this);
                    if (gps.canGetLocation()){
                        Log.d("GPS","Latitude : "+gps.getLatitude()+" Longitude : "+gps.getLongitude());
                    }
                    else {
                        Log.d("GPS","Failed");
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to acces location, finish", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG,"Connection: "+mGoogleApiClient.isConnected());
        Intent intent = new Intent(MainActivity.this, RoutesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"Error :"+connectionResult.getErrorCode());
        if (connectionResult.hasResolution()) {

            try {
                connectionResult.startResolutionForResult(this, CONNECT);
            } catch (IntentSender.SendIntentException e) {
               Log.d(TAG,"StartResolution failed.");
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }
}
