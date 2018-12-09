package com.banyan.mvvmplay.Chat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessScrollListener.class.getSimpleName();

    private static final int visibleThreshold = 10;

    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    private int mFirstVisibleItem = -1;
    private int mLastVisibleItem = -1;

    EndlessScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleITem = mLinearLayoutManager.findLastVisibleItemPosition();
        if(firstVisibleItem != mFirstVisibleItem || lastVisibleITem != mLastVisibleItem) {
            mFirstVisibleItem = firstVisibleItem;
            mLastVisibleItem = lastVisibleITem;
            onLoadMore(mFirstVisibleItem, mLastVisibleItem);
        }
    }


    public abstract void onLoadMore(int firstVisibleItemIndex, int lastVisibleItemIndex);
}