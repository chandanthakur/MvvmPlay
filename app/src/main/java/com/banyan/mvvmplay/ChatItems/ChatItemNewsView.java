package com.banyan.mvvmplay.ChatItems;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.banyan.mvvmplay.Chat.IChatItemView;
import com.banyan.mvvmplay.Chat.IChatItemViewEvents;
import com.banyan.mvvmplay.Chat.IVmChatItem;
import com.banyan.mvvmplay.databinding.ChatItemNewsViewBinding;


/**
 * CallLogView, used ListView to show recent call logs, Follows MVVM pattern
 */
public class ChatItemNewsView extends FrameLayout implements IChatItemView, IChatItemViewEvents {
    private final Context context;

    private ChatItemNewsViewBinding binding;

    private VmChatItemNews vmChatItem;

    public ChatItemNewsView(Context context) {
        this(context, null, 0);
    }

    public ChatItemNewsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatItemNewsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        this.binding = ChatItemNewsViewBinding.inflate(inflater, this, true);
        this.binding.setEvents(this);
        this.binding.setView(this);
    }

    @Override
    public void setVm(IVmChatItem vm) {
        this.vmChatItem = (VmChatItemNews)vm;
        binding.setVm(vmChatItem);
    }

    @Override
    public void onViewClick() {
        Toast.makeText(getContext(), "onListItemClick", Toast.LENGTH_SHORT).show();
    }

    public static String getNewsDateInFormat() {
        return null;
    }

    public void onIconClick() {
        Toast.makeText(getContext(), "onIconClick", Toast.LENGTH_SHORT).show();
    }
}