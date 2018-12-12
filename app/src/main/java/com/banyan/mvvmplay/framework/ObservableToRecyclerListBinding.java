package com.banyan.mvvmplay.framework;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Reflect changes in the observable list to recycler view adapter list
 * @param <T>
 */
public class ObservableToRecyclerListBinding<T> extends ObservableList.OnListChangedCallback
{
    private static final String TAG = ObservableToRecyclerListBinding.class.getSimpleName();

    private final WeakReference<RecyclerView.Adapter> adapterReference;

    public ObservableToRecyclerListBinding(RecyclerView.Adapter bindingRecyclerViewAdapter)
    {
        this.adapterReference = new WeakReference<>(bindingRecyclerViewAdapter);
    }

    @Override
    public void onChanged(ObservableList sender)
    {
        RecyclerView.Adapter adapter = adapterReference.get();
        if (adapter != null)
        {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount)
    {
        RecyclerView.Adapter adapter = adapterReference.get();
        if (adapter != null)
        {
            adapter.notifyItemRangeChanged(positionStart, itemCount);
        }
    }

    @Override
    public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount)
    {
        Log.i(TAG, String.format("onItemRangeInserted, start %d, count %d", positionStart, itemCount));
        RecyclerView.Adapter adapter = adapterReference.get();
        if (adapter != null)
        {
            adapter.notifyItemRangeInserted(positionStart, itemCount);
        }
    }

    @Override
    public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount)
    {
        RecyclerView.Adapter adapter = adapterReference.get();
        if (adapter != null)
        {
            adapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

    @Override
    public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount)
    {
        Log.i(TAG, String.format("onItemRangeRemoved, start %d, count %d", positionStart, itemCount));
        RecyclerView.Adapter adapter = adapterReference.get();
        if (adapter != null)
        {
            adapter.notifyItemRangeRemoved(positionStart, itemCount);
        }
    }
}
