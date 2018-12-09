package com.banyan.mvvmplay.Chat;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.banyan.mvvmplay.R;
import com.banyan.mvvmplay.databinding.ChatViewBinding;

import java.util.HashMap;
import java.util.Map;


public class ChatView extends FrameLayout {

    private static final String LOG_PREFIX = "CallView:";

    private static final Map<Integer, Integer> itemTypeToLayoutIdMap = new HashMap<Integer, Integer>(){
        {
            put(ChatItemType.Message.getValue(), R.layout.chat_item_message_container);
            put(ChatItemType.News.getValue(), R.layout.chat_item_news_container);
        }
    };

    private final Context context;
    private final VmChat vmChat;
    private ChatRecyclerViewAdapter listAdapter;

    private EndlessScrollListener mScrollListener;

    public ChatView(Context context) {
        this(context, null, 0);
    }

    public ChatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        vmChat = VmLocator.getVmChat();
        initializeView();
    }

    private void initializeView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        ChatViewBinding binding = ChatViewBinding.inflate(inflater, this, true);
        binding.setVm(vmChat);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        //mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        binding.listView.setLayoutManager(mLayoutManager);
        listAdapter = new ChatRecyclerViewAdapter(context, vmChat.getChatItems(), itemTypeToLayoutIdMap);
        binding.listView.setAdapter(listAdapter);

        mScrollListener = new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int firstVisibleItem, int lastVisibleItem) {
                vmChat.onActiveItemsChange(firstVisibleItem, lastVisibleItem);
            }
        };

        binding.listView.addOnScrollListener(mScrollListener);
    }
}