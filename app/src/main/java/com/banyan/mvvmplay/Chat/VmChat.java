package com.banyan.mvvmplay.Chat;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.util.Log;
import android.util.Pair;

import com.banyan.mvvmplay.ChatItems.VmChatItemMessage;
import com.banyan.mvvmplay.ChatItems.VmChatItemNews;
import com.banyan.mvvmplay.Repository.NewsArticleSchema;
import com.banyan.mvvmplay.Service.NewsResponse;
import com.banyan.mvvmplay.Service.NewsService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.Callable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * ViewModel for Chat
 */
public final class VmChat {

    private static String TAG = VmChat.class.getSimpleName();

    private ObservableField<String> title = new ObservableField<>();

    private ObservableItemsList<IVmChatItem> chatItems = new ObservableItemsList<>();

    private ObservableField<Pair<Integer, Integer>> trimEvent = new ObservableField<>();

    // Required for cancellation
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private int loadIndex = 0;

    private boolean isLoadingMore = false;

    private String[] countryList = {"us", "in", "uk", "ca", "us", "in", "uk" };

    VmChat(String chatId){
        fetchNews(countryList[loadIndex%countryList.length]); loadIndex++;
    }

    public ObservableField<String> getTitle() {
        return title;
    }

    public ObservableList<IVmChatItem> getChatItems() {
        return chatItems;
    }

    public ObservableField<Pair<Integer, Integer>> onTrimEvent() {
        return trimEvent;
    }

    void onActiveItemsChange(int firstActiveItem, int lastActiveItem) {
        final int nItemsAtTopBeforeLoadMore = 20;
        int nItemsAtTopForTrimming = 2*nItemsAtTopBeforeLoadMore;
        Log.i("VmChat", String.format("onActiveItemsChange: firstActive: %d, lastActive %d, totalCount %d",
                firstActiveItem, lastActiveItem, chatItems.size()));
        if(firstActiveItem < nItemsAtTopBeforeLoadMore) {
            Log.i("VmChat", String.format("loadMore:nItemsAtTop:%d, loadThreshold: %d", firstActiveItem, nItemsAtTopBeforeLoadMore));
            loadMore();
        } else if(firstActiveItem > nItemsAtTopForTrimming) {
            Log.i("VmChat", String.format("trimTop:nItemsAtTop:%d, trimThreshold: %d", firstActiveItem, nItemsAtTopBeforeLoadMore));
            // Important to change thread to main again to relinquish the current flow of control
            // This is required to ensure we don't do that on sync way, limitation
            trimTop(nItemsAtTopBeforeLoadMore);
        }
    }

    private Observer<NewsResponse> newsObserver = new Observer<NewsResponse>() {
        @Override
        public void onSubscribe(Disposable d) {
            compositeDisposable.add(d);
        }

        @Override
        public void onNext(NewsResponse newsResponse) {
            onNewsResponse(newsResponse);
            Log.d("", newsResponse.totalResults);
        }

        @Override
        public void onError(Throwable e) {
            Log.w(TAG, "fetchNews:onErrror:", e);
        }

        @Override
        public void onComplete() {}
    };


    private void fetchNews(String country) {
        NewsService.getInstance().headlines(country)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(newsObserver);
    }

    private void onNewsResponse(NewsResponse newsResponse) {
        if(newsResponse == null || newsResponse.articles == null) {
            return;
        }

        // First batch the items to add in local list
        List<IVmChatItem> itemsToAdd = new LinkedList<>();
        for(NewsArticleSchema article: newsResponse.articles) {
            if(article.hashCode()%2 == 0) {
                IVmChatItem item = getNewsItem(article);
                itemsToAdd.add(item);
            } else {
                IVmChatItem item = getMessageItem(article);
                itemsToAdd.add(item);
            }
        }

        // Add to observable list by batching
        chatItems.addAll(0, itemsToAdd);
        isLoadingMore = false;
    }

    private IVmChatItem getMessageItem(NewsArticleSchema article) {
        ChatItemSchema chatItem = new ChatItemSchema();
        chatItem.id = "Message: " + article.hashCode();
        chatItem.payload = article;
        VmChatItemMessage vmChatItem = new VmChatItemMessage(chatItem.id);
        vmChatItem.setData(chatItem);
        return vmChatItem;
    }

    private IVmChatItem getNewsItem(NewsArticleSchema article) {
        ChatItemSchema chatItem = new ChatItemSchema();
        chatItem.id = "News: " + article.hashCode();
        chatItem.payload = article;
        VmChatItemNews vmChatItem = new VmChatItemNews(chatItem.id);
        vmChatItem.setData(chatItem);
        return vmChatItem;
    }

    public void loadMore() {
        if(isLoadingMore) {
            Log.i("VmChat", "loadMore:skipped");
            return;
        }

        isLoadingMore = true;
        Log.i("VmChat", "loadMore:triggered");
        fetchNews(countryList[loadIndex%countryList.length]); loadIndex++;
    }

    private void trimTop(int nItemsToTrim) {
        Log.i("VmChat", String.format("trimTop:triggered, beginCount:%d", chatItems.size()));
        List<IVmChatItem> itemsToTrim = new LinkedList<>();
        int ii = 0;
        for(IVmChatItem chatItem: this.chatItems) {
            itemsToTrim.add(chatItem);
            if(ii >= nItemsToTrim) {
                break;
            }

            ii++;
        }

        // Pending, crash fix required here, was expecting this to work
        // We need to first notify to adapter and then trim
        // Solution is to raise event to view layer and then view layer will first
        // notify the the removal to adapter and then modify the VmList
        chatItems.removeRange(0, nItemsToTrim);
        Log.i("VmChat", String.format("trimTop:triggered, endCount:%d", chatItems.size()));
    }



    public void release() {
        compositeDisposable.dispose();
    }
}

