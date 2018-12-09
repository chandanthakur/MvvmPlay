package com.banyan.mvvmplay.ChatItems;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.banyan.mvvmplay.Chat.IChatItemView;
import com.banyan.mvvmplay.Chat.IVmChatItem;
import com.banyan.mvvmplay.databinding.ChatItemMessageViewBinding;
import com.bumptech.glide.Glide;


/**
 * CallLogView, used ListView to show recent call logs, Follows MVVM pattern
 */
public class ChatItemMessageView extends FrameLayout  implements IChatItemView {
    private final Context context;

    private ChatItemMessageViewBinding binding;

    private VmChatItemMessage vmChatItem;

    public ChatItemMessageView(Context context) {
        this(context, null, 0);
    }

    public ChatItemMessageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatItemMessageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        this.binding = ChatItemMessageViewBinding.inflate(inflater, this, true);
    }

    @Override
    public void setVm(IVmChatItem vm) {
        this.vmChatItem = (VmChatItemMessage)vm;
        binding.setVm( vmChatItem);
    }
}