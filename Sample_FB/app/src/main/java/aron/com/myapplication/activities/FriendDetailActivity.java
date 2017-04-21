package aron.com.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import aron.com.myapplication.R;
import aron.com.myapplication.utils.Friend;

/**
 * Created by Yunwen on 4/20/2017.
 */

public class FriendDetailActivity extends Activity{
    public static final String EXTRA_FRIEND = "EXTRA_FRIEND";
    private ImageView photoImage;
    private Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        friend = (Friend) getIntent().getSerializableExtra(EXTRA_FRIEND);
        setTitle(friend.getName());
        photoImage = (ImageView) findViewById(R.id.image_friend);
        Picasso.with(this).load(friend.getPicture().url).into(photoImage);
    }
}
