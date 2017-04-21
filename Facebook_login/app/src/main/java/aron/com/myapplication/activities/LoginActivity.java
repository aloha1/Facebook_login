package aron.com.myapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

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
        initFacebook();
        setContentView(R.layout.activity_login);
        checkExistLogin(savedInstanceState);
        checkPermissions();
    }

    private void initFacebook(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    private void checkExistLogin(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            LoginFragment fragment = LoginFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_login, fragment);
            transaction.addToBackStack(LoginFragment.TAG);
            transaction.commit();
        } else {
            Toast.makeText(getApplicationContext(),"Last Login existed",Toast.LENGTH_SHORT).show();
        }
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

    public void startFriendsActivity() {
        startActivity(new Intent(this, FriendListActivity.class));
    }

    @Override
    public void onFriendsPermissionObtained() {
        startFriendsActivity();
    }
}
