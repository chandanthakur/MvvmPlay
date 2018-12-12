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
import com.banyan.mvvmplay.framework.ObservableToRecyclerListBinding;

import java.util.Map;


/**
 * Adapter for Recycler view
 */
public class ChatRecyclerViewAdapter
        extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

    private ObservableList<IVmChatItem> items;
    Map<Integer, Integer> itemTypeToLayoutIdMap;
    private final ObservableToRecyclerListBinding onListChangedCallback;
    private Context context;

    ChatRecyclerViewAdapter(Context ctx, ObservableList<IVmChatItem> data, Map<Integer, Integer> map){
        context = ctx;
        items = data;
        this.onListChangedCallback = new ObservableToRecyclerListBinding<>(this);
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
}
