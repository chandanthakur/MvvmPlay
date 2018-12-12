package com.banyan.newsservice.Service;

import java.util.concurrent.Callable;

import io.reactivex.Observable;

import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsService {
    static NewsService service;

    private Observable<INewsService> serviceInterfaceObservable;

    private INewsService serviceInterface;

    private NewsService() {
        // Zero cost at getInstance(), cost should be paid on first call in async manner
        serviceInterfaceObservable = this.getServiceInterface();
    }

    public static NewsService getInstance() {
        if(service == null) {
            service = new NewsService();
        }

        return service;
    }

    private INewsService getServiceInterfaceSync() {
        if(this.serviceInterface != null) {
            return this.serviceInterface;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.serviceInterface = retrofit.create(INewsService.class);
        return this.serviceInterface;
    }

    // This thread needs optimization, do we want a thread every time
    private Observable<INewsService> getServiceInterface() {
        return Observable.fromCallable(new Callable<INewsService>() {
            @Override
            public INewsService call() throws Exception {
                return getServiceInterfaceSync();
            }
        }).subscribeOn(Schedulers.computation()).cache();
    }


    public Observable<NewsResponse> headlines(final String country) {
        return serviceInterfaceObservable.flatMap(new Function<INewsService, ObservableSource<NewsResponse>>() {
            @Override
            public ObservableSource<NewsResponse> apply(INewsService serviceInterface) throws Exception {
                return serviceInterface.getTopHeadlines(country);
            }
        });
    }
}
