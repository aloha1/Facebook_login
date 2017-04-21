package aron.com.myapplication.utils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import aron.com.myapplication.model.Friend;
import aron.com.myapplication.model.Picture;

/**
 * Created by Yunwen on 4/20/2017.
 */

public class FriendDeserializer implements JsonDeserializer<Friend> {
    private static final String TAG = "FriendDeserializer";

    public FriendDeserializer() {

    }

    @Override
    public Friend deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        Friend friend = gson.fromJson(json, Friend.class);
        JsonObject obj = json.getAsJsonObject();
        if (obj.has("picture")) {
            JsonObject picObj = obj.getAsJsonObject("picture");
            JsonObject picData = picObj.getAsJsonObject("data");
            Picture picture = gson.fromJson(picData, Picture.class);
            friend.setPicture(picture);
        }
        return friend;
    }
}
