package aron.com.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import aron.com.myapplication.R;
import aron.com.myapplication.model.Friend;

/**
 * Created by Yunwen on 4/20/2017.
 */

public class FriendDetailActivity extends Activity{
    public static final String EXTRA_FRIEND = "EXTRA_FRIEND";
    private ImageView imagePhoto;
    private TextView textBirthday,textPhone,textEmail, textGender;
    private Button btnName;
    private Friend friend;
    private static boolean firstClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        initView();
    }

    private void initView(){
        friend = (Friend) getIntent().getSerializableExtra(EXTRA_FRIEND);
        btnName = (Button) findViewById(R.id.btn_name);
        imagePhoto = (ImageView) findViewById(R.id.row_image);
        textBirthday = (TextView) findViewById(R.id.text_birthday); textPhone = (TextView) findViewById(R.id.text_phone);
        textEmail = (TextView) findViewById(R.id.text_email); textGender = (TextView) findViewById(R.id.text_gender);
        btnName.setText(friend.getName());
        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstClick) {
                    textBirthday.setVisibility(v.VISIBLE); textPhone.setVisibility(v.VISIBLE);
                    textEmail.setVisibility(v.VISIBLE); textGender.setVisibility(v.VISIBLE);
                    firstClick = false;
                } else {
                    textBirthday.setVisibility(v.GONE); textPhone.setVisibility(v.GONE);
                    textEmail.setVisibility(v.GONE); textGender.setVisibility(v.GONE);
                    firstClick = true;
                }
            }
        });
        Picasso.with(this).load(friend.getPicture().url).into(imagePhoto);
    }
}
