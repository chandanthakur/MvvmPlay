package com.banyan.mvvmplay.Repository;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "NewsArticleTable")
public class NewsArticleSchema {

    @PrimaryKey
    @NonNull
    public String url;

    public String author;

    public String title;

    public String description;

    public String urlToImage;

    public String publishedAt;

    public String content;

}
