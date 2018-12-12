package com.banyan.mvvmplay.ChatItems;

import android.databinding.ObservableField;

import com.banyan.mvvmplay.Chat.ChatItemSchema;
import com.banyan.mvvmplay.Chat.ChatItemType;
import com.banyan.mvvmplay.Chat.IVmChatItem;
import com.banyan.newsservice.Repository.NewsArticleSchema;

/**
 * ViewModel for CallLog
 */
public final class VmChatItemMessage implements IVmChatItem {

    ObservableField<String> title = new ObservableField<>();
    ObservableField<String> description = new ObservableField<>();

    public VmChatItemMessage(String chatId){
    }

    public void setData(ChatItemSchema itemSchema) {
        NewsArticleSchema article = (NewsArticleSchema) itemSchema.payload;
        title.set(article.title);
        description.set(article.content);
    }

    public ObservableField<String> getTitle() {
        return title;
    }

    public ObservableField<String> getDescription() {
        return description;
    }

    @Override
    public int getItemType() {
        return ChatItemType.Message.getValue();
    }

    public void save() {

    }
}

