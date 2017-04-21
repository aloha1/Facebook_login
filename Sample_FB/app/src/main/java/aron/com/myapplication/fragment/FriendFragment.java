package aron.com.myapplication.fragment;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import aron.com.myapplication.R;
import aron.com.myapplication.activities.FriendDetailActivity;
import aron.com.myapplication.utils.Friend;
import aron.com.myapplication.utils.JsonUtils;

import static aron.com.myapplication.activities.FriendDetailActivity.EXTRA_FRIEND;

/**
 * Created by Yunwen on 4/20/2017.
 */

public class FriendFragment extends Fragment{

    public final static String TAG = "FriendFragment";
    private static FriendFragment mInstance;
    private View view;
    private RecyclerView rvFriendList;
    private LinearLayoutManager linearLayoutManager;
    private List<Friend> friendList = new ArrayList<>();
    private FriendListAdapter friendListAdapter;
    private TextView tvEmptyList;
    private GraphRequest nextRequest;

    public static synchronized FriendFragment getInstance() {
        if (mInstance == null) {
            mInstance = new FriendFragment();
        }
        return mInstance;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        if (!checkAccessToken()) {
            getActivity().finish();
        }
    }

    private boolean checkAccessToken() {
        if (AccessToken.getCurrentAccessToken() == null ||
                AccessToken.getCurrentAccessToken().isExpired()) {
            return false;
        }
        return true;
    }

    private boolean requestNextFriends(GraphRequest pendingRequest) {
        if (pendingRequest == null) {
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putString("fields", "id,name,picture");
        pendingRequest.setParameters(bundle);
        pendingRequest.setCallback(DefaultCallback(new Callback() {
            @Override
            public void complete(GraphResponse graphResponse, JSONObject jsonObject) {
                onNextRequestComplete(graphResponse, jsonObject);
            }

            @Override
            public void fail() {
                onFriendsRequestFail();
            }
        }));
        pendingRequest.executeAsync();
        return true;
    }

    private void requestFriends() {
        if (friendList.size() > 0) {
            return;
        }
        Bundle params = new Bundle();
        params.putString("fields", "id,name,picture");
        params.putString("limit", "8");
        GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(),
                "me/taggable_friends",
                params,
                HttpMethod.GET,
                DefaultCallback(new Callback() {
                    @Override
                    public void complete(GraphResponse graphResponse, JSONObject jsonObject) {
                        onFriendsRequestComplete(graphResponse, jsonObject);
                    }

                    @Override
                    public void fail() {
                        onFriendsRequestFail();
                    }
                }));
        graphRequest.executeAsync();
    }

    public void onNextRequestComplete(GraphResponse graphResponse, JSONObject jsonObject) {
        List<Friend> newFriends = new JsonUtils().parseJsonObjectToFriendList(jsonObject);
        friendList.remove(friendList.size() - 1);
        new Handler().post(new Runnable() {
            public void run() {
                friendListAdapter.notifyItemRemoved(friendList.size());
            }
        });
        int end = newFriends.size();
        for (int i = 0; i < end; i++) {
            friendList.add(newFriends.get(i));
            new Handler().post(new Runnable() {
                public void run() {
                    friendListAdapter.notifyItemInserted(friendList.size());
                }
            });
        }
        friendListAdapter.setLoaded();
        nextRequest = graphResponse.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
    }

    public void onFriendsRequestComplete(GraphResponse graphResponse, JSONObject jsonObject) {
        if (friendList == null) Toast.makeText(getContext(),"Refreshing",Toast.LENGTH_SHORT).show();
        List<Friend> newFriends = new JsonUtils().parseJsonObjectToFriendList(jsonObject);
        friendList.addAll(newFriends);
        friendListAdapter.notifyDataSetChanged();
        rvFriendList.setVisibility(View.VISIBLE);
        tvEmptyList.setVisibility(View.GONE);
        nextRequest = graphResponse.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
    }

    public void onFriendsRequestFail() {
        Toast.makeText(getContext(),"fail to get Friends",Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        rvFriendList = (RecyclerView) view.findViewById(R.id.friends_view);
        rvFriendList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFriendList.setLayoutManager(linearLayoutManager);
        friendListAdapter = new FriendListAdapter(friendList, rvFriendList, new FriendListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Friend item) {
                Intent intent = new Intent(getActivity(), FriendDetailActivity.class);
                intent.putExtra(EXTRA_FRIEND, item);
                getActivity().startActivity(intent);
            }
        });
        rvFriendList.setAdapter(friendListAdapter);
        tvEmptyList = (TextView) view.findViewById(R.id.tv_empty_list);
        if (friendList.isEmpty()) {
            rvFriendList.setVisibility(View.GONE);
            tvEmptyList.setVisibility(View.VISIBLE);
        } else {
            rvFriendList.setVisibility(View.VISIBLE);
            tvEmptyList.setVisibility(View.GONE);
        }
        friendListAdapter.setLoadMoreListener(new FriendListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                friendList.add(null);
                new Handler().post(new Runnable() {
                    public void run() {
                        friendListAdapter.notifyItemInserted(friendList.size() - 1);
                    }
                });

                if (!requestNextFriends(nextRequest)) {
                    Toast.makeText(getContext(),"Last Request",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "no more nextRequest");
                    friendList.remove(friendList.size() - 1);
                    new Handler().post(new Runnable() {
                        public void run() {
                            friendListAdapter.notifyItemRemoved(friendList.size());
                        }
                    });
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestFriends();
    }

    private GraphRequest.Callback DefaultCallback(final Callback callback) {
        GraphRequest.Callback GraphRequestcallback = new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                if (graphResponse.getError() != null) {
                    callback.fail();
                    Log.d(TAG, "graphResponse Error: " + graphResponse.getError().getErrorMessage());
                } else {
                    callback.complete(graphResponse, graphResponse.getJSONObject());
                }
            }
        };
        return GraphRequestcallback;
    }


    public interface Callback {
        void complete(GraphResponse graphResponse, JSONObject jsonObject);
        void fail();
    }
}
