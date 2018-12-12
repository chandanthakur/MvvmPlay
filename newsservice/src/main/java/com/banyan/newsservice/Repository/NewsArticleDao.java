package com.banyan.newsservice.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface NewsArticleDao  {
    @Query("SELECT * from NewsArticleTable ORDER BY publishedAt ASC")
    LiveData<List<NewsArticleSchema>> getAllArticles();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NewsArticleSchema word);

    @Query("DELETE FROM NewsArticleTable")
    void deleteAll();
}