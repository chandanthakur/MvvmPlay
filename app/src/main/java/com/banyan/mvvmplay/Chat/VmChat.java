package com.banyan.mvvmplay.Chat;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.util.Log;

import com.banyan.mvvmplay.ChatItems.VmChatItemMessage;
import com.banyan.mvvmplay.ChatItems.VmChatItemNews;
import com.banyan.mvvmplay.Service.INewsService;
import com.banyan.mvvmplay.Service.NewsArticleSchema;
import com.banyan.mvvmplay.Service.NewsResponse;
import com.banyan.mvvmplay.Service.NewsService;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * ViewModel for CallLog
 */
public final class VmChat {

    public static String TAG = VmChat.class.getSimpleName();

    ObservableField<String> title = new ObservableField<>();

    ObservableList<IVmChatItem> chatItems = new ObservableArrayList<>();

    HashMap<String, IVmChatItem> vmChatItemMap = new HashMap<>();

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    int index = 0;

    int loadIndex = 0;

    boolean isLoadingMore = false;

    String[] countryList = {"us", "in", "uk", "ca", "us", "in", "uk" };

    VmChat(){
        fetchNews(countryList[loadIndex%countryList.length]); loadIndex++;
    }

    private void fetchNews(String country) {
        NewsService.getInstance().headlines(country)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<NewsResponse>() {
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
            public void onComplete() {

            }
        });
    }

    private void onNewsResponse(NewsResponse newsResponse) {
        if(newsResponse == null || newsResponse.articles == null) {
            return;
        }

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
        chatItem.id = "Message: " + index++;
        chatItem.payload = article;
        VmChatItemMessage vmChatItem = new VmChatItemMessage(chatItem.id);
        vmChatItem.setData(chatItem);
        return vmChatItem;
    }

    private IVmChatItem getNewsItem(NewsArticleSchema article) {
        ChatItemSchema chatItem = new ChatItemSchema();
        chatItem.id = "News: " + index++;
        chatItem.payload = article;
        VmChatItemNews vmChatItem = new VmChatItemNews(chatItem.id);
        vmChatItem.setData(chatItem);
        return vmChatItem;
    }

    public ObservableField<String> getTitle() {
        return title;
    }

    public ObservableList<IVmChatItem> getChatItems() {
        return chatItems;
    }

    public void onActiveItemsChange(int firstActiveItem, int lastActiveItem) {
        Log.i("VmChat", "loadMore:firstActiveItem:" + firstActiveItem + ",lastActiveItem:" + lastActiveItem);
    }


    public void loadMore() {
        if(isLoadingMore) {
            return;
        }

        isLoadingMore = true;
        Log.i("VmChat", "loadMore");
        fetchNews(countryList[loadIndex%countryList.length]); loadIndex++;
    }

    public void release() {
        compositeDisposable.dispose();
    }
}

