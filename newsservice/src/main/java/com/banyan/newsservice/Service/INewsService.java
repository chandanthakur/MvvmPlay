package com.banyan.newsservice.Service;


import retrofit2.http.GET;
import retrofit2.http.Query;
import io.reactivex.Observable;

public interface INewsService {
    @GET("/v2/top-headlines?apiKey=01ba049a99e24416a4c4410aac354107")
    Observable<NewsResponse> getTopHeadlines(@Query("country") String country);
}
