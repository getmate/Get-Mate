package com.getmate.getmate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.getmate.getmate.Class.Location;
import com.getmate.getmate.Class.User;
import com.getmate.getmate.Class.UserAuthentication;
import com.getmate.getmate.Class.fbUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * Created by HP on 24-06-2017.
 */

public class GetMateLogin extends AppCompatActivity
{
    User Fuser = new User();
    LoginButton facebookSignup;
    CallbackManager callbackManager;
    //Activity activity;
    private List<String> permissionNeeds = Arrays.asList("public_profile, email, user_birthday");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        callbackManager = CallbackManager.Factory.create();
        facebookSignup = (LoginButton) findViewById(R.id.btnfbsignup);
      //  activity = this;
        final EditText uidEdit = (EditText) findViewById(R.id.username);
        final EditText passEdit = (EditText) findViewById(R.id.password);
        Button btn = (Button) findViewById(R.id.btnLogin);

        facebookSignup.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("Main", response.toString());
                                //setProfileToView(object);
                                Log.v("JSOM",object.toString());
                                Gson gs = new Gson();
                                fbUser user = gs.fromJson(String.valueOf(object),fbUser.class);
                                Log.e("email",user.getEmail()+""+user.getName());
                                Log.e("id b g ",user.getId()+" "+user.getBirthday());
                                Log.e("Gender",user.getGender()+"");
                                Fuser.setEmail(user.getEmail());
                                Fuser.setUsername(user.getName());
                                Fuser.setUid(user.getId());
                                Fuser.setBirthday(user.getBirthday());
                                Fuser.setGender(user.getGender());
                                fn_profilepic();

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

   /////////////////////////////// Sign Up with Get-MAte //////////////////////////////////////
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constant.URL+"user/authentication";
                String json = "";
                UserAuthentication u = new UserAuthentication();
                String uid,pass;
                uid  = uidEdit.getText()+"";
                pass = passEdit.getText()+"";

                if(uid.equals(null)||uid.equals(""))
                {
                    Toast.makeText(GetMateLogin.this,"Invalid username",Toast.LENGTH_LONG).show();

                }else if(pass.equals("")||pass.equals(null))
                {
                    Toast.makeText(GetMateLogin.this,"Invalid password",Toast.LENGTH_LONG).show();
                }else{
                u.setUid(uid);
                u.setPassword(pass);
                Gson g = new Gson();
                json =g.toJson(u);
                new HttpPostAuthentication().execute(url,json);



                }
            }
        });



    }

    class HttpPostAuthentication extends AsyncTask<String,Void,String>
    {
        ProgressDialog pd = new ProgressDialog(GetMateLogin.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setTitle("Loading...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String urlString = params[0];
            Log.e("url string",params[0]);
            HttpDataHandler http = new HttpDataHandler();
            String json = params[1];
            String response = http.PostHttpData(urlString,json);
            Log.e("do In Background ",response);
            Gson gs = new Gson();
            Fuser = gs.fromJson(response,User.class);
            Location location = new Location();
            location.setX(12.988799);
            location.setY(80.229451);
            Fuser.setLocation(location);
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Intent i = new Intent(GetMateLogin.this,MainClass.class);
            i.putExtra("Profile",Fuser);
            Log.e("Work",Fuser.getWork().get(0).getPosition()+" "
            +Fuser.getWork().get(0).getCompany());
            getApplicationContext().startActivity(i);
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();
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

                            Fuser.setImageUrl(str_facebookimage);
                            Intent i = new Intent(GetMateLogin.this,EditProfile.class);
                            i.putExtra("Profile",Fuser);
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

}
