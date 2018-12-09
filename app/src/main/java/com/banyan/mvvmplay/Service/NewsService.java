package com.banyan.mvvmplay.Service;

import io.reactivex.Observable;

import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsService {
    static NewsService service;

    private INewsService serviceInterface;



    private NewsService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.serviceInterface = retrofit.create(INewsService.class);
    }

    public static NewsService getInstance() {
        if(service == null) {
            service = new NewsService();
        }

        return service;
    }

    public Observable<NewsResponse> headlines(String country) {
        return this.serviceInterface.getTopHeadlines(country);
    }
}
