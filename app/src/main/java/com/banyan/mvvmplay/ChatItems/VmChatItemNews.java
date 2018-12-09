package com.banyan.mvvmplay.ChatItems;

import android.databinding.ObservableField;

import com.banyan.mvvmplay.Chat.ChatItemSchema;
import com.banyan.mvvmplay.Chat.ChatItemType;
import com.banyan.mvvmplay.Chat.IVmChatItem;
import com.banyan.mvvmplay.Service.NewsArticleSchema;

/**
 * ViewModel for CallLog
 */
public final class VmChatItemNews implements IVmChatItem {

    ObservableField<String> title = new ObservableField<>();
    ObservableField<String> description = new ObservableField<>();
    ObservableField<String> imageSrc = new ObservableField<>();
    ObservableField<String> publishedAt = new ObservableField<>();

    public VmChatItemNews(String chatId){
    }

    public void setData(ChatItemSchema itemSchema) {
        NewsArticleSchema article = (NewsArticleSchema) itemSchema.payload;
        title.set(article.title);
        description.set(article.content);
        imageSrc.set(article.urlToImage);
        publishedAt.set(article.publishedAt);
    }

    public ObservableField<String> getTitle() {
        return title;
    }

    public ObservableField<String> getDescription() {
        return description;
    }

    public ObservableField<String> getImageSrc() {
        return imageSrc;
    }

    public ObservableField<String> getPublishedAt() {
        return publishedAt;
    }

    @Override
    public int getItemType() {
        return ChatItemType.News.getValue();
    }
}

