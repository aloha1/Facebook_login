package aron.com.myapplication.activities;

/**
 * Created by Yunwen on 4/29/2016.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import aron.com.myapplication.R;
import aron.com.myapplication.fragment.FriendFragment;

public class FriendListActivity extends AppCompatActivity {

    private final static String TAG = "FriendListActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        if (savedInstanceState == null) {
            FriendFragment fragment = FriendFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_list, fragment);
            transaction.addToBackStack(FriendFragment.TAG);
            transaction.commit();
        } else {
            Toast.makeText(getApplicationContext(),"Last Login Exist",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
