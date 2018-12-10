package com.banyan.mvvmplay.Chat;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.banyan.mvvmplay.R;
import com.banyan.mvvmplay.databinding.ChatViewBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


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

    private ChatViewBinding chatViewBinding;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private RecyclerViewScrollListener mScrollListener;

    public ChatView(Context context) {
        this(context, null, 0);
    }

    public ChatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        vmChat = VmLocator.getVmChat("id"); //TODO/chthakur: How to flow this id is important as well? pending
        initializeView();
    }

    private void initializeView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        chatViewBinding = ChatViewBinding.inflate(inflater, this, true);
        chatViewBinding.setVm(vmChat);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        chatViewBinding.listView.setLayoutManager(mLayoutManager);
        listAdapter = new ChatRecyclerViewAdapter(context, vmChat.getChatItems(), itemTypeToLayoutIdMap);
        chatViewBinding.listView.setAdapter(listAdapter);

        mScrollListener = new RecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int firstVisibleItem, int lastVisibleItem) {
                // We should relinquish the thread here. The following method could change
                // The internal data structure of the recycler view
                // This implies conflict given this is synchronous on scroll
                triggerLoadMore(firstVisibleItem, lastVisibleItem);
            }
        };

        chatViewBinding.listView.addOnScrollListener(mScrollListener);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        compositeDisposable.dispose();
    }

    // Ensure we relinquish the synchronous call within scroll listener and then call this method
    // Are there going to be some delay? Possible but this should be safe.
    // TODO: chthakur: Consider revisiting if there is better API to know the items in view
    private void triggerLoadMore(final int firstVisibleItem, final int lastVisibleItem) {
        io.reactivex.Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                vmChat.onActiveItemsChange(firstVisibleItem, lastVisibleItem);
                return true;
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                vmChat.onActiveItemsChange(firstVisibleItem, lastVisibleItem);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("ChatView", "onError" , e);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}