package aron.com.myapplication.fragment;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.Set;

import aron.com.myapplication.R;

/**
 * Created by Yunwen on 4/20/2017.
 */

public class LoginFragment extends Fragment{

    public final static String TAG = "LoginFragment";
    private static LoginFragment mInstance;
    private View view;
    private LoginButton btnLogin;
    private FriendsPermissionListener mListener;
    private CallbackManager callbackManager;
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d(TAG, "onSuccess() accessToken: " + loginResult.getAccessToken().toString());
        }

        @Override
        public void onCancel() {
            Log.d(TAG, "onCancel()");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d(TAG, "onError() message: " + error.getMessage());
        }
    };

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach()");
        try {
            mListener = (FriendsPermissionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FriendsPermissionListener");
        }
    }

    public static synchronized LoginFragment getInstance() {
        if (mInstance == null) {
            mInstance = new LoginFragment();
        }
        return mInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView()");
        view = inflater.inflate(R.layout.fragment_login, container, false);
        callbackManager = CallbackManager.Factory.create();
        btnLogin = (LoginButton) view.findViewById(R.id.facebook_sign_in_button);
        btnLogin.registerCallback(callbackManager, callback);
        btnLogin.setFragment(this);
        btnLogin.setReadPermissions(Arrays.asList("public_profile", "user_friends"));
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() requestCode: " + requestCode);
        if (checkPermissions()) {
            mListener.onFriendsPermissionObtained();
        }
    }

    public interface FriendsPermissionListener {
        void onFriendsPermissionObtained();
    }

    private boolean checkPermissions() {
        boolean hasPermission = false;
        AccessToken token = AccessToken.getCurrentAccessToken();
        Log.d(TAG, "AccessToken: " + token);
        if (token != null) {
            Log.d(TAG, "isExpired: " + token.isExpired());
            if (!token.isExpired()) {
                Set<String> permissions = token.getPermissions();
                for (String permission : permissions) {
                    Log.d(TAG, "permission: " + permission);
                    if (permission.equals("user_friends")) {
                        hasPermission = true;
                    }
                }
            }
        }
        return hasPermission;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach()");
    }
}
