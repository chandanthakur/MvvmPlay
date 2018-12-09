package com.banyan.mvvmplay.Chat;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.banyan.mvvmplay.R;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;


/**
 * Adapter to hook call logs data with recycler view
 * All the data is attached to view using Android Binding, Avoid adding any specific logic to this class
 */
public class ChatRecyclerViewAdapter
        extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

    private ObservableList<IVmChatItem> items;
    Map<Integer, Integer> itemTypeToLayoutIdMap;
    private final WeakReferenceOnListChangedCallback onListChangedCallback;
    private Context context;

    ChatRecyclerViewAdapter(Context ctx, ObservableList<IVmChatItem> data, Map<Integer, Integer> map){
        context = ctx;
        items = data;
        this.onListChangedCallback = new WeakReferenceOnListChangedCallback<>(this);
        itemTypeToLayoutIdMap = map;
        items.addOnListChangedCallback(this.onListChangedCallback);
    }

    @Override
    public ChatRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(!itemTypeToLayoutIdMap.containsKey(viewType)) {
            // throw explicit message for developers
            return null;
        }

        int layoutId = itemTypeToLayoutIdMap.get(viewType);
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                layoutId, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (items != null)
        {
            items.removeOnListChangedCallback(this.onListChangedCallback);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IVmChatItem item = items.get(position);
        holder.bindData(item);
    }

    @Override
    public int getItemViewType(int position){
        IVmChatItem holderData = items.get(position);
        return holderData.getItemType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding itemBinding;
        IChatItemView itemContainer;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        public void bindData(IVmChatItem item) {
            itemContainer = itemBinding.getRoot().findViewById(R.id.itemView);
            itemContainer.setVm(item);
        }
    }

    private static class WeakReferenceOnListChangedCallback<T> extends ObservableList.OnListChangedCallback
    {

        private final WeakReference<ChatRecyclerViewAdapter> adapterReference;

        public WeakReferenceOnListChangedCallback(ChatRecyclerViewAdapter bindingRecyclerViewAdapter)
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
            RecyclerView.Adapter adapter = adapterReference.get();
            if (adapter != null)
            {
                adapter.notifyItemRangeRemoved(positionStart, itemCount);
            }
        }
    }
}
