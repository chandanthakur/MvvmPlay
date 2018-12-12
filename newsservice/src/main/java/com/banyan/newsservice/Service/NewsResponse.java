package com.banyan.newsservice.Service;


import com.banyan.newsservice.Repository.NewsArticleSchema;

import java.util.List;

public class NewsResponse {

    public String status;

    public String totalResults;

    public List<NewsArticleSchema> articles;
}
