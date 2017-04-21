package aron.com.myapplication;

import com.facebook.GraphResponse;

import org.json.JSONObject;

/**
 * Created by Yunwen on 4/21/2017.
 */

public interface Callback {
    void complete(GraphResponse graphResponse, JSONObject jsonObject);
    void fail();
}
