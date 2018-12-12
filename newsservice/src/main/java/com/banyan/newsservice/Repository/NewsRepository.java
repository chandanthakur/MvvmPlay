package com.banyan.newsservice.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


class NewsRepository {
    private NewsArticleDao mNewsArticleDao;

    private LiveData<List<NewsArticleSchema>> mAllArticles;

    NewsRepository(Application application) {
        NewsArticleDatabase db = NewsArticleDatabase.getDatabase(application);
        mNewsArticleDao = db.newsArticleDao();
        mAllArticles = mNewsArticleDao.getAllArticles();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<NewsArticleSchema>> getAllArticles() {
        return mAllArticles;
    }

    void insert(final List<NewsArticleSchema> articles) {
        Observable.defer(new Callable<ObservableSource<?>>() {
            @Override
            public ObservableSource<?> call() throws Exception {
                for(NewsArticleSchema article: articles) {
                    mNewsArticleDao.insert(article);
                }

                return null;
            }
        })
        .subscribeOn(Schedulers.io())
        .subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

            }
        });
    }
}
