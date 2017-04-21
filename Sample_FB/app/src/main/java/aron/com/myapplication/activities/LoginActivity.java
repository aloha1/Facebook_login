package aron.com.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import aron.com.myapplication.R;
import aron.com.myapplication.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.FriendsPermissionListener{

    private final static String TAG = "LoginActivity";
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            Log.d(TAG, "Bundle is clear, transaction add");
            LoginFragment fragment = LoginFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_login, fragment);
            transaction.addToBackStack(LoginFragment.TAG);
            transaction.commit();
        } else {
            Log.d(TAG, "Last Login existed");
        }
        checkPermissions();
    }

    private void checkPermissions() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            if (!token.isExpired()) {
                Set<String> permissions = token.getPermissions();
                List<String> expectedPermList = Arrays.asList("user_friends", "user_posts");
                token.getPermissions().containsAll(expectedPermList);
                if (permissions.containsAll(expectedPermList)) {
                    startFriendsActivity();
                } else {
                    LoginManager.getInstance().logInWithReadPermissions(this, expectedPermList);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed()");
    }

    public void startFriendsActivity() {
        startActivity(new Intent(this, FriendListActivity.class));
    }

    @Override
    public void onFriendsPermissionObtained() {
        Log.d(TAG, "onFriendsPermissionObtained()" );
        startFriendsActivity();
    }
//
//    private CallbackManager callbackManager;
//    private TextView info;
//    private LoginButton loginButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//
//        //Check if user is currently logged in
//        if(AccessToken.getCurrentAccessToken() == null){
//            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
//            Log.d("TAG","Not login yet");
//        }
//
//        if (AccessToken.getCurrentAccessToken() != null){
//            Toast.makeText(this, "Already Login, Transacting...", Toast.LENGTH_LONG).show();
//            Log.d("TAG","Already in");
//            Intent intent = new Intent(LoginActivity.this, FriendListActivity.class);
//            intent.putExtra("AccessToken", AccessToken.getCurrentAccessToken());
//            LoginActivity.this.startActivity(intent);
//        }
//        setContentView(R.layout.activity_main);
//
//        info = (TextView)findViewById(R.id.info);
//        loginButton = (LoginButton)findViewById(R.id.login_button);
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                info.setText("User ID:  " +
//                        loginResult.getAccessToken().getUserId() + "\n" +
//                        "Application:  " +
//                        loginResult.getAccessToken().getApplicationId() + "\n" +
//                        "Auth Token: " + loginResult.getAccessToken().getToken());
//                Intent intent = new Intent(LoginActivity.this, FriendListActivity.class);
//                intent.putExtra("AccessToken", loginResult.getAccessToken().getToken());
//                LoginActivity.this.startActivity(intent);
//            }
//
//            @Override
//            public void onCancel() {
//                info.setText("Login attempt cancelled.");
//            }
//
//            @Override
//            public void onError(FacebookException e) {
//                info.setText("Login attempt failed.");
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }
}
