package com.example.thesparksfoundation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;


public class MainActivity extends AppCompatActivity {

    TwitterLoginButton loginButton;
    TextView textView;
    int co;

    LoginButton loginButtonfa;
    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.activity_main);

//        loginButtonfa.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();


        //facebook
        loginButtonfa = findViewById(R.id.login_button1);


        //Twitter
        textView=findViewById(R.id.textView);
        loginButton = findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                co = 1;

                final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;


                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
                Call<User> call = twitterApiClient.getAccountService().verifyCredentials(true, false, true);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {

                        String email = result.data.email;
                        String username = result.data.name;
                        String profile = result.data.profileImageUrl.replace("_normal", "");


                        Intent intent = new Intent(MainActivity.this,Home.class);
                        intent.putExtra("username",username);
                        intent.putExtra("emafa",email);
                        intent.putExtra("imagefa",profile);
                        startActivity(intent);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Do something on failure
                    }
                });

            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getBaseContext(), "Fail" , Toast.LENGTH_LONG).show();
            }
        });






        //Facebook

        loginButtonfa.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserProfile(AccessToken.getCurrentAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

    }


    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            String username =first_name + " " +last_name;

                            Intent intent = new Intent(MainActivity.this,Home.class);
                            intent.putExtra("emafa",email);
                            intent.putExtra("imagefa",image_url);
                            intent.putExtra("username",username);
                            startActivity(intent);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();




    }



        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);


                callbackManager.onActivityResult(requestCode, resultCode, data);
                loginButton.onActivityResult(requestCode, resultCode, data);



    }
}
