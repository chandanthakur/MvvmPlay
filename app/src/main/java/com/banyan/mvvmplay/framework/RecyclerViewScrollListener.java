package com.banyan.mvvmplay.framework;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager mLinearLayoutManager;
    private int mFirstVisibleItem = -1;
    private int mLastVisibleItem = -1;

    public RecyclerViewScrollListener(LinearLayoutManager linearLayoutManager) {
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
            triggerLoadMore(mFirstVisibleItem, mLastVisibleItem);
        }
    }

    private void triggerLoadMore(int firstVisibleItemIndex, int lastVisibleItemIndex) {
        onLoadMore(firstVisibleItemIndex, lastVisibleItemIndex);
    }

    public abstract void onLoadMore(int firstVisibleItemIndex, int lastVisibleItemIndex);
}