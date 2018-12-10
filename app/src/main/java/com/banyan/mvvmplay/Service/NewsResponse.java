package com.banyan.mvvmplay.Service;


import com.banyan.mvvmplay.Repository.NewsArticleSchema;

import java.util.List;

public class NewsResponse {

    public String status;

    public String totalResults;

    public List<NewsArticleSchema> articles;
}
