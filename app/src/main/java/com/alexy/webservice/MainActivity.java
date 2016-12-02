package com.alexy.webservice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton loginButton;
    String email,name,gender,birthday,currentCity;
    ProfilePictureView profilePictureView;
    EditText editTextLogin,editTextPassword;
    Button connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);
        editTextLogin = (EditText) findViewById(R.id.editTextLogin);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        connect = (Button) findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RoutesActivity.class);
                startActivity(intent);
            }
        });
//        profilePictureView = (ProfilePictureView) findViewById(R.id.image);
//        loginButton = (LoginButton) findViewById(R.id.login_button);
//        loginButton.setReadPermissions(Arrays.asList(
//                "public_profile", "email", "user_birthday", "user_friends","user_relationships","user_location"));
//        callbackManager = CallbackManager.Factory.create();
//        updateView();
//        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//            updateView();
//            }
//        };
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }
//
//    public boolean isLoggedIn() {
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        return accessToken != null;
//    }
//
//    public void updateView(){
//        if (isLoggedIn()){
//
//            GraphRequest request = GraphRequest.newMeRequest(
//                    AccessToken.getCurrentAccessToken(),
//                    new GraphRequest.GraphJSONObjectCallback() {
//                        @Override
//                        public void onCompleted(JSONObject object, GraphResponse response) {
//                            Log.v("LoginActivity", response.toString());
//
//                            // Application code
//                            try {
//                                email = object.getString("email");
//                                birthday = object.getString("birthday"); // 01/31/1980 format
//                                gender = object.getString("gender");
//                                name = object.getString("name");
//                                currentCity = object.getJSONObject("location").getString("name");
////                                profilePictureView.setProfileId(object.getString("id"));
////                                profilePictureView.setVisibility(View.VISIBLE);
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//            Bundle parameters = new Bundle();
//            parameters.putString("fields", "id,name,email,gender,birthday,relationship_status,location");
//            request.setParameters(parameters);
//            request.executeAsync();
//
//
//        }
//        else {
////            profilePictureView.setVisibility(View.INVISIBLE);
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case 1: {
//
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    GPSTracker gps = new GPSTracker(this);
//                    if (gps.canGetLocation()){
//                        Log.d("GPS","Latitude : "+gps.getLatitude()+" Longitude : "+gps.getLongitude());
//                    }
//                    else {
//                        Log.d("GPS","Failed");
//                    }
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    Toast.makeText(MainActivity.this, "Permission denied to acces location, finish", Toast.LENGTH_SHORT).show();
//                    this.finish();
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }

}
