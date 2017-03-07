package com.example.barry215.woo.Adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by laucherish on 16/3/4.
 */
public abstract class AutoLoadOnScrollListener extends RecyclerView.OnScrollListener {
    private boolean loading = false;
    int totalItemCount, lastVisibleItem;

    private LinearLayoutManager mLinearLayoutManager;

    public AutoLoadOnScrollListener(
            LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        totalItemCount = mLinearLayoutManager.getItemCount();
        lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

        //当最后一个可见的item在倒数三个以内时
        if (!loading && (lastVisibleItem > totalItemCount - 3) && dy > 0) {
            onLoadMore();
            loading = true;
        }
    }

    public abstract void onLoadMore();

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}
