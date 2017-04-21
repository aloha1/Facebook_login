package aron.com.myapplication.utils;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import aron.com.myapplication.model.Friend;

/**
 * Created by Yunwen on 4/20/2017.
 */

public class JsonUtils {
    private static final String TAG = "JsonUtils";
    public List<Friend> parseJsonObjectToFriendList(JSONObject jsonObject) {
        List<Friend> friendList = new ArrayList<>();
        try {
            if (jsonObject.has("data")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (jsonArray != null && jsonArray.length() > 0) {
                    JSONObject obj;

                    GsonBuilder builder = new GsonBuilder();
                    builder.registerTypeAdapter(Friend.class, new FriendDeserializer());
                    Gson gson = builder.create();
                    Friend friend;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        obj = jsonArray.getJSONObject(i);
                        friend = gson.fromJson(obj.toString(), Friend.class);
                        friendList.add(friend);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "parsing Json Error", e);
        }
        return friendList;
    }
}
