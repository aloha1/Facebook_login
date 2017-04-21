package aron.com.myapplication.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.List;
import aron.com.myapplication.R;
import aron.com.myapplication.model.Friend;


public class FriendListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "FriendListAdapter";
    public final int TYPE_FRIEND = 0;
    public final int TYPE_LOAD = 1;

    private List<Friend> friendList;
    private int visibleThreshold = 5;
    private int lastVisible, itemCount;
    private boolean loading;
    private OnLoadMoreListener loadMoreListener;
    private final OnItemClickListener onItemClickListener;


    public FriendListAdapter(List<Friend> friends, RecyclerView recyclerView, OnItemClickListener onItemClickListener) {
        friendList = friends;
        this.onItemClickListener = onItemClickListener;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();
            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int x, int y) {
                            super.onScrolled(recyclerView, x, y);
                            itemCount = linearLayoutManager.getItemCount();
                            lastVisible = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && itemCount <= (lastVisible + visibleThreshold)) {
                                loading = true;
                                if (loadMoreListener != null) {
                                    loadMoreListener.onLoadMore();
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == TYPE_FRIEND) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.recycler_row_friend, parent, false);

            holder = new FriendHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.recycler_row_load, parent, false);
            holder = new LoadHolder(v);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FriendHolder) {
            Friend friend= friendList.get(position);
            Log.d(TAG, friend.getName());
            ((FriendHolder) holder).tvName.setText(friend.getName());
            Picasso.with((((FriendHolder) holder).civFriends.getContext()))
                    .load(friend.getPicture().url).into(((FriendHolder) holder).civFriends);
            ((FriendHolder) holder).bind(friendList.get(position), onItemClickListener);
        } else {
            ((LoadHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemViewType(int position) {
        return friendList.get(position) != null ? TYPE_FRIEND : TYPE_LOAD;
    }


    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class FriendHolder extends RecyclerView.ViewHolder{
        ImageView civFriends;
        TextView tvName;

        public FriendHolder(View itemView) {
            super(itemView);
            civFriends = (ImageView) itemView.findViewById(R.id.row_image);
            tvName = (TextView)itemView.findViewById(R.id.row_text_name);
        }

        public void bind(final Friend item, final OnItemClickListener listener) {
            tvName.setText(item.getName());
            Picasso.with(itemView.getContext()).load(item.getPicture().url).into(civFriends);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }


    public static class LoadHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    interface OnLoadMoreListener{
        void onLoadMore();
    }

    interface OnItemClickListener{
        void onItemClick(Friend item);
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}