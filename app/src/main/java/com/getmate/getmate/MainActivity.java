package com.getmate.getmate;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.constraint.solver.SolverVariable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.getmate.getmate.Class.User;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements Animation.AnimationListener{
    Animation animation;
    LoginButton fbButton;
    FacebookCallback<LoginResult> callback;
    CallbackManager callbackManager;
    ImageView logo;
    boolean boolean_login;
    User p;
    private List<String> permissionNeeds = Arrays.asList("public_profile,email,user_birthday,user_location");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initialize();
        animation.setAnimationListener(MainActivity.this);
        logo.setVisibility(View.VISIBLE);
        logo.startAnimation(animation);
        p = new User();
        TextView tx = (TextView)findViewById(R.id.welcometext);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "abc.ttf");

        tx.setTypeface(custom_font);
        loginwithFb();
        fbButton.registerCallback(callbackManager, callback);

        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(boolean_login)
                {
                    boolean_login = false;
                    LoginManager.getInstance().logOut();
                }
                else
                {
                    LoginManager.getInstance().logInWithReadPermissions(MainActivity.this,
                            permissionNeeds);
                    loginwithFb();
                }

            }
        });

    }

    private void loginwithFb()
    {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("ONSUCCESS", "User ID: " + loginResult.getAccessToken().getUserId()
                        + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken()
                );

                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                Log.e("ONSUCCESS", "User ID: " + loginResult.getAccessToken().getUserId()
                        + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken()+" Curr "+accessToken
                );
                GraphRequest request = GraphRequest.newMeRequest(
                      loginResult.getAccessToken() ,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {

                                     boolean_login = true;
                                    Log.e("object ", object.toString());


                                    try {
                                       Log.e("Email",object.getString("email"));
                                       p.setEmail(object.getString("email"));
                                    } catch (Exception e) {
                                        Log.e("email ",e+"");
                                        e.printStackTrace();
                                    }

                                    try {
                                         Log.e("ID ",object.getString("id"));

                                    } catch (Exception e) {
                                       Log.e("Excp id",e+"");
                                        e.printStackTrace();

                                    }
                                    try {
                                        Log.e("Gender",object.getString("gender"));
                                        p.setGender(object.getString("gender"));
                                    } catch (Exception e) {
                                        Log.e("EXcp Gender ",e+"");
                                        e.printStackTrace();
                                    }

                                    try {
                                       Log.e("Birthday ",object.getString("birthday"));
                                       p.setBirthday(object.getString("birthday"));
                                    } catch (Exception e) {
                                        Log.e("EXcp Birth",e+"");
                                        e.printStackTrace();
                                    }

                                    try {
                                        JSONObject jsonobject_location = object.getJSONObject("location");
                                       Log.e("Loca ",jsonobject_location.getString("name"));
                                    } catch (Exception e) {
                                        Log.e("Exc loc ",e+"");
                                        e.printStackTrace();
                                    }
                                    fn_profilepic();

                                } catch (Exception e) {

                                }
                            }
                        });
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                if (AccessToken.getCurrentAccessToken() == null) {
                    return; // already logged out
                }
                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();
                        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile,email"));
                        loginwithFb();

                    }
                }).executeAsync();


            }

            @Override
            public void onError(FacebookException e) {
                Log.e("ON ERROR", "Login attempt failed.");


                AccessToken.setCurrentAccessToken(null);
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile,email,user_birthday"));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    protected void onStop() {
        super.onStop();

    }

    private void fn_profilepic() {

        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        params.putString("type", "large");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "me/picture",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        Log.e("Response 2", response + "");

                        try {
                            String str_facebookimage = (String) response.getJSONObject().getJSONObject("data").get("url");
                            Log.e("Picture", str_facebookimage);

                            p.setImageUrl(str_facebookimage);
                            Intent i = new Intent(MainActivity.this,InterestActivity.class);
                            i.putExtra("Profile",p);
                            startActivity(i);
                            finish();
                           // Glide.with(MainActivity.this).load(str_facebookimage).skipMemoryCache(true).into(iv_image);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
        ).executeAsync();


    }

    private void fn_emailid() {

        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        params.putString("type", "large");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "me/email",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        Log.e("Response 2", response + "");

                        try {
                            String str_facebookemail = (String) response.getJSONObject().getJSONObject("data").get("url");
                            Log.e("email agya", str_facebookemail);

                            p.setEmail(str_facebookemail);

                            // Glide.with(MainActivity.this).load(str_facebookimage).skipMemoryCache(true).into(iv_image);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("Email nahii ayyaaa",e+" ");
                        }


                    }
                }
        ).executeAsync();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }catch (Exception e)
        {

        }
    }


    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();
    }




    private void initialize() {
        callbackManager = CallbackManager.Factory.create();
        fbButton = (LoginButton) findViewById(R.id.btnfb);
        logo = (ImageView) findViewById(R.id.logo);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate);
        fbButton.setReadPermissions(permissionNeeds);
       // fbButton.setPublishPermissions(permissionNeeds);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

}
